package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.List;

public class BooleanAttribute extends AbstractAttribute<Boolean> {
	public BooleanAttribute(String name, String label, Boolean defaultValue, boolean readonly, boolean required, boolean hidden, String description, List<Check> dependencies,
			List<Check> validators) {
		super(name, label, Boolean.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
	}
	
	@Override
	public Boolean getValue(String stringValue) {
		if (stringValue == null || stringValue.trim().equals("")) {
			return null;
		} else {
			return Boolean.valueOf(stringValue);
		}
	}
}
