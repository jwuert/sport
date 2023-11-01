package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.attributetype.DynamicMultiSelect;
import org.wuerthner.sport.core.ElementFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicListAttribute<T> extends AbstractAttribute<List<T>, DynamicListAttribute<T>, DynamicMultiSelect> implements DynamicMultiSelect {
    public final static String SEP = "\\|";
    public final Class<T> elementType;
    private ElementFilter elementFilter;

    public DynamicListAttribute(String name, Class<T> clasz) {
        super(name, (Class<? extends List<T>>) ((Class<?>)List.class), DynamicMultiSelect.class);
        this.elementType = clasz;
    }

    public DynamicListAttribute<T> addFilter(ElementFilter elementFilter) {
        this.elementFilter = elementFilter;
        return this;
    }

    @Override
    public ElementFilter getElementFilter() { return elementFilter; }

    @Override
    public List<T> getValue(String stringValue) {
        if (stringValue == null || stringValue.equals("")) {
            return null;
        } else if (stringValue.equals("[]")) {
            List<T> convertList = new ArrayList<T>();
            return convertList;
        } else {
            try {
                String[] stringArray = stringValue.replaceFirst("^\\[", "").replaceAll("\\]$", "").replaceAll("\\\\,", SEP).split(",");
                List<T> convertList = new ArrayList<T>();
                for (String v : stringArray) {
                    convertList.add(convertComponent(v.replaceAll(SEP, ",")));
                }
                return convertList;
            } catch (IllegalArgumentException | SecurityException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getStringPresentation(List<T> value) {
        List<T> maskedList = new ArrayList<>();
        for (T element : value) {
            T maskedElement;
            if (element instanceof String) {
                maskedElement = (T) ((String) element).replaceAll("\\,", "\\\\,");
            } else {
                maskedElement = element;
            }
            maskedList.add(maskedElement);
        }
        return maskedList.toString();
    }

    private T convertComponent(String value) {
        if (value == null || value.trim().equals("")) {
            return null;
        }
        value = value.trim();
        T result = null;
        boolean methodExistsButConversionFails = false;

        try {
            result = useValueOf(value);
        } catch (InvocationTargetException e) {
            methodExistsButConversionFails = true;
        }

        if (result != null) {
            return result;
        }

        try {
            result = useConstructor(value);
        } catch (InvocationTargetException e) {
            methodExistsButConversionFails = true;
        }

        if (result == null) {
            if (methodExistsButConversionFails) {
                String msg = "String '" + value + "' cannot be converted to an object of class " + elementType.getSimpleName();
                throw new RuntimeException(msg);
            } else {
                throw new RuntimeException("Class " + elementType.getSimpleName() + " must either have a public static method 'valueOf(String value)' or a public String-constructor!");
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private T useValueOf(String value) throws InvocationTargetException {
        T result = null;
        try {
            Method valueOfMethod = elementType.getMethod("valueOf", String.class);
            result = (T) valueOfMethod.invoke(null, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
            // ok, valueOf method does not exist!
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private T useConstructor(String value) throws InvocationTargetException {
        T result = null;
        try {
            Constructor<T> constructor = (Constructor<T>) elementType.getConstructor(String.class);
            result = constructor.newInstance(value);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e1) {
            // ok, string-constructor does not exist!
        }
        return result;
    }

    @Override
    public Map<String, Object> getValueMap(ModelElement selectedElement) {
        ModelElement root = selectedElement.getRoot();
        List<ModelElement> resultList = root.lookupByType(elementFilter.type)
                .stream()
                .filter(el -> elementFilter.filter.evaluate(el, null))
                .collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        for (ModelElement el : resultList) {
            map.put(el.getAttributeValue("id"), el.getAttributeValue("id"));
        }
        return map;
    }
}
