package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Text;

public class IntegerAttribute extends AbstractAttribute<Integer,IntegerAttribute,Text> implements Text {

	public IntegerAttribute(String name) {
		super(name, Integer.class, Text.class);
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
