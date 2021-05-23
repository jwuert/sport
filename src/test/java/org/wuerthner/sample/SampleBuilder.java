package org.wuerthner.sample;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;

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

    public SampleBuilder done() {
        pointer = pointer.getParent();
        return this;
    }

    public ModelElement build() {
        return pointer.getRoot();
    }
}
