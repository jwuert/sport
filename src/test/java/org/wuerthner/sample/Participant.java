package org.wuerthner.sample;

import java.util.Arrays;

import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class Participant extends AbstractModelElement {
	public final static String TYPE = "Participant";
	
	public final static IdAttribute ID = new IdAttribute();
	public final static StringAttribute NAME = new StringAttribute("name")
			.label("Name")
			.required();

	public Participant() {
		super(TYPE, Arrays.asList(), Arrays.asList(ID, NAME));
	}

	public String getId() {
		return this.getAttributeValue(ID);
	}
}
