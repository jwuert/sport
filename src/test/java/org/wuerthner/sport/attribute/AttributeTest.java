package org.wuerthner.sport.attribute;

import org.junit.Test;
import org.wuerthner.sample.SampleFactory;
import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.Arrays;
import java.util.List;

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
}
