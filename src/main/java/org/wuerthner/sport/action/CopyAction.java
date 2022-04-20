package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.ModelState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CopyAction implements Action {
    @Override
    public String getId() {
        return "copy";
    }

    @Override
    public String getToolTip() {
        return "Copy";
    }

    @Override
    public boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) {
        return (selectedElement != null && factory.getClipboard().isPresent());
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> resultMap = new HashMap<>();
        if (factory!= null && modelState.hasSelectedElement()) {
            ModelElement selectedElement = modelState.getSelectedElement();
            if (factory.getClipboard().isPresent()) {
                List<ModelElement> elementList = Arrays.asList(selectedElement);
                if (factory.getHistory().isPresent()) {
                    selectedElement.performCopyToClipboardOperation(factory.getClipboard().get(), elementList, factory, factory.getHistory().get());
                } else {
                    selectedElement.performTransientCopyToClipboardOperation(factory.getClipboard().get(), elementList, factory);
                }
            } else {
                resultMap.put(ERROR, "No clipboard available!");
            }
        }
        return resultMap;
    }
}
