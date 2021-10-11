package org.wuerthner.sport.operation;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.AttributeOperation;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.core.AbstractModelElement;

public class SetAttributeValueOperation<T> implements Operation, AttributeOperation<T> {
	private final ModelElement element;
	private final T oldValue;
	private final T newValue;
	private final Attribute<T> attribute;
	
	public SetAttributeValueOperation(ModelElement element, Attribute<T> attribute, T value) {
		this.element = element;
		this.attribute = attribute;
		this.oldValue = element.getAttributeValue(attribute);
		this.newValue = value;
	}
	
	@Override
	public boolean undoable() {
		return true;
	}
	
	@Override
	public void execute() {
		((AbstractModelElement) element).setAttributeValue(attribute, newValue);
		element.getParent().sort(attribute);
	}
	
	@Override
	public void undo() {
		((AbstractModelElement) element).setAttributeValue(attribute, oldValue);
		element.getParent().sort(attribute);
	}
	
	@Override
	public String info() {
		return "SetAttribute " + element.getParent().getId() + "." + attribute.getName() + ": " + oldValue + " -> " + newValue;
	}
	
	@Override
	public ModelElement getParent() {
		return element;
	}
	
	@Override
	public Attribute<T> getAttribute() {
		return attribute;
	}
	
	@Override
	public T getOldValue() {
		return oldValue;
	}
	
	@Override
	public T getNewValue() {
		return newValue;
	}
}
