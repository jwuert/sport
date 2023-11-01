package org.wuerthner.sport.api.attributetype;

import java.util.Map;

public interface StaticMapping<TYPE> {
    public Map<String, TYPE> getValueMap();
}
