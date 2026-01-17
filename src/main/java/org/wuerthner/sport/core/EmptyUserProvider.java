package org.wuerthner.sport.core;

import org.wuerthner.sport.api.UserProvider;

import java.util.HashMap;
import java.util.Map;

public class EmptyUserProvider implements UserProvider {
    @Override
    public Map<String, String> getUserMap() {
        return new HashMap<>();
    }
}
