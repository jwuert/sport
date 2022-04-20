package org.wuerthner.sport.check;

import java.util.HashMap;
import java.util.Map;

import org.wuerthner.sport.api.Check;

public abstract class AbstractCheck implements Check {
	private final Map<String, String> propertyMap = new HashMap<>();
	
	public AbstractCheck(String name, String message, String... strings) {
		propertyMap.put("name", name);
		propertyMap.put("message", message);
		if (strings.length % 2 != 0) {
			throw new RuntimeException("AbstractCheck: Wrong number of parameters!");
		}
		for (int i = 0; i < strings.length; i = i + 2) {
			propertyMap.put(strings[i], strings[i + 1]);
		}
	}
	
	@Override
	public Map<String, String> getProperties() {
		return propertyMap;
	}
	
	@Override
	public String getMessage() {
		return propertyMap.get("message");
	}
}
