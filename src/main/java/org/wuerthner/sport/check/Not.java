package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.ModelElement;

public class Not extends AbstractCheck<Not> {
    private final Check innerCheck;

    public Not(Check innerCheck) {
        super("Not");
        this.innerCheck = innerCheck;
        addProperty("innerCheck", innerCheck.getProperties());
        message("(NOT " + innerCheck.getMessage() + ")");
    }

    @Override
    public boolean evaluate(ModelElement element, Attribute<?> attribute) {
        return !innerCheck.evaluate(element, attribute);
    }
}
