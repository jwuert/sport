package org.wuerthner.sport.core;

import org.wuerthner.sport.api.ModelElement;

public class ModelState {
    private final ModelElement rootElement;
    private final ModelElement selectedElement;
    private final ModelElement auxiliaryElement;

    public ModelState(ModelElement rootElement, ModelElement selectedElement, ModelElement auxiliaryElement) {
        this.rootElement = rootElement;
        this.selectedElement = selectedElement;
        this.auxiliaryElement = auxiliaryElement;
    }

    public ModelState(ModelElement rootElement) {
        this(rootElement, null, null);
    }

    public ModelState(ModelElement rootElement, ModelElement selectedElement) {
        this(rootElement, selectedElement, null);
    }

    public ModelState() {
        this(null, null, null);
    }

    public boolean hasRootElement() {
        return rootElement != null;
    }

    public boolean hasSelectedElement() {
        return selectedElement != null;
    }

    public boolean hasAuxiliaryElement() {
        return auxiliaryElement != null;
    }

    public ModelElement getRootElement() {
        return rootElement;
    }

    public ModelElement getSelectedElement() {
        return selectedElement;
    }

    public ModelElement getAuxiliaryElement() {
        return auxiliaryElement;
    }
}
