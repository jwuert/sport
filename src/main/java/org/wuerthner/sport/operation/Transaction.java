package org.wuerthner.sport.operation;

import org.wuerthner.sport.api.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Transaction implements Operation, Iterable<Operation> {

    private final Operation[] operationList;
    private final boolean reverseUndo;
    private final String name;

    public Transaction(Operation... operationList) {
        this(null, operationList);
    }

    public Transaction(String name, Operation... operationList) {
        this.name = name;
        this.operationList = operationList;
        this.reverseUndo = false;
    }

    public Transaction(boolean reverseUndo, String name, Operation... operationList) {
        this.name = name;
        this.operationList = operationList;
        this.reverseUndo = reverseUndo;
    }

    public Transaction(String name, List<? extends Operation> operationList) {
        this(name, operationList.toArray(new Operation[] {}));
    }

    @Override
    public boolean undoable() {
        return true;
    }

    @Override
    public void execute() {
        for (int i = 0; i < operationList.length; i++) {
            operationList[i].execute();
        }
    }

    @Override
    public void undo() {
        if (reverseUndo) {
            for (int i = operationList.length - 1; i >= 0; i--) {
                operationList[i].undo();
            }
        } else {
            for (int i = 0; i < operationList.length; i++) {
                operationList[i].undo();
            }
        }
    }

    @Override
    public String info() {
        StringBuilder stringBuilder = new StringBuilder();
        if (name != null) {
            stringBuilder.append(name);
        } else if (operationList.length > 0) {
            Operation operation = operationList[0];
            stringBuilder.append(operation.info());
            if (operationList.length > 1) {
                stringBuilder.append(" ...");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public Iterator<Operation> iterator() {
        return new ArrayList<Operation>(Arrays.asList(operationList)).iterator();
    }

    @Override
    public String toString() {
        return info();
    }
}
