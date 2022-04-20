package org.wuerthner.sport.core;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.ModelElement;

import java.util.*;

public class ValidationResult {
    private Date timestamp;
    private List<ValidationResultEntry> validationResultList = new ArrayList<>();

    public ValidationResult() {
        timestamp = new Date();
    }

    public void addWarning(ModelElement element, String message) {
        validationResultList.add(new ValidationResultEntry(element, message, ValidationResultEntry.WARNING));
    }

    public void addError(ModelElement element, String message) {
        validationResultList.add(new ValidationResultEntry(element, message, ValidationResultEntry.ERROR));
    }

    public void addError(ModelElement element, Attribute<?> attribute, String message) {
        validationResultList.add(new ValidationResultEntry(element, attribute, message, ValidationResultEntry.ERROR));
    }

    public void addEntries(ValidationResult validationResult) {
        validationResultList.addAll(validationResult.getEntries());
    }

    public List<ValidationResultEntry> getEntries() {
        return validationResultList;
    }

    public List<String> getMessages() {
        List<String> list = new ArrayList<>();
        for (ValidationResultEntry entry : validationResultList) {
            list.add(entry.getMessage());
        }
        return list;
    }

    public List<String> getMessages(Attribute<?> attribute) {
        List<String> list = new ArrayList<>();
        for (ValidationResultEntry entry : validationResultList) {
            if (entry.attributeName.equals(attribute.getName()))
                list.add(entry.getMessage());
        }
        return list;
    }

    public List<String> getShortMessages(Attribute<?> attribute) {
        List<String> list = new ArrayList<>();
        for (ValidationResultEntry entry : validationResultList) {
            if (entry.attributeName.equals(attribute.getName()))
                list.add(entry.getShortMessage());
        }
        return list;
    }

    public void print() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("Validation: " + timestamp);
        for (String msg : getMessages()) {
            System.out.println(msg);
        }
    }

    public Map<String,String> asMap() {
        Map<String,String> map = new LinkedHashMap<>();
        for (ValidationResultEntry result : validationResultList) {
            map.put(result.getMessage(), result.elementFqId);
        }
        return map;
    }

    public class ValidationResultEntry {
        public static final String INFO = "Info";
        public static final String ERROR = "Error";
        public static final String WARNING = "Warning";

        public final String type;
        public final long elementTId;
        public final String elementId;
        public final String elementFqId;
        public final String elementLabel;
        public final String elementType;
        public final String attributeName;
        public final String attributeLabel;
        public final boolean valid;
        public final String validationMessage;

        public ValidationResultEntry() {
            this.valid = true;
            this.validationMessage = "";
            this.type = INFO;
            this.elementTId = 0;
            this.elementId = "";
            this.elementFqId = "";
            this.elementLabel = "-";
            this.elementType = "";
            this.attributeName = "";
            this.attributeLabel = "";
        }

        public ValidationResultEntry(ModelElement element, String validationMessage) {
            // element = getBranchElement(element);
            this(element, null, validationMessage, WARNING);
        }

        public ValidationResultEntry(ModelElement element, String validationMessage, String type) {
            this(element, null, validationMessage, type);
        }

        public ValidationResultEntry(ModelElement element, Attribute<?> attribute, String validationMessage, String type) {
            this.valid = false;
            this.type = type;
            this.validationMessage = validationMessage;
            this.elementTId = element.getTechnicalId();
            this.elementId = element.getId();
            this.elementFqId = element.getFullId();
            this.elementLabel = element.getAttributeValue("label");
            this.elementType = element.getType();
            this.attributeName = attribute == null ? null : attribute.getName();
            this.attributeLabel = attribute == null ? null : attribute.getLabel();
        }

        public String getMessage() {
            return type + ": " + elementType + " " + elementId + (attributeName == null ? "" : ", field " + attributeLabel) + " " + validationMessage;
        }

        public String getShortMessage() {
            return type + ": " + (attributeName == null ? "" : attributeLabel + " ") + validationMessage;
        }
    }
}
