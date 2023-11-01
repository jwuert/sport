package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.Variable;
import org.wuerthner.sport.api.attributetype.Display;

import java.util.ArrayList;
import java.util.List;

public class MessageAttribute extends AbstractAttribute<String,MessageAttribute,Display> implements Display {

	private List<Variable<?>> variableList = new ArrayList<>();
	private boolean twoColumns = false;

	public MessageAttribute(String name) {
		super(name, String.class, Display.class);
	}

	public MessageAttribute twoColumns() {
		twoColumns = true;
		return this;
	}

	public boolean hasTwoColumns() {
		return twoColumns;
	}

	@Override
	public String getValue(String stringValue) { return stringValue; }

	@Override
	public String getValue(String stringValue, ModelElement element) {
		if (stringValue==null)  return "";
		for (int i=0; i< variableList.size(); i++) {
			Variable<?> variable = variableList.get(i);
			Object value = variable.evaluate(element);
			stringValue = stringValue.replaceFirst("\\$", value == null ? "-" : String.valueOf(value));
		}
		return stringValue;
	}

	public MessageAttribute addVariable(Variable variable) {
		variableList.add(variable);
		return this;
	}

	public List<Variable<?>> getVariableList() {
		return variableList;
	}
}
