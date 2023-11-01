package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.ModelElement;

public class And extends AbstractCheck<And> {
    private final Check innerCheck1;
    private final Check innerCheck2;

    public And(Check innerCheck1, Check innerCheck2) {
        super("And");
        this.innerCheck1 = innerCheck1;
        this.innerCheck2 = innerCheck2;
        addProperty("innerCheck1", innerCheck1.getProperties());
        addProperty("innerCheck2", innerCheck2.getProperties());
        message("(" + innerCheck1.getMessage() + " AND " + innerCheck2.getMessage());
    }

    @Override
    public boolean evaluate(ModelElement element, Attribute<?> attribute) {
        return innerCheck1.evaluate(element, attribute) && innerCheck2.evaluate(element, attribute);
    }
}
