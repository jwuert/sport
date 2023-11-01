package org.wuerthner.sport.api;

import org.wuerthner.sport.core.ModelState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Action {
    public final static String DISPLAY = "display";
    public final static String ERROR = "error";
    public final static String ROOT = "root";
    public final static String SELECTION = "selection";
    public final static String VALIDATION = "validation";
    public static final String DELTA = "delta";
    public static final String NEW = "new";

    public String getId();

    public default boolean requiresData() {
        return false;
    }

    public default boolean isEnabled(ModelElement selectedElement, ModelElementFactory factory) { return true; }

    public default String getToolTip() {
        return "-";
    }

    public default List<Attribute<?>> getParameterList(ModelElement selectedElement) {
        return new ArrayList<>();
    }

    public default Map<String,Object> invoke(ModelElementFactory factory, ModelState modelState, Map<String,String> parameterMap) {
        return new HashMap<>();
    }

    default List<String> getDescription() {
        return new ArrayList<>();
    }
}
