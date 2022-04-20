package org.wuerthner.sport;

import org.junit.Test;
import org.wuerthner.sample.*;
import org.wuerthner.sport.api.*;
import org.wuerthner.sport.core.Model;
import org.wuerthner.sport.core.ModelClipboard;
import org.wuerthner.sport.core.ModelHistory;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class OperationTest {
    private ModelElementFactory factory = new SampleFactory();

    @Test
    public void testCutAndPasteAndUndo() {
        ModelElement school = new SampleBuilder("MHS", "Midland High School", 37, true)
                .addCourse("PH", "physics", 100)
                .addParticipant("JW", "Jan")
                .addParticipant("HX", "Hugo")
                .done()
                .addCourse("MA", "math", 120)
                .addParticipant("JW", "Jan")
                .build();

        History history = new ModelHistory();
        Clipboard<ModelElement> clipboard = new ModelClipboard();

        ModelElement ma;
        ModelElement ph;
        ModelElement hugo;

        // original state:
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        hugo = school.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(noneDeletedOrInClipboard(school));

        // CUT hugo, assert that both courses have only 1 participant and hugo is in clipboard
        hugo.performCutToClipboardOperation(clipboard, Arrays.asList(hugo), history);
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(!school.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        assertTrue(ph.getChildren().size()==1);
        assertTrue(ma.getChildren().size()==1);
        assertTrue(clipboard.getElements().size()==1);

        assertTrue(clipboard.getElements().get(0).isInClipboard());
        assertTrue(clipboard.getElements().get(0).isDeleted());
        assertTrue(clipboard.getElements().get(0).getId().equals("HX"));
        assertTrue(noneDeletedOrInClipboard(school));

        // PASTE into ma, assert that ma has 2 participants, ph has 1, clipboard contains hugo
        ma.performPasteClipboardOperation(clipboard, factory, history);
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(ph.getChildren().size()==1);
        assertTrue(ma.getChildren().size()==2);
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(clipboard.getElements().size()==1);
        assertTrue(clipboard.getElements().get(0).isInClipboard());
        assertTrue(clipboard.getElements().get(0).isDeleted());
        assertTrue(clipboard.getElements().get(0).getId().equals("HX"));
        assertTrue(noneDeletedOrInClipboard(school));
        ModelElement maHugo = ma.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(!maHugo.equals(hugo));
        assertTrue(hugo.getFullId().equals("HX"));
        assertTrue(maHugo.getFullId().equals("MHS.MA.HX"));

        // PASTE into ph, assert that ma and ph have 2 participants, clipboard contains hugo
        ph.performPasteClipboardOperation(clipboard, factory, history);
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(ph.getChildren().size()==2);
        assertTrue(ma.getChildren().size()==2);
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(clipboard.getElements().size()==1);
        assertTrue(clipboard.getElements().get(0).isInClipboard());
        assertTrue(clipboard.getElements().get(0).isDeleted());
        assertTrue(clipboard.getElements().get(0).getId().equals("HX"));
        assertTrue(noneDeletedOrInClipboard(school));
        maHugo = ma.lookupByTypeAndId(Participant.TYPE, "HX").get();
        ModelElement phHugo = ph.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(!maHugo.equals(hugo));
        assertTrue(hugo.getFullId().equals("HX"));
        assertTrue(maHugo.getFullId().equals("MHS.MA.HX"));
        assertTrue(phHugo.getFullId().equals("MHS.PH.HX"));

        // UNDO PASTE
        history.undo();
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(ph.getChildren().size()==1);
        assertTrue(ma.getChildren().size()==2);
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(clipboard.getElements().size()==1);
        assertTrue(clipboard.getElements().get(0).isInClipboard());
        assertTrue(clipboard.getElements().get(0).isDeleted());
        assertTrue(clipboard.getElements().get(0).getId().equals("HX"));
        assertTrue(noneDeletedOrInClipboard(school));
        assertTrue(!ph.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        maHugo = ma.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(!maHugo.equals(hugo));
        assertTrue(hugo.getFullId().equals("HX"));
        assertTrue(maHugo.getFullId().equals("MHS.MA.HX"));

        // UNDO PASTE
        history.undo();
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(!school.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        assertTrue(ph.getChildren().size()==1);
        assertTrue(ma.getChildren().size()==1);
        assertTrue(clipboard.getElements().size()==1);
        assertTrue(clipboard.getElements().get(0).isInClipboard());
        assertTrue(clipboard.getElements().get(0).isDeleted());
        assertTrue(clipboard.getElements().get(0).getId().equals("HX"));
        assertTrue(noneDeletedOrInClipboard(school));
        assertTrue(!ma.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        assertTrue(!ph.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        assertTrue(ma.lookupByTypeAndId(Participant.TYPE, "JW").isPresent());
        assertTrue(ph.lookupByTypeAndId(Participant.TYPE, "JW").isPresent());

        // UNDO CUT
        history.undo();
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(clipboard.getElements().size()==0);
        assertTrue(ma.getChildren().size()==1);
        assertTrue(ph.getChildren().size()==2);
        assertTrue(!ma.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        assertTrue(ph.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        assertTrue(ma.lookupByTypeAndId(Participant.TYPE, "JW").isPresent());
        assertTrue(ph.lookupByTypeAndId(Participant.TYPE, "JW").isPresent());
        assertTrue(noneDeletedOrInClipboard(school));
    }

    @Test
    public void testCopyPaste() {
        ModelElement school = new SampleBuilder("MHS", "Midland High School", 37, true)
                .addCourse("PH", "physics", 100)
                .addParticipant("JW", "Jan")
                .addParticipant("HX", "Hugo")
                .done()
                .addCourse("MA", "math", 120)
                .addParticipant("JW", "Jan")
                .build();

        History history = new ModelHistory();
        Clipboard<ModelElement> clipboard = new ModelClipboard();

        ModelElement ma;
        ModelElement ph;
        ModelElement hugo;

        // original state:
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        hugo = school.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(noneDeletedOrInClipboard(school));

        // COPY
        hugo.performCopyToClipboardOperation(clipboard, Arrays.asList(hugo), factory, history);
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(school.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        assertTrue(ma.getChildren().size()==1);
        assertTrue(ph.getChildren().size()==2);
        assertTrue(clipboard.getElements().size()==1);

        // PASTE
        ma.performPasteClipboardOperation(clipboard, factory, history);
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(ma.getChildren().size()==2);
        assertTrue(ph.getChildren().size()==2);
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(clipboard.getElements().size()==1);
        assertTrue(clipboard.getElements().get(0).isInClipboard());
        assertTrue(clipboard.getElements().get(0).isDeleted());
        assertTrue(clipboard.getElements().get(0).getId().equals("HX"));
        assertTrue(noneDeletedOrInClipboard(school));
        ModelElement maHugo = ma.lookupByTypeAndId(Participant.TYPE, "HX").get();
        ModelElement phHugo = ph.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(!maHugo.equals(hugo));
        assertTrue(phHugo.equals(hugo));
        assertTrue(!clipboard.getElements().get(0).equals(hugo));
        assertTrue(!clipboard.getElements().get(0).equals(phHugo));
        assertTrue(!clipboard.getElements().get(0).equals(maHugo));
        assertTrue(clipboard.getElements().get(0).getFullId().equals("HX"));
        assertTrue(maHugo.getFullId().equals("MHS.MA.HX"));
        assertTrue(phHugo.getFullId().equals("MHS.PH.HX"));

        // UNDO PASTE
        history.undo();
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(ph.getChildren().size()==2);
        assertTrue(ma.getChildren().size()==1);
        assertTrue(!ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(clipboard.getElements().size()==1);
        assertTrue(clipboard.getElements().get(0).isInClipboard());
        assertTrue(clipboard.getElements().get(0).isDeleted());
        assertTrue(clipboard.getElements().get(0).getId().equals("HX"));
        assertTrue(noneDeletedOrInClipboard(school));
        assertTrue(ph.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        phHugo = ph.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(phHugo.equals(hugo));
        assertTrue(hugo.getFullId().equals("MHS.PH.HX"));
        assertTrue(phHugo.getFullId().equals("MHS.PH.HX"));
        assertTrue(clipboard.getElements().get(0).getFullId().equals("HX"));

        // UNDO COPY
        history.undo();
        ma = school.lookupByTypeAndId(Course.TYPE, "MA").get();
        ph = school.lookupByTypeAndId(Course.TYPE, "PH").get();
        assertTrue(ph.getChildren().size()==2);
        assertTrue(ma.getChildren().size()==1);
        assertTrue(!ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ma.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("HX"));
        assertTrue(ph.getChildren().stream().map(el -> el.getId()).collect(Collectors.toList()).contains("JW"));
        assertTrue(clipboard.getElements().size()==0);
        assertTrue(noneDeletedOrInClipboard(school));
        assertTrue(ph.lookupByTypeAndId(Participant.TYPE, "HX").isPresent());
        phHugo = ph.lookupByTypeAndId(Participant.TYPE, "HX").get();
        assertTrue(phHugo.equals(hugo));
        assertTrue(hugo.getFullId().equals("MHS.PH.HX"));
        assertTrue(phHugo.getFullId().equals("MHS.PH.HX"));
    }

    private boolean noneDeletedOrInClipboard(ModelElement element) {
        if (element.isDeleted() || element.isInClipboard()) {
            return false;
        } else {
            for (ModelElement child : element.getChildren()) {
                if (!noneDeletedOrInClipboard(child)) {
                    return false;
                }
            }
        }
        return true;
    }
}
