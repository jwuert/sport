package org.wuerthner.sport.check;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

public class RangeValidation extends AbstractCheck {
	private final int min;
	private final int max;
	
	public RangeValidation(int min, int max, String message) {
		super("RangeValidation", message, "min", "" + min, "max", "" + max);
		this.min = min;
		this.max = max;
	}
	
	public RangeValidation(int min, int max) {
		this(min, max, "Value must be between " + min + " and " + max);
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
