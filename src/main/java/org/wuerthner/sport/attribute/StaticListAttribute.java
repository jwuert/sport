package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.attributetype.DynamicMultiSelect;
import org.wuerthner.sport.api.attributetype.StaticMultiSelect;
import org.wuerthner.sport.core.ElementFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class StaticListAttribute<T> extends AbstractAttribute<List<T>, StaticListAttribute<T>, StaticMultiSelect> implements StaticMultiSelect {
    public final static String SEP = "\\|";
    private Map<String, T> selectableValueMap = new LinkedHashMap<>();
    public final Class<T> elementType;

    public StaticListAttribute(String name, Class<T> clasz) {
        //noinspection unchecked
        super(name, (Class<? extends List<T>>) ((Class<?>)List.class), StaticMultiSelect.class);
        this.elementType = clasz;
    }

    public StaticListAttribute<T> values(Map<String, T> selectableValues) {
        this.selectableValueMap.putAll(selectableValues);
        return this;
    }

    public StaticListAttribute<T> addValue(String key, T value) {
        this.selectableValueMap.put(key, value);
        return this;
    }

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
    public Map<String, T> getValueMap() {
        return selectableValueMap;
    }
}
