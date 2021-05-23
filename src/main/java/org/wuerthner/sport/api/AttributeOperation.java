package org.wuerthner.sport.api;

public interface AttributeOperation<T> extends Operation {
	public ModelElement getParent();
	
	public Attribute<T> getAttribute();
	
	public Object getOldValue();
	
	public Object getNewValue();
}
