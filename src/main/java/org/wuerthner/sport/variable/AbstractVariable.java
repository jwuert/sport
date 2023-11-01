package org.wuerthner.sport.variable;

import org.wuerthner.sport.api.Variable;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractVariable<TYPE> implements Variable<TYPE> {
    private final Map<String, Object> propertyMap = new HashMap<>();

    public AbstractVariable(String name) {
        propertyMap.put("name", name);
    }

    public TYPE addProperty(String key, Object value) {
        propertyMap.put(key, value);
        return (TYPE) this;
    }

    @Override
    public Map<String, Object> getProperties() {
        return propertyMap;
    }
}
