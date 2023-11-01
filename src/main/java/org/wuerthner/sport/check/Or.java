package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.ModelElement;

public class Or extends AbstractCheck<Or> {
    private final Check innerCheck1;
    private final Check innerCheck2;

    public Or(Check innerCheck1, Check innerCheck2) {
        super("Or");
        this.innerCheck1 = innerCheck1;
        this.innerCheck2 = innerCheck2;
        addProperty("innerCheck1", innerCheck1.getProperties());
        addProperty("innerCheck2", innerCheck2.getProperties());
        message("(" + innerCheck1.getMessage() + " OR " + innerCheck2.getMessage() + ")");
    }

    @Override
    public boolean evaluate(ModelElement element, Attribute<?> attribute) {
        return innerCheck1.evaluate(element, attribute) || innerCheck2.evaluate(element, attribute);
    }
}
