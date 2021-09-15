package org.wuerthner.sport.api;

public interface Modifier<Element extends ModelElement> {
    public void modify(Element element);
}
