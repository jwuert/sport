package org.wuerthner.sport.operation;

import org.wuerthner.sport.api.Clipboard;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.api.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PasteOperation implements Operation {

    private final ModelElement targetElement;
    private final Clipboard clipboard;
    private final Optional<Transaction> transaction;
    private final String info;

    public PasteOperation(Clipboard<?> clipboard, ModelElement targetElement, ModelElementFactory factory) {
        this.clipboard = clipboard;
        this.targetElement = targetElement;
        List<Operation> opList = new ArrayList<>();
        for (ModelElement element : clipboard.getElements()) {
            final ModelElement copy = factory.copyTree(element);
            Operation op = new AddChildOperation(targetElement, copy);
            opList.add(op);
        }
        if (opList.isEmpty()) {
            transaction = Optional.empty();
            info = "Paste empty list";
        } else {
            info = "Paste " + opList.size() + " elements to " + targetElement.getId();
            transaction = Optional.of(new Transaction(true, info, opList));
        }
    }

    @Override
    public boolean undoable() {
        return transaction.isPresent();
    }

    @Override
    public void execute() {
        if (transaction.isPresent()) {
            transaction.get().execute();
        }
    }

    @Override
    public void undo() {
        if (transaction.isPresent()) {
            transaction.get().undo();
        }
    }

    @Override
    public String info() {
        return info;
    }
}
