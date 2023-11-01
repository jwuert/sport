package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Text;

public class StringAttribute extends AbstractAttribute<String,StringAttribute,Text> implements Text {

	public StringAttribute(String name) {
		super(name, String.class, Text.class);
	}

	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}
}
