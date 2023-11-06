package org.wuerthner.sport;

import org.junit.Test;
import org.wuerthner.sample.Course;
import org.wuerthner.sample.SampleFactory;
import org.wuerthner.sample.School;
import org.wuerthner.sample.TestActionProvider;
import org.wuerthner.sport.action.*;
import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ActionProvider;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.ModelState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ActionTest {
    @Test
    public void testActions() throws IOException {
        ModelElementFactory factory = new SampleFactory();
        ActionProvider ap = new TestActionProvider();
        {
            // ID List
            List<String> idList = ap.getIdList();
            assertTrue(idList.contains(new NewRootElementAction().getId()));
            assertTrue(idList.contains(new NewElementAction().getId()));
            assertTrue(idList.contains(new ImportAction().getId()));
            assertTrue(idList.contains(new ExportAction().getId()));
            assertTrue(idList.contains(new Separator().getId()));
            assertTrue(idList.contains(new UndoAction().getId()));
            assertTrue(idList.contains(new RedoAction().getId()));
            assertTrue(idList.contains(new DeleteAction().getId()));
            assertTrue(idList.contains(new CutAction().getId()));
            assertTrue(idList.contains(new CopyAction().getId()));
            assertTrue(idList.contains(new PasteAction().getId()));
            assertTrue(idList.contains(new ClearAction().getId()));
            assertTrue(idList.contains(new AboutAction().getId()));
            assertTrue(idList.contains(new ValidateAction().getId()));
            assertTrue(idList.contains(new CompareAction().getId()));
            assertTrue(idList.contains(new DebugAction().getId()));
            assertTrue(idList.contains(new GenerateModelReportAction().getId()));
            assertTrue(idList.contains(new GenerateReportAction().getId()));
        }
        {
            // Action List
            List<Action> actionList = ap.getActionList();
            assertEquals(21, actionList.size());
        }
        {
            // Action newRootElement
            School school = new School();
            Optional<Action> newRootElement = ap.getAction("newRootElement");
            ModelState state = new ModelState(school, school);
            assertTrue(newRootElement.isPresent());
            assertEquals(Arrays.asList(), newRootElement.get().getDescription());
            assertEquals("creates a new document", newRootElement.get().getToolTip());
            assertEquals(Arrays.asList(), newRootElement.get().getParameterList(null));
            assertEquals(0, newRootElement.get().getParameterList(school).size());
            assertEquals(false, newRootElement.get().requiresData());
            assertEquals(true, newRootElement.get().isEnabled(null, factory));
            assertEquals(true, newRootElement.get().isEnabled(school, factory));
            Map<String, Object> result = newRootElement.get().invoke(factory, state, new HashMap<>());
            assertEquals(true, result.get("new"));
            assertEquals("Untitled", ((ModelElement)result.get("root")).getAttributeValue(School.ID));
        }
        {
            // Action newElement
            School school = new School();
            Optional<Action> newElement = ap.getAction("newElement");
            ModelState state = new ModelState(school, school);
            assertTrue(newElement.isPresent());
            assertEquals("newElement", newElement.get().getId());
            assertEquals(Arrays.asList(), newElement.get().getDescription());
            assertEquals("Create a new element", newElement.get().getToolTip());
            assertEquals(Arrays.asList(), newElement.get().getParameterList(null));
            assertEquals(2, newElement.get().getParameterList(school).size());
            assertEquals(NewElementAction.PARAMETER_NAME, newElement.get().getParameterList(school).get(0).getName());
            assertEquals(NewElementAction.PARAMETER_TYPE, newElement.get().getParameterList(school).get(1).getName());
            assertEquals(false, newElement.get().requiresData());
            assertEquals(false, newElement.get().isEnabled(null, factory));
            assertEquals(true, newElement.get().isEnabled(school, factory));
            Map<String, Object> result = newElement.get().invoke(factory, state, Map.of("name", "foo", "type", Course.TYPE));
            assertEquals("foo", ((ModelElement)result.get("selection")).getAttributeValue(Course.ID));
        }
        {
            // Action import
            Optional<Action> newElement = ap.getAction("import");
            ModelState state = new ModelState();
            assertTrue(newElement.isPresent());
            assertEquals("import", newElement.get().getId());
            assertEquals(Arrays.asList(), newElement.get().getDescription());
            assertEquals("Imports a document", newElement.get().getToolTip());
            assertEquals(1, newElement.get().getParameterList(null).size());
            assertEquals(ImportAction.PARAMETER_FILE, newElement.get().getParameterList(null).get(0).getName());
            assertEquals(false, newElement.get().requiresData());
            assertEquals(true, newElement.get().isEnabled(null, factory));
            Path tempFile = Files.createTempFile("test", ".xml");
            Files.write(tempFile, "<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?><root timestamp=\"2023-11-06 20:35:34\" version=\"1.0\"><SchoolDef id=\"1000\" type=\"School\"><Attribute key=\"id\" type=\"java.lang.String\">MySchool</Attribute><CourseRef id=\"1001\"/></SchoolDef><CourseDef id=\"1001\" type=\"Course\"><Attribute key=\"id\" type=\"java.lang.String\">Math</Attribute></CourseDef></root>".getBytes(StandardCharsets.UTF_8));
            Map<String, Object> result = newElement.get().invoke(factory, state, Map.of("file", tempFile.toString()));
            ModelElement schoolImport = (ModelElement) result.get("root");
            assertEquals("MySchool", schoolImport.getAttributeValue(School.ID));
            ModelElement course = schoolImport.getChildren().get(0);
            assertEquals("Math", course.getAttributeValue(Course.ID));
        }
        {
            // Action export
            School school = new School();
            Optional<Action> actionOpt = ap.getAction("export");
            ModelState state = new ModelState(school, school);
            assertTrue(actionOpt.isPresent());
            Action action = actionOpt.get();
            assertEquals("export", action.getId());
            assertEquals(Arrays.asList(), action.getDescription());
            assertEquals("Export a document", action.getToolTip());
            assertEquals(1, action.getParameterList(null).size());
            assertEquals(1, action.getParameterList(school).size());
            assertEquals(ExportAction.PARAMETER_FILE, action.getParameterList(null).get(0).getName());
            assertEquals(false, action.requiresData());
            assertEquals(true, action.isEnabled(null, factory));
            assertEquals(true, action.isEnabled(school, factory));
            Path tempFile = Files.createTempFile("test", ".xml");
            Map<String, Object> result = action.invoke(factory, state, Map.of("file", tempFile.toString()));
            String content = Files.readString(tempFile).lines().collect(Collectors.joining())
                    .replaceAll(" +"," ")
                    .replaceFirst("timestamp=......................","");
            String expected = "<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?><root version=\"1.0\"> <SchoolDef id=\"1000\" type=\"School\"> <Attribute key=\"id\" type=\"java.lang.String\">Untitled</Attribute> </SchoolDef></root>";
            assertEquals(expected, content);
        }
    }
}
