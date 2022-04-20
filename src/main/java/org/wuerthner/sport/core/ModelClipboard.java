package org.wuerthner.sport.core;

import org.wuerthner.sport.api.Clipboard;
import org.wuerthner.sport.api.ModelElement;

import java.util.ArrayList;
import java.util.List;

public class ModelClipboard<Element extends ModelElement> implements Clipboard<Element> {
    private List<Element> elementList = new ArrayList<>();

    @Override
    public List<Element> getElements() {
        return elementList;
    }

    @Override
    public int size() { return elementList.size(); }

    @Override
    public boolean isEmpty() {
        return elementList.isEmpty();
    }

    @Override
    public void setElements(List<Element> elementList) {
        for (Element element : this.elementList) {
            setInClipboard(element, false);
        }
        this.elementList = elementList;
        for (Element element : elementList) {
            setInClipboard(element, true);
        }
    }

    private void setInClipboard(ModelElement element, boolean inClipboard) {
        element.setInClipboad(inClipboard);
        for (ModelElement child: element.getChildren()) {
            setInClipboard(child, inClipboard);
        }
    }
}

