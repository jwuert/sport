package org.wuerthner.sport.core;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.attribute.IdAttribute;

import java.util.*;

public class Validation {
    public Validation() {}

    public ValidationResult validate(ModelElement element) {
        ValidationResult validationResult = validateSingle(element);
        Map<String,List<String>> idMap = new HashMap<>();
        for (ModelElement child : element.getChildren()) {
            validationResult.addEntries(validate(child));
            if (idMap.get(child.getType()) != null && idMap.get(child.getType()).contains(child.getId())) {
                validationResult.addError(child, child.getAttribute(IdAttribute.ID_NAME),"Ambiguous ID");
            }
            idMap.computeIfAbsent(child.getType(), k -> new ArrayList<>()).add(child.getId());
        }
        return validationResult;
    }

    public ValidationResult validateSingle(ModelElement element) {
        ValidationResult validationResult = new ValidationResult();
        for (Attribute<?> attribute : element.getAttributes()) {
            Optional<Check> failedDep = attribute.getDependencies().stream().filter(check -> !check.evaluate(element, attribute)).findAny();
            boolean dependenciesFulfilled = !failedDep.isPresent();
            if (dependenciesFulfilled) {
                if (attribute.isRequired() && element.getAttributeValue(attribute) == null) {
                    validationResult.addError(element, attribute, "required");
                }
                List<Check> validators = attribute.getValidators();
                for (Check check : validators) {
                    Boolean result = check.evaluate(element, attribute);
                    if (!result) {
                        validationResult.addError(element, attribute, check.getMessage());
                    }
                }
            }
        }
        return validationResult;
    }
}
