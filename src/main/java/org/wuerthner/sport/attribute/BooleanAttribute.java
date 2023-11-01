package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Alternative;

public class BooleanAttribute extends AbstractAttribute<Boolean,BooleanAttribute,Alternative> implements Alternative {

	public BooleanAttribute(String name) {
		super(name, Boolean.class, Alternative.class);
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
