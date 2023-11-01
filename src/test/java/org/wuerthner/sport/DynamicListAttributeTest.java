package org.wuerthner.sport;

import org.junit.Test;
import org.wuerthner.sport.attribute.DynamicListAttribute;
import org.wuerthner.sport.attribute.SelectableIntegerAttribute;
import org.wuerthner.sport.attribute.StaticListAttribute;
import org.wuerthner.sport.attribute.StaticListAttributeTest;
import org.wuerthner.sport.check.True;
import org.wuerthner.sport.core.AbstractModelElement;
import org.wuerthner.sport.core.ElementFilter;

import java.util.Arrays;
import java.util.Date;

public class DynamicListAttributeTest {

    @Test
    public void testListAttribute() {
        TestElement te = new TestElement();

        te.setAttributeValue(TestElement.dl, Arrays.asList("abc", "def"));
        te.setAttributeValue(TestElement.sl, Arrays.asList(new SimpleDate(0), new SimpleDate(new Date(0))));
        te.setAttributeValue(TestElement.sia, 2);
        System.out.println("* " + te.getAttributeMap());

        System.out.println("sia " + ((SelectableIntegerAttribute)te.getAttribute("sia")).getValueMap());
        System.out.println("sia " + te.getAttributeValue(TestElement.sia));
        System.out.println("dl " + ((DynamicListAttribute)te.getAttribute("dl")).getValueMap(te));
        System.out.println("dl " + te.getAttributeValue(TestElement.dl));
        System.out.println("sl " + ((StaticListAttribute)te.getAttribute("sl")).getValueMap());
        System.out.println("sl " + te.getAttributeValue(TestElement.sl));

    }

    private static class TestElement extends AbstractModelElement {
        public static SelectableIntegerAttribute sia = new SelectableIntegerAttribute("sia").values(new String[]{"one", "two", "three"});
        public static DynamicListAttribute<String> dl = new DynamicListAttribute<>("dl", String.class).label("DynamicList").addFilter(
                new ElementFilter("TestElement", new True())
        );
        public static StaticListAttribute<SimpleDate> sl = new StaticListAttribute<>("sl", SimpleDate.class).label("StaticList")
                .addValue("now", new SimpleDate())
                .addValue("tomorrow", new SimpleDate(1))
                .addValue("begin", new SimpleDate(new Date(0)));
        public TestElement() {
            super("TestElement", Arrays.asList(), Arrays.asList(sia, dl, sl));
        }
    }


}
