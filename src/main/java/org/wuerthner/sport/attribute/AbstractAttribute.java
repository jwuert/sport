package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.Check;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAttribute<T> implements Attribute<T> {
	
	public final String name;
	public final String label;
	public final Class<? extends T> attributeType;
	public final Optional<T> defaultValue;
	public final boolean readonly;
	public final boolean required;
	public final boolean hidden;
	public final Optional<String> description;
	public final List<Check> dependencies = new ArrayList<>();
	public final List<Check> validators = new ArrayList<>();
	
	public AbstractAttribute(String name, String label, Class<? extends T> attributeType, T defaultValue, boolean readonly,
							 boolean required, boolean hidden, String description, List<Check> dependencies, List<Check> validators) {
		this.name = name;
		this.label = label;
		this.attributeType = attributeType;
		this.defaultValue = defaultValue != null ? Optional.of(defaultValue) : Optional.empty();
		this.readonly = readonly;
		this.required = required;
		this.hidden = hidden;
		this.description = Optional.of(description);
		this.getDependencies().addAll(dependencies);
		this.getValidators().addAll(validators);
	}
	
	@Override
	public Class<? extends T> getAttributeType() {
		return attributeType;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public Optional<T> getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public boolean hasDefaultValue() {
		return defaultValue.isPresent();
	}
	
	@Override
	public boolean isReadonly() {
		return readonly;
	}
	
	@Override
	public boolean isRequired() {
		return required;
	}
	
	@Override
	public boolean isHidden() {
		return hidden;
	}
	
	@Override
	public String getStringPresentation(T value) {
		return value.toString();
	}
	
	@Override
	public boolean isEmpty(T stringValue) {
		String value = stringValue == null ? null : getStringPresentation(stringValue);
		return value == null || value.trim().equals("");
	}
	
	@Override
	public String toString() {
		return "Attribute<" + attributeType.getSimpleName() + ">:" + getName();
	}
	
	@Override
	public Optional<String> getDescription() {
		return description;
	}
	
	public List<Check> getDependencies() {
		return dependencies;
	}
	
	public List<Check> getValidators() {
		return validators;
	}
}
