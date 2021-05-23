package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.List;

public class LongAttribute extends AbstractAttribute<Long> {

	public LongAttribute(String name, String label, Long defaultValue, boolean readonly, boolean required, boolean hidden, String description, List<Check> dependencies,
                         List<Check> validators) {
		super(name, label, Long.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
	}
	
	@Override
	public Long getValue(String stringValue) {
		if (stringValue == null || stringValue.trim().equals("")) {
			return null;
		} else {
			if (stringValue.matches("^\\d+$")) {
				return Long.valueOf(stringValue);
			} else {
				return null;
			}
		}
	}
}
