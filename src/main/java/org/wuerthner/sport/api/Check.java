package org.wuerthner.sport.api;

import java.util.Map;

public interface Check {
    public boolean evaluate(ModelElement element, Attribute<?> attribute);

    public Map<String, Object> getProperties();

    public String getMessage();

    default String getTrueIf() {
        return "";
    }
}
