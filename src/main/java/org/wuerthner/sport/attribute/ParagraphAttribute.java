package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Textarea;

public class ParagraphAttribute extends AbstractAttribute<String, ParagraphAttribute, Textarea> implements Textarea {

	public ParagraphAttribute(String name) {
		super(name, String.class, Textarea.class);
	}

	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}
}
