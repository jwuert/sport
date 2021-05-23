package org.wuerthner.sport.api;

import java.util.Map;

public interface Check {
    public boolean evaluate(ModelElement element, Attribute<?> attribute);

    public Map<String, String> getProperties();

    public String getMessage();
}
