package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.List;

public class ClassAttribute extends AbstractAttribute<Class<?>> {
	
	@SuppressWarnings("unchecked")
	public ClassAttribute(String name, String label, Class<?> defaultValue, boolean readonly, boolean required, boolean hidden, String description, List<Check> dependencies,
			List<Check> validators) {
		super(name, label, (Class<Class<?>>) (Class<?>) (Class<?>) Class.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
	}
	
	@Override
	public Class<?> getValue(String stringValue) {
		if (stringValue == null || stringValue.trim().equals("")) {
			return null;
		} else {
			try {
				return (Class<?>) Class.forName(stringValue);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("Invalid attribute conversion '" + stringValue + "'");
			}
		}
	}
	
	@Override
	public String getStringPresentation(Class<?> value) {
		return value.getName();
	}
}
