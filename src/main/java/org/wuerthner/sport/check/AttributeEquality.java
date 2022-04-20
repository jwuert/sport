package org.wuerthner.sport.check;

import java.util.Objects;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

public class AttributeEquality<TYPE> extends AbstractCheck {
	private final Attribute<TYPE> attribute;
	private final TYPE value;
	
	public AttributeEquality(Attribute<TYPE> attribute, TYPE value, String message) {
		super("AttributeEquality", message, "attribute", attribute.getName(), "value", attribute.getStringPresentation(value));
		this.attribute = attribute;
		this.value = value;
	}
	
	public AttributeEquality(Attribute<TYPE> attribute, TYPE value) {
		this(attribute, value, "Value must be '" + value + "'");
	}
	
	@Override
	public boolean evaluate(ModelElement element, Attribute<?> a) {
		TYPE attributeValue = element.getAttributeValue(attribute);
		return Objects.equals(attributeValue, value);
	}
	
	public String getScript() {
		return "function getEquality(check, element, key) {" +
				"	var value = check.value;" +
				"	var attributeValue = element.attributes[check.attribute];" +
				"	return (attributeValue==value);" +
				"}";
	}
}
