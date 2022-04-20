package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.Mapping;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MappedSelectableStringAttribute extends AbstractAttribute<String> implements Mapping<String> {
	private Map<String, String> selectableValueMap = new LinkedHashMap<>();
	
	public MappedSelectableStringAttribute(String name, String label, String defaultValue, Map<String, String> values, boolean readonly, boolean required, boolean hidden, String description,
										   List<Check> dependencies, List<Check> validators) {
		super(name, label, String.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
		selectableValueMap.putAll(values);
	}
	
	public Map<String, String> getValueMap() {
		return selectableValueMap;
	}
	
	public String getStringKey(String stringValue) {
		String key = "";
		for (Map.Entry<String, String> entry : selectableValueMap.entrySet()) {
			if (entry.getValue().equals(stringValue)) {
				key = entry.getKey();
				break;
			}
		}
		return key;
	}
	
	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}
}
