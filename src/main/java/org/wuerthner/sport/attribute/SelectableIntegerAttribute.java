package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.StaticMapping;

import java.util.LinkedHashMap;
import java.util.Map;

public class SelectableIntegerAttribute extends AbstractAttribute<Integer,SelectableIntegerAttribute,StaticMapping> implements StaticMapping<Integer> {
	private Map<String, Integer> selectableValueMap = new LinkedHashMap<>();

	public SelectableIntegerAttribute(String name) {
		super(name, Integer.class, StaticMapping.class);
	}

	public SelectableIntegerAttribute values(String[] selectableValues) {
		int i=0;
		for (String value : selectableValues) {
			this.selectableValueMap.put(value, i++);
		}
		return this;
	}

	public SelectableIntegerAttribute addValue(String key, int value) {
		this.selectableValueMap.put(key, value);
		return this;
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

	@Override
	public Integer getValue(String stringValue) {
		if (stringValue == null || stringValue.trim().equals("")) {
			return null;
		} else {
			if (stringValue.matches("^\\-?\\d+$")) {
				return Integer.valueOf(stringValue);
			} else {
				return null;
			}
		}
	}
}
