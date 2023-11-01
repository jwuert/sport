package org.wuerthner.sport.core;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

import java.util.List;
import java.util.stream.Collectors;

public class Model {
    public final static String SEP = System.lineSeparator();
    public final static String REFERENCE_FILE = "referenceFile";
    public final static int MAX_ID_LENGTH = 40;

    public static String makeString(ModelElement element) {
        return makeString(element, 0);
    }

    public static String makeString(ModelElement element, int depth) {
        StringBuffer buf = new StringBuffer();
        String name = element.getAttributeValue("id");
        String space = make(' ', depth * 2);
        // System.out.println(element.getType() + (name == null ? "" : " \"" + name + "\""));
        buf.append(space + element.getType() + " [" + element.getId() + ":" + element.getTechnicalId() + (element.isDeleted()?" D":"") + (element.isInClipboard()?" C":"") + "] " + element.hashCode() + SEP);
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

    public static String makeUniqueId(ModelElement element, String id) {
        String result = id;
        if (result.length()>MAX_ID_LENGTH) {
            result = result.substring(0,MAX_ID_LENGTH);
        }
        List<String> idList = element.getChildren().stream().map(ch -> ch.getId()).collect(Collectors.toList());
        while (idList.contains(result)) {
            int len = result.length();
            if (result.matches(".*[0-9][0-9][0-9]")) {
                int n = Integer.parseInt(result.substring(len - 3));
                n++;
                result = result.substring(0, len - 3) + String.format("%03d", n);
            } else {
                int diff = len + 3 - MAX_ID_LENGTH;
                if (diff > 0)
                    result = result.substring(0, MAX_ID_LENGTH - 3);
                result += "001";
            }
        }
        return result;
    }
}
