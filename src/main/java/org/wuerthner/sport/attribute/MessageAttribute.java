package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.Arrays;
import java.util.List;

public class MessageAttribute extends AbstractAttribute<String> {
	public MessageAttribute(String name, String label) {
		super(name, label, String.class, null, false, false, false, "message", Arrays.asList(), Arrays.asList());
	}
	
	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}
}
