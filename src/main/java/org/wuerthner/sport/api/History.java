package org.wuerthner.sport.api;

import org.wuerthner.sport.api.Operation;

import java.util.List;

public interface History {
    public void add(Operation op);

    public void undo();

    public void redo();

    public boolean hasUndo();

    public boolean hasRedo();

    public void clear();

    public List<Operation> getHistory();

    public List<Operation> getFuture();
}
