package org.wuerthner.sport.api;

import java.util.Map;

public interface Variable<TYPE> {
    public TYPE evaluate(ModelElement element);
    public Map<String, Object> getProperties();
}
