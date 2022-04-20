package org.wuerthner.sport.check;

import java.util.Objects;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

public class AttributeAttributeEquality<TYPE> extends AbstractCheck {
	private final Attribute<TYPE> attribute;
	private final ModelElement thatElement;
	private final Attribute<TYPE> thatAttribute;
	
	public AttributeAttributeEquality(Attribute<TYPE> attribute, ModelElement thatElement, Attribute<TYPE> thatAttribute, String message) {
		super("AttributeAttributeEquality", message, "attribute", attribute.getName(), "thatElement", thatElement.getAttributeValue("id"), "thatAttribute", thatAttribute.getName());
		this.attribute = attribute;
		this.thatElement = thatElement;
		this.thatAttribute = thatAttribute;
	}
	
	public AttributeAttributeEquality(Attribute<TYPE> attribute, ModelElement thatElement, Attribute<TYPE> thatAttribute) {
		this(attribute, thatElement, thatAttribute, "Value must be '" + thatAttribute.getName() + "'");
	}
	
	@Override
	public boolean evaluate(ModelElement element, Attribute<?> a) {
		TYPE attributeValue = element.getAttributeValue(attribute);
		TYPE thatAttributeValue = thatElement.getAttributeValue(thatAttribute);
		return Objects.equals(attributeValue, thatAttributeValue);
	}
	
	public String getScript() {
		return "function getAttributeAttributeEquality(check, element, key) {" +
				"   /*" +
				"	 * properties: attribute, thatElement, thatAttribute\n" +
				"	 */" +
				"	var attributeValue = element.attributes[check.attribute];" +
				"	var thatAttributeValue = thatElement.attributes[check.thatAttribute];" +
				"	return (attributeValue==thatAttributeValue);" +
				"}";
	}
}
