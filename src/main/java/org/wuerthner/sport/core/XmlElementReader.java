package org.wuerthner.sport.core;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class XmlElementReader {
    private final ModelElementFactory factory;
    private String patternTimestamp = "^.*timestamp=\"([^\"]+)\".*$";
    private String patternVersion = "^.*version=\"([^\"]+)\".*$";
    private String patternID = "^.*[iI][dD]=\"([^\"]+)\".*$";
    private String patternValue = "^.*[Vv]alue=\"([^\"]+)\".*$";
    private String patternType = "^.*[Tt][Yy][Pp][Ee]=\"([^\"]+)\".*$";
    private String patternKey = "^.*[Kk]ey=\"([^\"]+)\".*$";
    private ElementDef currentDef = null;
    private Attr currentAttr = null;
    private String rootType;

    public XmlElementReader(ModelElementFactory factory, String rootType) {
        this.factory = factory;
        this.rootType = rootType;
    }

    public ModelElement run(InputStream inputStream) {
        ModelElement root = null;
        try {
            String input = readString(inputStream);
            root = getData(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private ModelElement getData(String input) {
        XmlElementReader.Model model = null;
        ModelElement root = null;
        Map<Integer, XmlElementReader.ElementDef> elementDefMap = new HashMap<>();
        try {
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '<') {
                    int startIndex = i + 1;
                    int closeIndex = startIndex + input.substring(startIndex).indexOf('>');
                    String tag = input.substring(startIndex, closeIndex).trim();
                    // System.out.println("==> " + tag);
                    if (tag.startsWith("root")) {
                        String timestamp = tag.replaceFirst(patternTimestamp, "$1");
                        String version = tag.replaceFirst(patternVersion, "$1");
                        model = new XmlElementReader.Model(timestamp, version);
                    } else if (tag.startsWith("ElementDef")) {
                        String id = tag.replaceFirst(patternID, "$1");
                        String type = tag.replaceFirst(patternType, "$1");
                        currentDef = new ElementDef(Integer.valueOf(id), type);
                        elementDefMap.put(currentDef.id, currentDef);
                        currentAttr = null;
                    } else if (tag.startsWith("Attribute")) {
                        // System.out.println(" >> " + tag);
                        String id = tag.replaceFirst(patternKey, "$1");
                        String value = tag.matches(patternValue) ? tag.replaceFirst(patternValue, "$1") : "";
                        String type = tag.replaceFirst(patternType, "$1");
                        currentAttr = new Attr(id, value, type);
                        if (currentDef!=null) {
                            currentDef.add(currentAttr);
                        }
                    } else if (tag.startsWith("/Attribute")) {
                        currentAttr = null;
                    } else if (tag.startsWith("ElementRef")) {
                        String id = tag.replaceFirst(patternID, "$1");
                        XmlElementReader.ElementRef elementRef = new XmlElementReader.ElementRef(Integer.valueOf(id));
                        currentDef.add(elementRef);
                        currentAttr = null;
                    } else if (tag.startsWith("/ElementDef")) {
                        currentDef = null;
                        currentAttr = null;
                    }
                    i = closeIndex;
                } else {
                    int startValueIndex = i;
                    int endValueIndex = startValueIndex + input.substring(startValueIndex).indexOf('<');
                    if (endValueIndex>startValueIndex) {
                        String value = input.substring(startValueIndex, endValueIndex).trim();
                        // System.out.println(" v: " + value);
                        if (currentAttr != null && !value.equals("")) {
                            currentAttr.addValue(value);
                        }
                    } else {
                        break;
                    }
                    i = endValueIndex-1;
                }
            }
            // assemble
            Map<Integer, ModelElement> elementMap = new HashMap<>();
            for (Map.Entry<Integer, XmlElementReader.ElementDef> entry : elementDefMap.entrySet()) {
                int id = entry.getKey();
                XmlElementReader.ElementDef elementDef = entry.getValue();
                String type = elementDef.type;
                // int order = elementDef.order;
                ModelElement element = factory.createElement(type);
                for (XmlElementReader.Attr attr : elementDef.attrList) {
                    ((AbstractModelElement) element).setAttributeValue(attr.key.trim(), attr.value.trim());
                }
                elementMap.put(id, element);
                if (type.equals(rootType)) {
                    root = element;
                }
            }
            for (XmlElementReader.ElementDef elementDef : elementDefMap.values()) {
                for (XmlElementReader.ElementRef elementRef : elementDef.elementRefList) {
                    int defId = elementDef.id;
                    int refId = elementRef.id;
                    ModelElement parent = elementMap.get(defId);
                    ModelElement child = elementMap.get(refId);
                    if (child == null) {
                        throw new RuntimeException("Child " + refId + " not found!");
                    }
                    ((AbstractModelElement) parent).addChild(child);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    private String readString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = inputStream.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        return result.toString("UTF-8");
    }

    private class ElementDef {
        final int id;
        final String type;
        final List<XmlElementReader.Attr> attrList = new ArrayList<>();
        final List<XmlElementReader.ElementRef> elementRefList = new ArrayList<>();

        public ElementDef(int id, String type) {
            this.id = id;
            if (type.indexOf('.')>=0) {
                int index = type.lastIndexOf('.');
                type = type.substring(index+1);
            }
            this.type = type;
        }

        void add(XmlElementReader.Attr attr) {
            attrList.add(attr);
        }

        void add(XmlElementReader.ElementRef elementRef) {
            elementRefList.add(elementRef);
        }
    }

    private class ElementRef {
        final int id;

        public ElementRef(int id) {
            this.id = id;
        }
    }

    private class Model {
        final String timestamp;
        final double version;

        public Model(String timestamp, String version) {
            double v = 0.0;
            try {
                v = Double.valueOf(version);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            this.timestamp = timestamp;
            this.version = v;
        }
    }

    private class Attr {
        final String key;
        String value;
        Class<?> type;

        public Attr(String key, String value, String type) {
            this.key = key;
            this.value = value;
//            System.out.println("NEW ATTR: " + key + " = " + value);
//            if (type.indexOf('.')>=0) {
//                int index = type.lastIndexOf('.');
//                type = type.substring(index+1);
//            }
//            try {
//                this.type = Class.forName(type);
//            } catch (ClassNotFoundException e) {
//                System.err.println("Class '" + type + "' not found!");
//                e.printStackTrace();
//                System.exit(0); // TODO: remove!
//            }
        }

        public void addValue(String data) {
            this.value += data.replaceAll("\n", "");
        }
    }
}
