package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AttributeContains<TYPE> extends AbstractCheck {
	private final Attribute<List<TYPE>> listAttribute;
	private final ModelElement thatElement;
	private final Attribute<TYPE> thatAttribute;

	public AttributeContains(Attribute<List<TYPE>> listAttribute, ModelElement thatElement, Attribute<TYPE> thatAttribute) {
		super("AttributeContains");
		this.listAttribute = listAttribute;
		this.thatElement = thatElement;
		this.thatAttribute = thatAttribute;
		addProperty("listAttribute", listAttribute.getName());
		addProperty("thatAttribute", thatAttribute.getName());
		addProperty("thatElement", thatElement);
		message("Value must be '" + thatAttribute.getName() + "'");
	}

	@Override
	public boolean evaluate(ModelElement element, Attribute<?> a) {
		List<TYPE> listAttributeValue = element.getAttributeValue(listAttribute);
		TYPE thatAttributeValue = thatElement.getAttributeValue(thatAttribute);
		if (listAttributeValue != null) {
			return listAttributeValue.contains(thatAttributeValue);
		} else {
			return false;
		}
	}

//	@Override
//	public Map<String,Object> getProperties() {
//		Map<String,Object> prop = super.getProperties();
//		prop.put("thatElement", thatElement);
//		return prop;
//	}

	public String getScript() {
		// TODO!!!
		return "";
//		return "function getEquality(check, element, key) {" +
//				"	var value = check.value;" +
//				"	var attributeValue = element.attributes[check.attribute];" +
//				"	return (attributeValue==value);" +
//				"}";
	}
}
