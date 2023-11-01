package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.attribute.SelectableStringAttribute;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.Model;
import org.wuerthner.sport.core.ModelState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewElementAction implements Action {
    public final static String PARAMETER_TYPE = "type";
    public final static String PARAMETER_NAME = "name";

    @Override
    public String getId() {
        return "newElement";
    }

    @Override
    public String getToolTip() {
        return "Create a new element";
    }

    @Override
    public boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) {
        return (selectedElement != null && !selectedElement.getChildTypes().isEmpty());
    }

    @Override
    public List<Attribute<?>> getParameterList(ModelElement selectedElement) {
        List<Attribute<?>> parameterList = new ArrayList<>();
        if (selectedElement != null) {
            Attribute<?> name = new StringAttribute(PARAMETER_NAME)
                    .label("Name")
                    .defaultValue("Unnamed");
            parameterList.add(name);
            Attribute<?> type = new SelectableStringAttribute(PARAMETER_TYPE)
                    .label("Type")
                    .values(selectedElement.getChildTypes().toArray(new String[]{}));
            parameterList.add(type);
        }
        return parameterList;
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String,String> parameterMap) {
        Map<String,Object> resultMap = new HashMap<>();
        if (modelState.hasSelectedElement()) {
            ModelElement selectedElement = modelState.getSelectedElement();
            if (factory != null && selectedElement != null && parameterMap != null && parameterMap.get(PARAMETER_TYPE) != null) {
                String name = parameterMap.get(PARAMETER_NAME);
                String uniqueId = Model.makeUniqueId(selectedElement, name);
                String type = parameterMap.get(PARAMETER_TYPE);
                ModelElement newElement = factory.createElement(type);
                newElement.performTransientSetAttributeValueOperation((IdAttribute) newElement.getAttribute(IdAttribute.ID_NAME), uniqueId);
                if (factory.getHistory().isPresent()) {
                    selectedElement.performAddChildOperation(newElement, factory.getHistory().get());
                } else {
                    selectedElement.performTransientAddChildOperation(newElement);
                }
                resultMap.put(SELECTION, newElement);
            }
        } else {
            resultMap.put(ERROR, "No element selected!");
        }
        return resultMap;
    }
}
