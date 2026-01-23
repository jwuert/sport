package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

import java.util.Optional;

public class IdAvailableCheck extends AbstractCheck {
    private final Optional<String> type;

    public IdAvailableCheck(String type) {
        super("IdAvailableCheck");
        addProperty("type", type);
        message("Must be a valid '" + type + "'");
        this.type = Optional.of(type);
    }

    public IdAvailableCheck() {
        super("IdAvailableCheck");
        message("Must be a valid reference");
        this.type = Optional.empty();
    }

    @Override
    public boolean evaluate(ModelElement element, Attribute<?> a) {
        String attributeValue = String.valueOf(element.getAttributeValue(a));
        boolean result = false;
        if (type.isPresent()) {
            result = element.getRoot().lookupByTypeAndId(type.get(), attributeValue).isPresent();
        } else {
            result = lookupById(element.getRoot(), attributeValue).isPresent();
        }
        return result;
    }

    private Optional<ModelElement> lookupById(ModelElement element, String id) {
        Optional<ModelElement> optional = Optional.empty();
        if (element.getId().equals(id)) {
            optional = Optional.of(element);
        } else {
            for (ModelElement child : element.getChildren()) {
                optional = lookupById(child, id);
                if (optional.isPresent()) {
                    break;
                }
            }
        }
        return optional;
    }
}
