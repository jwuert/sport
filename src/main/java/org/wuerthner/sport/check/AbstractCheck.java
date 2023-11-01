package org.wuerthner.sport.check;

import java.util.HashMap;
import java.util.Map;

import org.wuerthner.sport.api.Check;

public abstract class AbstractCheck<TYPE> implements Check {
	private final Map<String, Object> propertyMap = new HashMap<>();
	
	public AbstractCheck(String name) {
		propertyMap.put("name", name);
//		if (strings.length % 2 != 0) {
//			throw new RuntimeException("AbstractCheck: Wrong number of parameters!");
//		}
//		for (int i = 0; i < strings.length; i = i + 2) {
//			propertyMap.put(strings[i], strings[i + 1]);
//		}
	}

	public TYPE message(String message) {
		propertyMap.put("message", message);
		return (TYPE) this;
	}

	public TYPE addProperty(String key, Object value) {
		propertyMap.put(key, value);
		return (TYPE) this;
	}

	@Override
	public Map<String, Object> getProperties() {
		return propertyMap;
	}
	
	@Override
	public String getMessage() {
		return String.valueOf(propertyMap.get("message"));
	}

}
