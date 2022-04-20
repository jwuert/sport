package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.core.ModelState;
import org.wuerthner.sport.core.Validation;
import org.wuerthner.sport.core.ValidationResult;

import java.util.HashMap;
import java.util.Map;

public class ValidateAction implements Action {
    @Override
    public String getId() {
        return "validate";
    }

    @Override
    public String getToolTip() {
        return "Validate the data model";
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        Map<String,Object> resultMap = new HashMap<>();
        if (modelState.hasRootElement()) {
            Validation validation = new Validation();
            ValidationResult validationResult = validation.validate(modelState.getRootElement());
            validationResult.print();
            resultMap.put(VALIDATION, validationResult.asMap());
        } else {
            resultMap.put(ERROR, "Nothing to validate");
        }
        return resultMap;
    }
}
