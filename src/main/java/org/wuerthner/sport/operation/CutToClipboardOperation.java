package org.wuerthner.sport.operation;

import org.graalvm.compiler.nodes.calc.IntegerDivRemNode;
import org.wuerthner.sport.api.Clipboard;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.api.ParentChildOperation;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CutToClipboardOperation implements Operation {
	private final Clipboard clipboard;
	private final List<ModelElement> oldClipboardElements;
	private final List<ModelElement> newClipboardElements;
	private final Optional<Transaction> transaction;
	private final ModelElement parent;
	private final String info;

	public CutToClipboardOperation(Clipboard clipboard, List<? extends ModelElement> selection) {
		this.clipboard = clipboard;
		this.oldClipboardElements = clipboard.getElements();
		this.newClipboardElements = new ArrayList<>();
		List<Operation> opList = new ArrayList<>();
		for (ModelElement element : selection) {
			element.setReference(element.getParent());
			newClipboardElements.add(element);
			RemoveChildOperation op = new RemoveChildOperation(element);
			opList.add(op);
		}
		if (!opList.isEmpty()) {
			parent = selection.get(0).getParent();
			info = "Cut " + opList.size() + " elements to clipboard";
			transaction = Optional.of(new Transaction(true, info, opList));
		} else {
			transaction = Optional.empty();
			parent = null;
			info = "Cut empty list";
		}
	}
	
	@Override
	public boolean undoable() {
		return transaction.isPresent();
	}
	
	@Override
	public void execute() {
		if (transaction.isPresent()) {
			clipboard.setElements(newClipboardElements);
			transaction.get().execute();
			parent.sort();
		}
	}
	
	@Override
	public void undo() {
		if (transaction.isPresent()) {
			clipboard.setElements(oldClipboardElements);
			transaction.get().undo();
			parent.sort();
		}
	}
	
	@Override
	public String info() {
		return info;
	}
}
