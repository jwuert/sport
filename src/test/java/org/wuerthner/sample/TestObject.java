package org.wuerthner.sample;

import org.wuerthner.sport.SimpleDate;
import org.wuerthner.sport.attribute.*;
import org.wuerthner.sport.check.AttributeEquality;
import org.wuerthner.sport.check.True;
import org.wuerthner.sport.core.AbstractModelElement;
import org.wuerthner.sport.core.ElementFilter;

import java.util.Arrays;

public class TestObject extends AbstractModelElement {
    public final static String TYPE = "TestObject";

    public final static IdAttribute id = new IdAttribute();

    public final static ClassAttribute classAttribute = new ClassAttribute("classAttribute")
            .label("Class")
            .defaultValue(String.class);
    public final static DynamicSelectableStringAttribute dynamicSelStringAttribute = new DynamicSelectableStringAttribute("dynamicSelStringAttribute")
            .label("Dyn Sel String")
            .addFilter(new ElementFilter(Course.TYPE, new True()));
    public final static FileAttribute fileAttribute = new FileAttribute("fileAttribute")
            .label("File");
    public final static LongAttribute longAttribute = new LongAttribute("longAttribute")
            .label("Long")
            .defaultValue(0L);
    public final static MessageAttribute msgAttribute = new MessageAttribute("msgAttribute")
            .label("Message")
            .description("A message")
            .twoColumns()
            .readonly();
    public final static ParagraphAttribute paragraphAttribute = new ParagraphAttribute("paragraphAttribute")
            .label("Paragraph");
    public final static SelectableIntegerAttribute selIntAttribute = new SelectableIntegerAttribute("selIntAttribute")
            .label("Sel Int")
            .addValue("one", 1)
            .addValue("two", 2)
            .addValue("nine", 9);
    public final static SelectableStringAttribute selStringAttribute = new SelectableStringAttribute("selStringAttribute")
            .label("Sel String")
            .addValue("one", "One")
            .addValue("two", "Two")
            .addValue("eight", "Eight");
    public final static StaticListAttribute<SimpleDate> staticListAttribute = new StaticListAttribute<>("staticListAttribute", SimpleDate.class)
            .addValue("one", new SimpleDate(1))
            .addValue("two", new SimpleDate(2))
            .addValue("three", new SimpleDate(3));

    public TestObject() {
        super(TYPE, Arrays.asList(), Arrays.asList(id, classAttribute, dynamicSelStringAttribute, fileAttribute, longAttribute,
                msgAttribute, paragraphAttribute, selIntAttribute, selStringAttribute, staticListAttribute ));
    }

    public String getId() {
        return this.getAttributeValue(id);
    }
}
