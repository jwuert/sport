package org.wuerthner.sample;

import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.attribute.ListAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.ArrayList;
import java.util.Arrays;

public class ListObject extends AbstractModelElement {
    public final static String TYPE = "ListObject";

    public final static IdAttribute id = new IdAttribute();
    public final static ListAttribute<Integer> ia = new AttributeBuilder("ia")
            .defaultValue(new ArrayList<>())
            .buildListAttribute(Integer.class);

    public ListObject() {
        super(TYPE, Arrays.asList(), Arrays.asList(id, ia));
    }

    public String getId() {
        return this.getAttributeValue(id);
    }
}