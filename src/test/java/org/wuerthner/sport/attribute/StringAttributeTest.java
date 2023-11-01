package org.wuerthner.sport.attribute;

import org.junit.Test;

public class StringAttributeTest {
    @Test
    public void testStringAttribute() {
        StringAttribute stringAttribute = new StringAttribute("test").label("The label");
        System.out.println("* " + stringAttribute.getLabel());
    }
}
