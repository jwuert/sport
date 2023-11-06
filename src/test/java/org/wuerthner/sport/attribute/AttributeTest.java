package org.wuerthner.sport.attribute;

import org.junit.Test;
import org.wuerthner.sample.*;
import org.wuerthner.sport.SimpleDate;
import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AttributeTest {

    @Test
    public void testAddAttribute() {
        MyElement myElement = new MyElement();
    }

    public static class MyElement extends AbstractModelElement {
        public static final IdAttribute id = new IdAttribute()
                .label("Id")
                .strictPattern(false);

        public MyElement() {
            super("MyElement", Arrays.asList(), Arrays.asList(id));
            StringAttribute sa = new StringAttribute("sa");
            this.addAttribute(sa);
        }
    }

    @Test
    public void testAttributes() {
        ModelElement school = new SampleBuilder("demo", "demo", 12, true)
                .addCourse("C1", "English", 47).done()
                .addCourse("C2", "Math", 48).done()
                .addCourse("C3", "Geography", 19)
                .addParticipant("jw", "Jan")
                .addParticipant("pv", "Peter").done().done()
                .addTest()
                .build();
        {
            // School
            assertEquals("demo", school.getAttributeValue(School.ID));
            assertEquals("demo", school.getAttributeValue(School.NAME));
            assertEquals(Integer.valueOf(12), school.getAttributeValue(School.REP));
            assertEquals(true, school.getAttributeValue(School.ACTIVE));
        }
        {
            // Course
            ModelElement course = school.lookupByFullId("demo.C2").get();
            assertEquals("C2", course.getAttributeValue(Course.ID));
            assertEquals("Math", course.getAttributeValue(Course.TITLE));
            assertEquals(Integer.valueOf(48), course.getAttributeValue(Course.SCORE));
        }
        {
            // TestObject
            ModelElement testObject = school.getChildrenByType(TestObject.TYPE).get(0);

            assertEquals("Untitled", testObject.getAttributeValue(TestObject.id));
            assertEquals(Course.class, testObject.getAttributeValue(TestObject.classAttribute));
            assertEquals("C2", testObject.getAttributeValue(TestObject.dynamicSelStringAttribute));
            assertEquals("sport-file-test", testObject.getAttributeValue(TestObject.fileAttribute));
            assertEquals(Long.valueOf(123456789000000000L), testObject.getAttributeValue(TestObject.longAttribute));
            assertEquals("This is my message", testObject.getAttributeValue(TestObject.msgAttribute));
            assertEquals("Multiple lines go into the paragraph!", testObject.getAttributeValue(TestObject.paragraphAttribute));
            assertEquals(Integer.valueOf(1), testObject.getAttributeValue(TestObject.selIntAttribute));
            assertEquals("one", testObject.getAttributeValue(TestObject.selStringAttribute));
            assertEquals(Arrays.asList(new SimpleDate(1), new SimpleDate(2)), testObject.getAttributeValue(TestObject.staticListAttribute));
        }
    }

    @Test
    public void testClassAttribute() {
        TestObject to = new SampleFactory().createElement(TestObject.TYPE);
        Class<?> value = to.getAttributeValue(TestObject.classAttribute);
        assertEquals(String.class, value);
    }
}
