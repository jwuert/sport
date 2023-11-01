package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Attribute;
import org.wuerthner.sport.api.Check;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAttribute<T,AT,IT> implements Attribute<T> {
	
	private final String name;
	private final Class<? extends T> valueType;
	private final Class<IT> inputType;
	private String label;
	private Optional<T> defaultValue = Optional.empty();
	private boolean readonly = false;
	private boolean required = false;
	private boolean hidden = false;
	private Optional<String> description = Optional.empty();
	private List<Check> dependencies = new ArrayList<>();
	private List<Check> validators = new ArrayList<>();

	protected AbstractAttribute(String name, Class<? extends T> valueType, Class<IT> inputType) {
		this.name = name;
		this.valueType = valueType;
		this.inputType = inputType;
	}

	public AT label(String label) {
		this.label = label;
		return (AT) this;
	}

	public AT description(String description) {
		this.description = Optional.ofNullable(description);
		return (AT) this;
	}

	public AT defaultValue(T defaultValue) {
		this.defaultValue = defaultValue != null ? Optional.of(defaultValue) : Optional.empty();
		return (AT) this;
	}

	public AT required() {
		this.required = true;
		return (AT) this;
	}

	public AT readonly() {
		this.readonly = true;
		return (AT) this;
	}

	public AT hidden() {
		this.hidden = true;
		return (AT) this;
	}

	public AT addDependency(Check dependency) {
		this.dependencies.add(dependency);
		return (AT) this;
	}

	public AT addValidation(Check validator) {
		this.validators.add(validator);
		return (AT) this;
	}

	@Override
	public Class<? extends T> getValueType() {
		return valueType;
	}

	@Override
	public String getInputType() {
		return inputType.getSimpleName();
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
		return "Attribute<" + valueType.getSimpleName() + ">:" + getName();
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
