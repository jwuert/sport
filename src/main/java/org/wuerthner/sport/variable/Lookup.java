package org.wuerthner.sport.variable;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.Variable;

import java.util.*;
import java.util.stream.Collectors;

public class Lookup<TYPE> extends AbstractVariable<String> {
    public static final int THIS_LEVEL = 0;
    public static final int PARENT_LEVEL = 1;

    private int parentCounter = 0;
    private String type = null;
    private Check check = null;
    private List<LookupValue> valueList = new ArrayList<>();

    public Lookup() {
        super("Lookup");
    }

    @Override
    public String evaluate(ModelElement modelElement) {
        ModelElement element = modelElement;
        // find starting point in tree:
        for (int i=0; i<parentCounter; i++) {
            element = element.getParent();
        }
        // Lookup by type or use starting point:
        List<ModelElement> elementList = new ArrayList<>();
        if (type != null) {
            for (ModelElement el : element.lookupByType(type)) {
                if (check == null || check.evaluate(el, null)) {
                    elementList.add(el);
                }
            }
        } else {
            elementList = Arrays.asList(element);
        }
        // process values:
        String result = "";
        String sep = "";
        for (ModelElement el : elementList) {
            result = result + sep;
            for (LookupValue lookupValue: valueList) {
                result = result + lookupValue.getValue(el);
            }
            sep = "|";
        }
        return result;
    }

    public Lookup<TYPE> parent() {
        parentCounter++;
        addProperty("parentCounter", parentCounter);
        return this;
    }

    public Lookup<TYPE> type(String type) {
        this.type = type;
        addProperty("type", type);
        return this;
    }

    public Lookup<TYPE> filter(Check check) {
        this.check = check;
        addProperty("check", check);
        return this;
    }

    public Lookup<TYPE> value(Attribute<TYPE> attribute) {
        valueList.add(new LookupValue(THIS_LEVEL, attribute));
        addProperty("valueList", valueList.stream().map(lookupValue -> lookupValue.getProperties()).collect(Collectors.toList()));
        return this;
    }

    public Lookup<TYPE> value(int level, Attribute<?> attribute) {
        valueList.add(new LookupValue(level, attribute));
        addProperty("valueList", valueList.stream().map(lookupValue -> lookupValue.getProperties()).collect(Collectors.toList()));
        return this;
    }

    public Lookup<TYPE> value(String value) {
        valueList.add(new LookupValue(value));
        addProperty("valueList", valueList.stream().map(lookupValue -> lookupValue.getProperties()).collect(Collectors.toList()));
        return this;
    }

    public class LookupValue {
        public final int level;
        public final Attribute<?> attribute;
        public final String value;

        public LookupValue(int level, Attribute<?> attribute) {
            this.level = level;
            this.attribute = attribute;
            this.value = null;
        }

        public LookupValue(String value) {
            this.value = value;
            this.level = -1;
            this.attribute = null;
        }

        public String getValue(ModelElement element) {
            String result = "";
            if (value != null) {
                result = value;
            } else {
                for (int i=0; i<level; i++) {
                    element = element.getParent();
                }
                result = "" + element.getAttributeValue(attribute);
            }
            return result;
        }

        public Map<String,Object> getProperties() {
            Map<String,Object> map = new HashMap<>();
            if (level >= 0)
                map.put("level", level);
            if (attribute != null)
                map.put("attribute", attribute.getName());
            if (value != null)
                map.put("value", value);
            return map;
        }
    }
}
