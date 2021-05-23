package org.wuerthner.sport;

import org.wuerthner.sample.*;
import org.wuerthner.sport.api.History;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

public class Test {
    private ModelElementFactory factory = new SampleFactory();

    @org.junit.Test
    public void testSample() {
//        ModelElement school = factory.createElement(School.TYPE);
//        school.performSetAttributeValueOperation(School.NAME, "High School");
//        school.performSetAttributeValueOperation(School.ACTIVE, true);
//        school.performSetAttributeValueOperation(School.REP, 37);
//
//        ModelElement physics = factory.createElement(Course.TYPE);
//        physics.performSetAttributeValueOperation(Course.TITLE, "Physics");
//        physics.performSetAttributeValueOperation(Course.SCORE, 500);
//
//        ModelElement part1 = factory.createElement(Participant.TYPE);
//        part1.performSetAttributeValueOperation(Participant.NAME, "Jan");
//
//        ModelElement part2 = factory.createElement(Participant.TYPE);
//        part2.performSetAttributeValueOperation(Participant.NAME, "Peter");
//
//        school.performTransientAddChildOperation(physics);
//        physics.performTransientAddChildOperation(part1);
//        physics.performTransientAddChildOperation(part2);
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

        String xml = os.toString();
        System.out.println(xml);
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

        System.out.println(Model.makeString(school));
        System.out.println("--- UNDO:");
        history.undo();
        System.out.println(Model.makeString(school));
        System.out.println("--- REDO:");
        history.redo();

        System.out.println(Model.makeString(school));
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
        System.out.println("------------");
        for (Delta.Difference d: differences) {
            System.out.println("- " + d.toString());
        }
    }
}
