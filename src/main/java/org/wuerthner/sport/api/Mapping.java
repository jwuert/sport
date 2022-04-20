package org.wuerthner.sport.api;

import java.util.Map;

public interface Mapping<TYPE> {
    public Map<String, TYPE> getValueMap();
}
