package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.StaticMapping;

import java.util.LinkedHashMap;
import java.util.Map;

public class SelectableStringAttribute extends AbstractAttribute<String, SelectableStringAttribute,StaticMapping> implements StaticMapping<String> {
	private Map<String, String> selectableValueMap = new LinkedHashMap<>();

	public SelectableStringAttribute(String name) {
		super(name, String.class, StaticMapping.class);
	}

	public SelectableStringAttribute values(String[] selectableValues) {
		for (String value : selectableValues) {
			this.selectableValueMap.put(value, value);
		}
		return this;
	}

	public SelectableStringAttribute values(Map<String, String> selectableValues) {
		this.selectableValueMap.putAll(selectableValues);
		return this;
	}

	public SelectableStringAttribute addValue(String key, String value) {
		this.selectableValueMap.put(key, value);
		return this;
	}

	@Override
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
