package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.ModelState;
import org.wuerthner.sport.core.NoElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClearAction implements Action {
    @Override
    public String getId() {
        return "clear";
    }

    @Override
    public String getToolTip() {
        return "Clear selection";
    }

    @Override
    public boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) {
        return (selectedElement != null);
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put(SELECTION, NoElement.INSTANCE);
        return resultMap;
    }
}
