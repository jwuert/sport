package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.List;

public class IntegerAttribute extends AbstractAttribute<Integer> {

	public IntegerAttribute(String name, String label, Integer defaultValue, boolean readonly, boolean required, boolean hidden, String description, List<Check> dependencies,
			List<Check> validators) {
		super(name, label, Integer.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
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
