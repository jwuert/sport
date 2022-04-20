package org.wuerthner.sport.api;

import java.util.List;
import java.util.Optional;

public interface ActionProvider {
    public List<Action> getActionList();

    public List<String> getIdList();

    public Optional<Action> getAction(String id);
}
