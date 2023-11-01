package org.wuerthner.sport.api.attributetype;

import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.core.ElementFilter;

import java.util.Map;

public interface DynamicMapping {
    public Map<String, Object> getValueMap(ModelElement selectedElement);
    public ElementFilter getElementFilter();
}
