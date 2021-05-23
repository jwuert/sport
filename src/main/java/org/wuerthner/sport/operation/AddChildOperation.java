package org.wuerthner.sport.operation;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.api.ParentChildOperation;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.Collections;

public class AddChildOperation implements Operation, ParentChildOperation {
	private final ModelElement newParent;
	private final ModelElement child;
	private final ModelElement oldParent;
	
	public AddChildOperation(ModelElement parent, ModelElement child) {
		this.newParent = parent;
		this.oldParent = (ModelElement) child.getParent();
		this.child = child;
	}
	
	@Override
	public ModelElement getChild() {
		return child;
	}
	
	@Override
	public ModelElement getParent() {
		return newParent;
	}
	
	@Override
	public boolean undoable() {
		return true;
	}
	
	@Override
	public void execute() {
		if (oldParent != child) {
			((AbstractModelElement) oldParent).removeChild(child);
			child.setDeleted(true);
			oldParent.sort();
		}
		if (newParent != child) {
			((AbstractModelElement) newParent).addChild(child);
			child.setDeleted(false);
			newParent.sort();
		}
	}
	
	@Override
	public void undo() {
		if (newParent != child) {
			((AbstractModelElement) newParent).removeChild(child);
			child.setDeleted(true);
			newParent.sort();
		}
		if (oldParent != child) {
			((AbstractModelElement) oldParent).addChild(child);
			child.setDeleted(false);
			oldParent.sort();
		}
	}
	
	@Override
	public String info() {
		return "Add element " + child.getId() + " to " + newParent.getId();
	}
}
