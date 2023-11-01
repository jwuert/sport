package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

import java.util.Optional;

public class Completeness extends AbstractCheck {
    private final Optional<Attribute<?>> attribute;

    public Completeness(Attribute<?> attribute) {
        super("Completeness");
        addProperty("attribute", attribute.getName());
        message("Attribute '" + attribute.getName() + "' must be completed");
        this.attribute = Optional.of(attribute);
    }

    public Completeness() {
        super("Completeness");
        message("Must be completed");
        this.attribute = Optional.empty();
    }

    @Override
    public boolean evaluate(ModelElement element, Attribute<?> a) {
        Object attributeValue;
        if (attribute.isPresent()) {
            attributeValue = element.getAttributeValue(attribute.get());
        } else {
            attributeValue = element.getAttributeValue(a);
        }
        return attributeValue != null && attributeValue != Boolean.FALSE;
    }
}
