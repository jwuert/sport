package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.ModelState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasteAction implements Action {
    @Override
    public String getId() {
        return "paste";
    }

    @Override
    public String getToolTip() {
        return "Paste";
    }

    @Override
    public boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) {
        return (selectedElement != null && factory.getClipboard().isPresent() && !factory.getClipboard().get().isEmpty());
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String, Object> resultMap = new HashMap<>();
        if (factory != null) {
            if (modelState.hasSelectedElement()) {
                ModelElement selectedElement = modelState.getSelectedElement();
                    if (factory.getClipboard().isPresent()) {
                    List<ModelElement> elementList = Arrays.asList(selectedElement);
                    if (factory.getHistory().isPresent()) {
                        selectedElement.performPasteClipboardOperation(factory.getClipboard().get(), factory, factory.getHistory().get());
                    } else {
                        selectedElement.performTransientPasteClipboardOperation(factory.getClipboard().get(), factory);
                    }
                } else {
                    resultMap.put(ERROR, "No clipboard available!");
                }
            } else {
                resultMap.put(ERROR, "No element selected!");
            }
        } else {
            resultMap.put(ERROR, "No factory given!");
        }
        return resultMap;
    }
}
