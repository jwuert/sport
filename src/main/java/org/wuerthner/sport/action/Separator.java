package org.wuerthner.sport.action;

import org.wuerthner.sport.api.Action;

public class Separator implements Action {
    public final static String ID = "separator";

    @Override
    public String getId() {
        return ID;
    }
}
