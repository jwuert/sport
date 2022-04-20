package org.wuerthner.sport.core;

import org.wuerthner.sport.api.ModelElement;

import java.util.Arrays;

public class NoElement extends AbstractModelElement{
    public final static String TYPE="NoElement";
    public final static ModelElement INSTANCE = new NoElement();

    public NoElement() {
        super(TYPE, Arrays.asList(), Arrays.asList());
    }
}
