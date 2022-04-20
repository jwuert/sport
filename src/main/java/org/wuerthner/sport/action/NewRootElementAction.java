package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.core.ModelState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRootElementAction implements Action {
    @Override
    public String getId() {
        return "newRootElement";
    }

    @Override
    public String getToolTip() {
        return "creates a new document";
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String,String> parameterMap) {
        Map<String,Object> result = new HashMap<>();
        if (factory != null) {
            ModelElement newRootElement = factory.createElement(factory.getRootElementType());
            result.put(ROOT, newRootElement);
        }
        return result;
    }
}
