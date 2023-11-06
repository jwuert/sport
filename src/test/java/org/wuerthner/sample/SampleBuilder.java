package org.wuerthner.sample;

import org.wuerthner.sport.SimpleDate;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;

import java.io.File;
import java.util.Arrays;

public class SampleBuilder {
    private static final ModelElementFactory factory = new SampleFactory();
    private ModelElement pointer;

    public SampleBuilder(String id, String name, int reputation, boolean active) {
        ModelElement school = factory.createElement(School.TYPE);
        school.performTransientSetAttributeValueOperation(School.ID, id);
        school.performTransientSetAttributeValueOperation(School.NAME, name);
        school.performTransientSetAttributeValueOperation(School.ACTIVE, active);
        school.performTransientSetAttributeValueOperation(School.REP, reputation);
        pointer = school;
    }

    public SampleBuilder addCourse(String id, String title, int score) {
        ModelElement course = factory.createElement(Course.TYPE);
        course.performTransientSetAttributeValueOperation(Course.ID, id);
        course.performTransientSetAttributeValueOperation(Course.TITLE, title);
        course.performTransientSetAttributeValueOperation(Course.SCORE, score);
        pointer.performTransientAddChildOperation(course);
        pointer = course;
        return this;
    }

    public SampleBuilder addParticipant(String id, String name) {
        ModelElement part = factory.createElement(Participant.TYPE);
        part.performTransientSetAttributeValueOperation(Participant.NAME, name);
        part.performTransientSetAttributeValueOperation(Participant.ID, id);
        pointer.performTransientAddChildOperation(part);
        return this;
    }

    public SampleBuilder addTest() {
        ModelElement test = factory.createElement(TestObject.TYPE);
        test.performTransientSetAttributeValueOperation(TestObject.classAttribute, Course.class);
        test.performTransientSetAttributeValueOperation(TestObject.dynamicSelStringAttribute, "C2");
        test.performTransientSetAttributeValueOperation(TestObject.fileAttribute, "sport-file-test");
        test.performTransientSetAttributeValueOperation(TestObject.longAttribute, 123456789000000000L);
        test.performTransientSetAttributeValueOperation(TestObject.msgAttribute, "This is my message");
        test.performTransientSetAttributeValueOperation(TestObject.paragraphAttribute, "Multiple lines go into the paragraph!");
        test.performTransientSetAttributeValueOperation(TestObject.selIntAttribute, 1);
        test.performTransientSetAttributeValueOperation(TestObject.selStringAttribute, "one");
        test.performTransientSetAttributeValueOperation(TestObject.staticListAttribute, Arrays.asList(new SimpleDate(1), new SimpleDate(2)));
        pointer.performTransientAddChildOperation(test);
        return this;
    }

    public SampleBuilder done() {
        pointer = pointer.getParent();
        return this;
    }

    public ModelElement build() {
        return pointer.getRoot();
    }
}
