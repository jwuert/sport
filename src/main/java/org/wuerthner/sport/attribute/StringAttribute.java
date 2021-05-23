package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.List;

public class StringAttribute extends AbstractAttribute<String> {
	public StringAttribute(String name, String label, String defaultValue, boolean readonly, boolean required, boolean hidden, String description, List<Check> dependencies,
			List<Check> validators) {
		super(name, label, String.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
	}
	
	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}
}
