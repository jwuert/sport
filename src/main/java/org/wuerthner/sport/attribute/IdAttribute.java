package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.attributetype.Text;

public class IdAttribute extends AbstractAttribute<String,IdAttribute,Text> implements Text {
	public final static String ID_PATTERN = "\\p{Alpha}\\w*";
	public final static String ID_NAME = "id";
	public final static String ID_LABEL = "ID";
	public final static boolean READONLY = false;
	public final static boolean REQUIRED = true;
	public final static boolean HIDDEN = false;
	public final static String DESCRIPTION = "The ID of the element";
	public final static String DEFAULT_VALUE = "Untitled";
	private boolean strictPattern;

	public IdAttribute(boolean strictPattern) {
		super(ID_NAME, String.class, Text.class);
		label(ID_LABEL);
		defaultValue(DEFAULT_VALUE);
		description(DESCRIPTION);
		required();
		strictPattern(strictPattern);
	}

	public IdAttribute() {
		this(true);
	}

	public IdAttribute strictPattern(boolean strictPattern) {
		this.strictPattern = strictPattern;
		return this;
	}
	
	@Override
	public String getValue(String stringValue) {
		if (strictPattern && stringValue != null && !stringValue.matches("^" + ID_PATTERN + "$")) {
			throw new RuntimeException("Invalid ID '" + stringValue + "'!");
		}
		return stringValue;
	}
}
