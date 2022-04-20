package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.ModelState;

import java.util.HashMap;
import java.util.Map;

public class AboutAction implements Action {
    private final static Map<String,Object> ABOUT_MAP = new HashMap<>();
    static {
        ABOUT_MAP.put(Action.DISPLAY, "About Message");
    }

    @Override
    public String getId() {
        return "about";
    }

    @Override
    public String getToolTip() {
        return "displays information about the application";
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        return ABOUT_MAP;
    }
}
