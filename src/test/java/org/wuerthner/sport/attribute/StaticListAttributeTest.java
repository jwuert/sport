package org.wuerthner.sport.attribute;

import org.junit.Test;
import org.wuerthner.sport.SimpleDate;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class StaticListAttributeTest {
    @Test
    public void testList() {
        StaticListAttribute<String> la = new StaticListAttribute<>("sl", String.class).label("StaticList")
                .addValue("one", "eins")
                .addValue("two", "zwei");

        assertEquals("eins", la.getValueMap().get("one"));
        assertEquals("zwei", la.getValueMap().get("two"));
        assertEquals(Arrays.asList("one"), la.getValue("one"));
        assertEquals("[hello, world\\, yea!]", la.getStringPresentation(Arrays.asList("hello", "world, yea!")));
    }
}
