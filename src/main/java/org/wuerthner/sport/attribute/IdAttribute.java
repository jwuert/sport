package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.ArrayList;
import java.util.List;

public class IdAttribute extends StringAttribute {
	public final static String ID_PATTERN = "\\p{Alpha}\\w*";
	public final static String ID_NAME = "id";
	public final static String ID_LABEL = "ID";
	public final static boolean READONLY = false;
	public final static boolean REQUIRED = true;
	public final static boolean HIDDEN = false;
	public final static String DESCR = "The ID of the element";
	
	public IdAttribute() {
		super(ID_NAME, ID_LABEL, "Untitled", READONLY, REQUIRED, HIDDEN, DESCR, new ArrayList<>(), new ArrayList<>());
	}
	
	public IdAttribute(String name, String label, String defaultValue, boolean readonly, boolean required, boolean hidden, String description, List<Check> dependencies,
			List<Check> validators) {
		super(name, label, defaultValue, readonly, required, hidden, description, dependencies, validators);
	}
	
	@Override
	public String getValue(String stringValue) {
		if (stringValue != null && !stringValue.matches("^" + ID_PATTERN + "$")) {
			throw new RuntimeException("Invalid ID '" + stringValue + "'!");
		}
		return stringValue;
	}
}
