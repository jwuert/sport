package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Text;

public class ClassAttribute extends AbstractAttribute<Class<?>,ClassAttribute,Text> implements Text {
	
	public ClassAttribute(String name) {
		super(name, (Class<Class<?>>) (Class<?>) Class.class, Text.class);
	}

	@Override
	public Class<?> getValue(String stringValue) {
		if (stringValue == null || stringValue.trim().equals("")) {
			return null;
		} else {
			try {
				if (stringValue.startsWith("class ")) stringValue = stringValue.substring(6);
				return Class.forName(stringValue);
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
