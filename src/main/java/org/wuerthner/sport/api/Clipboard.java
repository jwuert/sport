package org.wuerthner.sport.api;

import java.util.List;

public interface Clipboard<Element extends ModelElement> {
    public List<Element> getElements();
    public void setElements(List<Element> elementList);
}
