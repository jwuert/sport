package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.core.ModelState;

import java.util.*;

public class DeleteAction implements Action {
    public final static String PARAMETER_DELETE = "delete";

    @Override
    public String getId() {
        return "delete";
    }

    @Override
    public String getToolTip() {
        return "Delete selection";
    }

    @Override
    public boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) {
        return (selectedElement != null);
    }

    @Override
    public List<Attribute<?>> getParameterList(ModelElement selectedElement) {
        List<Attribute<?>> parameterList = new ArrayList<>();
        if (selectedElement != null && selectedElement.getParent()!=selectedElement) {
            Attribute<?> attribute = new AttributeBuilder(PARAMETER_DELETE)
                    .label("Delete element(s): " + selectedElement.getId())
                    .buildMessage();
            parameterList.add(attribute);
        }
        return parameterList;
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> resultMap = new HashMap<>();
        if (factory!= null) {
            if (modelState.hasSelectedElement()) {
                ModelElement selectedElement = modelState.getSelectedElement();
                if (selectedElement.getParent() != selectedElement) {
                    ModelElement parent = selectedElement.getParent();
                    if (factory.getHistory().isPresent()) {
                        selectedElement.performRemoveChildOperation(selectedElement, factory.getHistory().get());
                    } else {
                        selectedElement.performTransientRemoveChildOperation(selectedElement);
                    }
                    resultMap.put(SELECTION, parent);
                } else {
                    resultMap.put(ERROR, "Cannot delete root element!");
                }
            } else {
                resultMap.put(ERROR, "No element selected!");
            }
        }
        return resultMap;
    }
}
