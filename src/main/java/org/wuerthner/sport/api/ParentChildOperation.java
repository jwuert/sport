package org.wuerthner.sport.api;

public interface ParentChildOperation extends Operation {
	public ModelElement getChild();
	
	public ModelElement getParent();
}
