package org.wuerthner.sport;

import org.wuerthner.sample.*;
import org.wuerthner.sport.api.ActionProvider;
import org.wuerthner.sport.api.History;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.*;
import org.wuerthner.sport.util.fop.FOPBuilder;
import org.wuerthner.sport.util.fop.FOPProcessor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Test {
    private ModelElementFactory factory = new SampleFactory();

    @org.junit.Test
    public void testSample() {
        ModelElement school = new SampleBuilder("MHS", "Midland High School", 37, true)
                .addCourse("PH", "physics", 100)
                    .addParticipant("JW", "Jan")
                    .addParticipant("HX", "Hugo")
                    .done()
                .addCourse("MA", "math", 120)
                    .addParticipant("JW", "Jan")
                    .addParticipant("EG", "Emil")
                .build();

        XMLWriter w = new XMLWriter();
        OutputStream os = new ByteArrayOutputStream();
        w.run(school, os);

        String xml = os.toString().lines()
                .collect(Collectors.joining())
                .replaceAll(" +", " ")
                .replaceFirst("timestamp=......................","");
        String expected = "<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"no\"?><root version=\"1.0\"> <SchoolDef id=\"1000\" type=\"School\"> <Attribute key=\"name\" type=\"java.lang.String\">Midland High School</Attribute> <Attribute key=\"active\" type=\"java.lang.Boolean\">true</Attribute> <Attribute key=\"reputation\" type=\"java.lang.Integer\">37</Attribute> <Attribute key=\"id\" type=\"java.lang.String\">MHS</Attribute> <CourseRef id=\"1001\"/> <CourseRef id=\"1004\"/> </SchoolDef> <CourseDef id=\"1001\" type=\"Course\"> <Attribute key=\"score\" type=\"java.lang.Integer\">120</Attribute> <Attribute key=\"id\" type=\"java.lang.String\">MA</Attribute> <Attribute key=\"title\" type=\"java.lang.String\">math</Attribute> <ParticipantRef id=\"1002\"/> <ParticipantRef id=\"1003\"/> </CourseDef> <ParticipantDef id=\"1002\" type=\"Participant\"> <Attribute key=\"name\" type=\"java.lang.String\">Emil</Attribute> <Attribute key=\"id\" type=\"java.lang.String\">EG</Attribute> </ParticipantDef> <ParticipantDef id=\"1003\" type=\"Participant\"> <Attribute key=\"name\" type=\"java.lang.String\">Jan</Attribute> <Attribute key=\"id\" type=\"java.lang.String\">JW</Attribute> </ParticipantDef> <CourseDef id=\"1004\" type=\"Course\"> <Attribute key=\"score\" type=\"java.lang.Integer\">100</Attribute> <Attribute key=\"id\" type=\"java.lang.String\">PH</Attribute> <Attribute key=\"title\" type=\"java.lang.String\">physics</Attribute> <ParticipantRef id=\"1005\"/> <ParticipantRef id=\"1006\"/> </CourseDef> <ParticipantDef id=\"1005\" type=\"Participant\"> <Attribute key=\"name\" type=\"java.lang.String\">Hugo</Attribute> <Attribute key=\"id\" type=\"java.lang.String\">HX</Attribute> </ParticipantDef> <ParticipantDef id=\"1006\" type=\"Participant\"> <Attribute key=\"name\" type=\"java.lang.String\">Jan</Attribute> <Attribute key=\"id\" type=\"java.lang.String\">JW</Attribute> </ParticipantDef></root>";
        assertEquals(expected, xml);
    }

    // @org.junit.Test
    public void testReport() {
        List<String> modelChanges = new ArrayList<>();
        String modelVersion = "?";
        ActionProvider actionProvider = factory.getActionProvider();
        FOPBuilder fb = new FOPBuilder(factory.getAppName(), "Model: " + modelVersion, actionProvider, modelChanges);
        // List<ModelElement> list = Stream.of(typesReduced).map(type -> (ModelElement) factory.createElement(type)).collect(Collectors.toList());
        List<ModelElement> list = factory.createElementList();
        File fopFile = fb.collect(list);
        File pdfFile = new File(fopFile.getAbsolutePath().replaceAll("\\.fop","-X.pdf"));
        FOPProcessor fp = new FOPProcessor(fopFile, pdfFile);
        fp.run();
    }

    @org.junit.Test
    public void testHistory() {
        ModelElement school = new SampleBuilder("MHS", "Midland High School", 37, true)
                .addCourse("PH", "physics", 100)
                .addParticipant("JW", "Jan")
                .addParticipant("HX", "Hugo")
                .done()
                .addCourse("MA", "math", 120)
                .addParticipant("JW", "Jan")
                .build();

        // System.out.println(Model.makeString(school));

        History history = new ModelHistory();

        Participant peter = factory.createElement(Participant.TYPE);
        peter.performTransientSetAttributeValueOperation(Participant.ID, "PV");
        peter.performTransientSetAttributeValueOperation(Participant.NAME, "Peter");
        school.lookupByTypeAndId(Course.TYPE, "MA").get().performAddChildOperation(peter, history);

        // System.out.println(Model.makeString(school));

        assertEquals(true, school.lookupByTypeAndId(Participant.TYPE, "PV").isPresent());
        history.undo();
        assertEquals(false, school.lookupByTypeAndId(Participant.TYPE, "PV").isPresent());
        history.redo();
        assertEquals(true, school.lookupByTypeAndId(Participant.TYPE, "PV").isPresent());

    }

    @org.junit.Test
    public void testDelta() {
        ModelElement school = new SampleBuilder("MHS", "Midland High School", 37, true)
                .addCourse("PH", "physics", 100)
                .addParticipant("JW", "Jan")
                .addParticipant("HX", "Hugo")
                .done()
                .addCourse("MA", "math", 120)
                .addParticipant("JW", "Jan")
                .build();
        school.unifyIds();

        ModelElement school2 = new SampleBuilder("MHS", "Midland High School", 37, true)
                .addCourse("PH", "physics", 100)
                .addParticipant("JW", "Jan")
                .addParticipant("HX", "Hugo")
                .done()
                .addCourse("MA", "math", 121)
                .addParticipant("JW", "Jan")
                .build();

        Comparison c = new Comparison(school, school2);

        Delta delta = c.diff();
        List<Delta.Difference> differences = delta.getDifferences();

        Delta.Difference difference = differences.get(0);

        assertEquals(1, differences.size());
        assertEquals("MA", difference.id);
        assertEquals("score", difference.attributeId);
        assertEquals("Course", difference.elementType);
        assertEquals("Score", difference.attributeLabel);
        assertEquals("MHS.MA", difference.fqid);
        assertEquals("120", difference.value1);
        assertEquals("121", difference.value2);
    }

    @org.junit.Test
    public void testList() {
        ListObject lo = new ListObject();
        System.out.println("lo: " + Model.makeString(lo));
        List<Integer> val = lo.getAttributeValue(ListObject.ia);
        System.out.println("val: " + val);
        // lo.performTransientSetAttributeValueOperation(ListObject.ia, Arrays.asList(4, 5, 6));
        // System.out.println(Model.makeString(lo));
        lo.forceAddAttribute("ia","[1, 2, 3]", List.class);
        System.out.println("lo: " + Model.makeString(lo));
        ModelElement copy = factory.copyTree(lo);
        System.out.println("copy: " + Model.makeString(copy));
    }


}
