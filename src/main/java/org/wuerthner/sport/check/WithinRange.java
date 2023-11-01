package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

public class WithinRange extends AbstractCheck {
	private final int min;
	private final int max;
	
	public WithinRange(int min, int max) {
		super("RangeValidation");
		this.min = min;
		this.max = max;
		addProperty("min", min);
		addProperty("max", max);
		message("Value must be between " + min + " and " + max);
	}
	
	@Override
	public boolean evaluate(ModelElement element, Attribute<?> attribute) {
		Object value = element.getAttributeValue(attribute);
		if (value != null) {
			int attributeValue = (Integer) value;
			return attributeValue <= max && attributeValue >= min;
		} else {
			return true;
		}
	}
}
