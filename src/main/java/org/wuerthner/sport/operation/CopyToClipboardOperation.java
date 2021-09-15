package org.wuerthner.sport.operation;

import org.wuerthner.sport.api.Clipboard;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CopyToClipboardOperation implements Operation {
	private final Clipboard clipboard;
	private final List<ModelElement> oldClipboardElements;
	private final List<ModelElement> newClipboardElements;
	private final ModelElement parent;
	private final String info;

	public CopyToClipboardOperation(Clipboard clipboard, List<? extends ModelElement> selection, ModelElementFactory factory) {
		this.clipboard = clipboard;
		this.oldClipboardElements = clipboard.getElements();
		this.newClipboardElements = new ArrayList<>();
		for (ModelElement element : selection) {
			final ModelElement copy = factory.copyTree(element);
			((AbstractModelElement)copy).setDeletedDeep(true);
			newClipboardElements.add(copy);
		}
		if (!selection.isEmpty()) {
			parent = selection.get(0).getParent();
			info = "Copy " + selection.size() + " elements to clipboard";
		} else {
			parent = null;
			info = "Copy empty list";
		}
	}
	
	@Override
	public boolean undoable() {
		return newClipboardElements.size() > 0;
	}
	
	@Override
	public void execute() {
		if (newClipboardElements.size() > 0) {
			clipboard.setElements(newClipboardElements);
		}
	}
	
	@Override
	public void undo() {
		if (newClipboardElements.size() > 0) {
			clipboard.setElements(oldClipboardElements);
		}
	}
	
	@Override
	public String info() {
		return info;
	}
}
