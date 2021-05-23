package org.wuerthner.sport.core;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

public class Model {
    public final static String SEP = System.lineSeparator();

    public static String makeString(ModelElement element) {
        return makeString(element, 0);
    }

    public static String makeString(ModelElement element, int depth) {
        StringBuffer buf = new StringBuffer();
        String name = element.getAttributeValue("id");
        String space = make(' ', depth * 2);
        // System.out.println(element.getType() + (name == null ? "" : " \"" + name + "\""));
        buf.append(space + element.getType() + " [" + element.getId() + ":" + element.getTechnicalId() + "]"+ SEP);
        for (Attribute<?> attribute : element.getAttributes()) {
            String key = attribute.getName();
            String value = element.getAttributeValue(attribute.getName());
            // if (!key.equals("name")) {
            buf.append(space + "  - " + key + ": " + value + SEP);
            // }
        }
        for (ModelElement child : element.getChildren()) {
            buf.append(makeString(child, depth + 1));
        }
        return buf.toString();
    }

    static private String make(char c, int size) {
        String result = new String(new char[size]).replace('\0', c);
        return result;
    }
}
