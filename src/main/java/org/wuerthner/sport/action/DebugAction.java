package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.core.Model;
import org.wuerthner.sport.core.ModelState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugAction implements Action {
    @Override
    public String getId() {
        return "debug";
    }

    @Override
    public String getToolTip() {
        return "Prints the data model to the console";
    }

    @Override
    public Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String, String> parameterMap) {
        if (modelState.hasRootElement()) {
            ModelElement root = modelState.getRootElement();
            String display = Model.makeString(root);
            System.out.println("-------------------------------------");
            System.out.println("| Data Model:");
            System.out.println("|");
            System.out.println(display);
            System.out.println("|");
            System.out.println("| History:");
            System.out.println("|");
            if (!factory.getHistory().isPresent()) {
                System.out.println("No history present!");
            } else {
                List<Operation> history = factory.getHistory().get().getHistory();
                List<Operation> future = factory.getHistory().get().getFuture();
                System.out.println("Past:");
                for (Operation op : history) System.out.println(" - " + op.info());
                System.out.println("Future:");
                for (Operation op : future) System.out.println(" - " + op.info());
            }
            System.out.println("-------------------------------------");


        } else {
            System.out.println("No Element Selected!");
        }
        return new HashMap<>();
    }
}
