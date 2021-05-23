package org.wuerthner.sport.core;

import org.wuerthner.sport.api.History;
import org.wuerthner.sport.api.Operation;

import java.util.ArrayList;
import java.util.List;

public class ModelHistory implements History {
    private int max_undo = 400;
    private boolean journal = false;
    private final List<Operation> operationHistory = new ArrayList<Operation>();
    private final List<Operation> operationFuture = new ArrayList<Operation>();

    @Override
    public void clear() {
        operationHistory.clear();
        operationFuture.clear();
    }

    public int getMaxUndo() {
        return max_undo;
    }

    @Override
    public void add(Operation op) {
        if (op.undoable()) {
            //
            // Only remember undoable operation: Add to history
            //
            operationHistory.add(op);
            if (operationFuture.size() > 0) {
                operationFuture.clear();
            }
            if (operationHistory.size() > max_undo) {
                operationHistory.remove(0);
            }
        }
    }

    /**
     * Undoes an operation
     **/
    @Override
    public void undo() {
        synchronized (this) {
            if (hasUndo()) {
                Operation operation = (Operation) operationHistory.get(operationHistory.size() - 1);
                operationHistory.remove(operation);
                operationFuture.add(operation);
                operation.undo();
            }
        }
    }

    /**
     * Redoes an operation
     **/
    @Override
    public void redo() {
        synchronized (this) {
            if (hasRedo()) {
                Operation operation = (Operation) operationFuture.get(operationFuture.size() - 1);
                operationFuture.remove(operation);
                operationHistory.add(operation);
                operation.execute();
            }
        }
    }

    /**
     * Returns true if there is something in the undo list
     **/
    @Override
    public boolean hasUndo() {
        return operationHistory.size() > 0;
    }

    /**
     * Returns true if there is something in the redo list
     **/
    @Override
    public boolean hasRedo() {
        return operationFuture.size() > 0;
    }

    @Override
    public String toString() {
        return "History={past: " + operationHistory.size() + ", future: " + operationFuture.size() + "}";
    }

    @Override
    public List<Operation> getHistory() {
        return operationHistory;
    }

    @Override
    public List<Operation> getFuture() {
        return operationFuture;
    }
}
