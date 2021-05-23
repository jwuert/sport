package org.wuerthner.sport.operation;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.api.ParentChildOperation;
import org.wuerthner.sport.core.AbstractModelElement;

public class RemoveChildOperation implements Operation, ParentChildOperation {
	private final ModelElement child;
	private final ModelElement parent;
	
	public RemoveChildOperation(ModelElement child) {
		this.child = child;
		this.parent = (ModelElement) child.getParent();
	}
	
	@Override
	public boolean undoable() {
		return true;
	}
	
	@Override
	public void execute() {
		if (child != parent) {
			((AbstractModelElement) parent).removeChild(child);
			parent.sort();
		}
		child.setDeleted(true);
	}
	
	@Override
	public void undo() {
		if (child != parent) {
			((AbstractModelElement) parent).addChild(child);
			parent.sort();
		}
		child.setDeleted(false);
	}
	
	@Override
	public String info() {
		return "Remove " + child.getId() + " from " + parent.getId();
	}
	
	@Override
	public ModelElement getChild() {
		return child;
	}
	
	@Override
	public ModelElement getParent() {
		return parent;
	}
}
