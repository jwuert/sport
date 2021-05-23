package org.wuerthner.sample;

import java.util.Arrays;

import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.attribute.IdAttribute;
import org.wuerthner.sport.attribute.IntegerAttribute;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class Course extends AbstractModelElement {
	public final static String TYPE = "Course";
	
	public final static IdAttribute ID = new IdAttribute();
	public final static StringAttribute TITLE = new AttributeBuilder("title")
			.label("Title")
			.required()
			.buildStringAttribute();
	public final static IntegerAttribute SCORE = new AttributeBuilder("score")
			.label("Score")
			.buildIntegerAttribute();
	
	public Course() {
		super(TYPE, Arrays.asList(Participant.TYPE), Arrays.asList(ID, TITLE, SCORE));
	}

	public String getId() {
		return this.getAttributeValue(ID);
	}
}
