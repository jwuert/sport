package org.wuerthner.sport.core;

import org.wuerthner.sport.api.Check;

public class ElementFilter {
    public final String type;
    public final Check filter;

    public ElementFilter(String type, Check filter) {
        this.type = type;
        this.filter = filter;
    }
}
