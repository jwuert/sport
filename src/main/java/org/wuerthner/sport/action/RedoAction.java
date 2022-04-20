package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.ModelState;

import java.util.HashMap;
import java.util.Map;

public class RedoAction implements Action {
    @Override
    public String getId() {
        return "redo";
    }

    @Override
    public String getToolTip() {
        return "Redo";
    }

    @Override
    public boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) {
        return (factory.getHistory().isPresent() && factory.getHistory().get().hasRedo());
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> resultMap = new HashMap<>();
        if (factory!=null) {
            if (factory.getHistory().isPresent()) {
                factory.getHistory().get().redo();
            }
        } else {
            resultMap.put(ERROR, "No factory given!");
        }
        return resultMap;
    }
}
