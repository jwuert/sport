package org.wuerthner.sample;

import java.util.Arrays;

import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.attribute.BooleanAttribute;
import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.attribute.IntegerAttribute;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class School extends AbstractModelElement {
	public final static String TYPE = "School";

	public final static IdAttribute ID = new IdAttribute();
	public final static StringAttribute NAME = new AttributeBuilder("name")
			.label("Name")
			.required()
			.buildStringAttribute();

	public final static BooleanAttribute ACTIVE = new AttributeBuilder("active")
			.label("Active")
			.buildBooleanAttribute();

	public final static IntegerAttribute REP = new AttributeBuilder("reputation")
			.label("Reputation")
			.required()
			.buildIntegerAttribute();

	public School() {
		super(TYPE, Arrays.asList(Course.TYPE), Arrays.asList(ID, NAME, ACTIVE, REP));
	}

	public String getId() {
		return this.getAttributeValue(ID);
	}
}
