package org.wuerthner.sample;

import org.wuerthner.sport.action.*;
import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ActionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestActionProvider implements ActionProvider {
        protected final List<Action> actionList = new ArrayList<>();

        public TestActionProvider() {
            actionList.add(new NewRootElementAction());
            actionList.add(new NewElementAction());
            actionList.add(new ImportAction());
            actionList.add(new ExportAction());

            actionList.add(new Separator());

            actionList.add(new UndoAction());
            actionList.add(new RedoAction());

            actionList.add(new Separator());

            actionList.add(new DeleteAction());
            actionList.add(new CutAction());
            actionList.add(new CopyAction());
            actionList.add(new PasteAction());
            actionList.add(new ClearAction());

            actionList.add(new Separator());

            actionList.add(new AboutAction());
            actionList.add(new ValidateAction());
            actionList.add(new CompareAction());
            actionList.add(new DebugAction());

            actionList.add(new Separator());

            actionList.add(new GenerateModelReportAction());
            actionList.add(new GenerateReportAction());
        }
    @Override
    public List<Action> getActionList() {
        return actionList;
    }

    @Override
    public List<String> getIdList() {
        return actionList.stream().map(action -> action.getId()).collect(Collectors.toList());
    }

    @Override
    public Optional<Action> getAction(String id) {
        return actionList.stream().filter(action -> action.getId().equals(id)).findAny();
    }}
