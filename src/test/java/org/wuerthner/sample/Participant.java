package org.wuerthner.sample;

import java.util.Arrays;

import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class Participant extends AbstractModelElement {
	public final static String TYPE = "Participant";
	
	public final static IdAttribute ID = new IdAttribute();
	public final static StringAttribute NAME = new AttributeBuilder("name")
			.label("Name")
			.required()
			.buildStringAttribute();

	public Participant() {
		super(TYPE, Arrays.asList(), Arrays.asList(ID, NAME));
	}

	public String getId() {
		return this.getAttributeValue(ID);
	}
}
