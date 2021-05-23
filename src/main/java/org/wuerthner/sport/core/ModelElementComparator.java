package org.wuerthner.sport.core;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

import java.util.Comparator;
import java.util.Optional;

public class ModelElementComparator<TYPE> implements Comparator<ModelElement> {
    public final static ModelElementComparator<?> DEFAULT = new ModelElementComparator<>();
    private final Optional<Attribute<TYPE>> attributeOptional;

    public ModelElementComparator() {
        attributeOptional = Optional.empty();
    }

    public ModelElementComparator(Attribute<TYPE> attribute) {
        attributeOptional = Optional.of(attribute);
        Class<? extends TYPE> type = attribute.getAttributeType();
        if (!type.isAssignableFrom(Comparable.class)) {
            throw new RuntimeException("Attribute " + attribute.getName() + " must be of type 'Comparable'!");
        }
    }

    public boolean hasAttribute() {
        return attributeOptional.isPresent();
    }

    public String getAttributeName() {
        return attributeOptional.isPresent() ? attributeOptional.get().getName() : "?";
    }

    @Override
    public int compare(ModelElement element1, ModelElement element2) {
        int result ;
        if (attributeOptional.isPresent()) {
            Attribute<TYPE> attribute = attributeOptional.get();
            result = ((Comparable)element1.getAttributeValue(attribute)).compareTo(element2.getAttributeValue(attribute));

        } else {
            result = element1.getId().compareTo(element2.getId());
            // result = String.compare(element1.getId(), element2.getId());
        }
        return result;
    }
}
