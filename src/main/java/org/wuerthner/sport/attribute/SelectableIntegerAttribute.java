package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.Mapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectableIntegerAttribute extends IntegerAttribute implements Mapping<Integer> {
	private Map<String, Integer> selectableValueMap = new LinkedHashMap<>();

	public SelectableIntegerAttribute(String name, String label, Integer defaultValue, Map<String, Integer> values, boolean readonly, boolean required, boolean hidden, String description,
										   List<Check> dependencies, List<Check> validators) {
		super(name, label, defaultValue, readonly, required, hidden, description, dependencies, validators);
		selectableValueMap.putAll(values);
	}

	@Override
	public Map<String, Integer> getValueMap() {
		return selectableValueMap;
	}

	public String getStringKey(String stringValue) {
		String key = "";
		for (Map.Entry<String, Integer> entry : selectableValueMap.entrySet()) {
			if (entry.getValue().equals(stringValue)) {
				key = entry.getKey();
				break;
			}
		}
		return key;
	}
}
