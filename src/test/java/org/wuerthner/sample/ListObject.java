package org.wuerthner.sample;

import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.attribute.DynamicListAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.ArrayList;
import java.util.Arrays;

public class ListObject extends AbstractModelElement {
    public final static String TYPE = "ListObject";

    public final static IdAttribute id = new IdAttribute();
    public final static DynamicListAttribute<Integer> ia = new DynamicListAttribute<>("ia", Integer.class)
            .defaultValue(new ArrayList<>());

    public ListObject() {
        super(TYPE, Arrays.asList(), Arrays.asList(id, ia));
    }

    public String getId() {
        return this.getAttributeValue(id);
    }
}