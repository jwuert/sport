package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Text;

public class LongAttribute extends AbstractAttribute<Long,LongAttribute,Text> implements Text {

	public LongAttribute(String name) {
		super(name, Long.class, Text.class);
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
