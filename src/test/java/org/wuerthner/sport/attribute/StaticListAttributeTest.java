package org.wuerthner.sport.attribute;

import org.junit.Test;
import org.wuerthner.sport.SimpleDate;

import java.util.Arrays;
import java.util.Date;

public class StaticListAttributeTest {
    @Test
    public void testList() {
        StaticListAttribute<String> la = new StaticListAttribute<>("sl", String.class).label("StaticList")
                .addValue("one", "eins")
                .addValue("two", "zwei");

        System.out.println(la.getValueMap());
        System.out.println(la.getValue("one"));
        System.out.println(la.getStringPresentation(Arrays.asList("hello", "world, yea!")));
    }
}
