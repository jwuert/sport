package org.wuerthner.sport.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.wuerthner.sport.api.ModelElement;


public class Delta {
    public enum Type {
        Value("Attribute Value"),
        Element("Element"),
        Error("Error");
        public String label;
        Type(String label) {
            this.label = label;
        }
    }

    private final List<Difference> differenceList = new ArrayList<>();

    public void addValueDifference(ModelElement element1, ModelElement element2, String attributeId, String value1, String value2) {
        verifyIdAndType(element1, element2);
        Difference diff = new Difference(Type.Value, element1.getFullId(), element1.getType(), value1, value2);
        add(diff);
    }

    public void addOnlyInFirstDifference(ModelElement element) {
        Difference diff = new Difference(Type.Element, element.getFullId(), element.getType(), "exists only in first", "-"); // "only in first"
        add(diff);
    }

    public void addOnlyInSecondDifference(ModelElement element) {
        Difference diff = new Difference(Type.Element, element.getFullId(), element.getType(), "-", "exists only in second"); // "only in second"
        add(diff);
    }

    public void add(Delta delta) {
        for (Difference newDiff : delta.differenceList) {
            add(newDiff);
        }
    }

    private void add(Difference newDiff) {
        if (!differenceList.contains(newDiff)) {
            differenceList.add(newDiff);
            List<Difference> toBeRemovedList = new ArrayList<>();
            for (Difference diff : differenceList) {
                Optional<Difference> d = diff.getDifferenceWithShorterPath(newDiff);
                if (d.isPresent()) {
                    toBeRemovedList.add(d.get());
                }
            }
            for (Difference remove : toBeRemovedList) {
                differenceList.remove(remove);
            }
        }
    }

    public boolean isEmpty() {
        return differenceList.isEmpty();
    }

    public int size() {
        return differenceList.size();
    }

    public Difference getDifference(String id) {
        for (Difference diff : differenceList) {
            if (diff.fqid.equals(id)) {
                return diff;
            }
        }
        return null;
    }

    public String getDifferencesInHTML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<table>");
        buf.append("<tr>");
        buf.append("<th>Comparison</th>");
        buf.append("<th>ID</th>");
        buf.append("<th>Type</th>");
        buf.append("<th>Value 1</th>");
        buf.append("<th>Value 2</th>");
        buf.append("</tr>");
        for (Difference diff : differenceList) {
            buf.append("<tr>");
            buf.append("<td>"+diff.type.label+"</td>");
            buf.append("<td>"+diff.fqid+"</td>");
            buf.append("<td>"+diff.elementType+"</td>");
            buf.append("<td>"+diff.value1+"</td>");
            buf.append("<td>"+diff.value2+"</td>");
            buf.append("</tr>");
        }
        buf.append("</table>");
        return buf.toString();
    }

    public String getDifferencesInText() {
        return getDifferencesInText(Optional.empty());
    }

    public String getDifferencesInText(Type type) {
        return getDifferencesInText(Optional.of(type));
    }

    private String getDifferencesInText(Optional<Type> type) {
        StringBuffer buf = new StringBuffer();
        buf.append(makeTab("Comparison", 20));
        buf.append(makeTab("ID", 30));
        buf.append(makeTab("Type", 15));
        buf.append(makeTab("Value 1", 30));
        buf.append(makeTab("Value 2", 30));
        buf.append(System.lineSeparator());
        for (Difference diff : differenceList) {
            if (!type.isPresent() || diff.type==type.get()) {
                buf.append(makeTab(diff.type.label, 20));
                buf.append(makeTab(diff.fqid, 30));
                buf.append(makeTab(diff.elementType, 15));
                buf.append(makeTab(diff.value1, 30));
                buf.append(makeTab(diff.value2, 30));
                buf.append(System.lineSeparator());
            }
        }
        return buf.toString();
    }

    public List<Difference> getDifferences() {
        return differenceList;
    }

    private void verifyIdAndType(ModelElement tree1, ModelElement tree2) {
        if (tree1.getType()==null) {
            add(new Difference(Type.Error, tree1.getFullId(), tree1.getType(), "first missing type", "-"));
        }
        if (tree2.getType()==null) {
            add(new Difference(Type.Error, tree2.getFullId(), tree2.getType(), "-", "second missing type"));
        }
        if (tree1.getType()!=null && tree2.getType()!=null) {
            if (tree1.getId() != tree2.getId()) {
                throw new RuntimeException("Comparison: two elements have different IDs: " + tree1.getId()+"/"+tree2.getId());
            }
            if (!tree1.getType().equals(tree2.getType())) {
                throw new RuntimeException("Comparison: two elements have different types: " + tree1.getType()+"/"+tree2.getType());
            }
        }
    }

    private static String makeTab(String input, int len) {
        String value = input + make(' ', len-input.length());
        return value;
    }

    private static String make(char c, int size) {
        String result = "";
        if (size>0) {
            result = new String(new char[size]).replace('\0', c);
        }
        return result;
    }

    public class Difference {
        public final Type type;
        public final String id;
        public final String fqid;
        public final String elementType;
        public final String value1;
        public final String value2;

        public Difference(Type type, String fqid, String elementType, Set<String> set1, Set<String> set2) {
            // Attributes differ
            this.type = type;
            this.fqid = fqid==null ? "" : fqid;
            this.elementType = elementType==null ? "" : elementType;
            this.id = cutId(this.fqid);
            String msg = "";
            String sep = "Missing in first: ";
            for (String s : set2) {
                if (!set1.contains(s)) {
                    msg += sep;
                    msg += s;
                    sep = ", ";
                }
            }
            this.value1 = msg;
            sep = "Missing in second: ";
            msg = "";
            for (String s : set1) {
                if (!set2.contains(s)) {
                    msg += sep;
                    msg += s;
                    sep = ", ";
                }
            }
            this.value2 = msg;
        }

        private String cutId(String input) {
            int i = input.lastIndexOf(".");
            if (i<0) {
                return input;
            } else {
                return input.substring(i+1);
            }
        }

        public Difference(Type type, String fqid, String elementType, String value1, String value2) {
            this.type = type;
            this.fqid = fqid==null ? "" : fqid;
            this.elementType = elementType==null ? "" : elementType;
            List<String> list = Arrays.asList(value1==null?"-":value1, value2==null?"-":value2);
            this.value1 = list.get(0);
            this.value2 = list.get(1);
            this.id = cutId(this.fqid);
        }

        public Optional<Difference> getDifferenceWithShorterPath(Difference diff) {
            if (type.equals(diff.type) && elementType.equals(diff.elementType) && value1.equals(diff.value1) && value2.equals(diff.value2) && id.equals(diff.id) && fqid.length()!=diff.fqid.length()) {
                if (fqid.indexOf(diff.fqid)>=0) {
                    return Optional.of(diff);
                } else if (diff.fqid.indexOf(fqid)>=0) {
                    return Optional.of(this);
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        }

        public String toString() {
            return type+":"+fqid+":"+elementType+":"+value1+":"+value2;
        }

        @Override
        public boolean equals(Object o) {

            if (o == this) return true;
            if (!(o instanceof Difference)) {
                return false;
            }

            Difference diff = (Difference) o;
            return new EqualsBuilder()
                    .append(type, diff.type)
                    .append(fqid, diff.fqid)
                    .append(elementType, diff.elementType)
                    .append(value1, diff.value1)
                    .append(value2, diff.value2)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(type)
                    .append(fqid)
                    .append(elementType)
                    .append(value1)
                    .append(value2)
                    .toHashCode();
        }
    }
}