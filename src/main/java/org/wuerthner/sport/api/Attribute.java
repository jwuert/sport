package org.wuerthner.sport.api;

import java.util.List;
import java.util.Optional;

public interface Attribute<T> {
	
	Class<? extends T> getValueType();

	String getInputType();

	String getName();
	
	Optional<T> getDefaultValue();
	
	abstract public T getValue(String stringValue);
	
	boolean hasDefaultValue();
	
	boolean isReadonly();
	
	boolean isRequired();
	
	boolean isHidden();
	
	String getStringPresentation(T value);
	
	String getLabel();
	
	boolean isEmpty(T Value);
	
	Optional<String> getDescription();
	
	List<Check> getDependencies();
	
	List<Check> getValidators();
}
