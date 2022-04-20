package org.wuerthner.sport.api;

import java.util.Map;

public interface DynamicMapping {
    public Map<String, String> getValueMap();

    String getElementFilterType();

    Check getElementFilterCheck();
}
