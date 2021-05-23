package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.attribute.SelectableStringAttribute.ElementFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeBuilder {
	protected String id;
	protected String label = "Unnamed";
	protected boolean required = false;
	protected boolean hidden = false;
	protected boolean readonly = false;
	protected Object defaultValue = null;
	protected String description = "";
	protected Map<String, String> values = new HashMap<>();
	protected SelectableStringAttribute.ElementFilter elementFilter = null;
	protected List<Check> dependencies = new ArrayList<>();
	protected List<Check> validators = new ArrayList<>();
	
	public AttributeBuilder(String id) {
		this.id = id;
	}
	
	public AttributeBuilder label(String label) {
		this.label = label;
		return this;
	}
	
	public AttributeBuilder descr(String description) {
		this.description = description;
		return this;
	}
	
	public AttributeBuilder required() {
		required = true;
		return this;
	}
	
	public AttributeBuilder readonly() {
		readonly = true;
		return this;
	}
	
	public AttributeBuilder hidden() {
		hidden = true;
		return this;
	}
	
	public AttributeBuilder defaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	public AttributeBuilder values(String[] selectableValues) {
		for (String value : selectableValues) {
			values.put(value, value);
		}
		return this;
	}
	
	public AttributeBuilder values(Map<String, String> selectableValues) {
		values.putAll(selectableValues);
		return this;
	}
	
	public AttributeBuilder elements(String type, Check check) {
		elementFilter = new ElementFilter(type, check);
		return this;
	}
	
	public AttributeBuilder addDependency(Check dependency) {
		dependencies.add(dependency);
		return this;
	}
	
	public AttributeBuilder addValidation(Check validator) {
		validators.add(validator);
		return this;
	}
	
	public StringAttribute buildStringAttribute() {
		return new StringAttribute(id, label, (String) defaultValue, readonly, required, hidden, description, dependencies, validators);
	}

	public IntegerAttribute buildIntegerAttribute() {
		return new IntegerAttribute(id, label, (Integer) defaultValue, readonly, required, hidden, description, dependencies, validators);
	}

	public LongAttribute buildLongAttribute() {
		return new LongAttribute(id, label, (Long) defaultValue, readonly, required, hidden, description, dependencies, validators);
	}

	public MappedSelectableStringAttribute buildMappedSelectable() {
		return new MappedSelectableStringAttribute(id, label, (String) defaultValue, values, readonly, required, hidden, description, dependencies, validators);
	}

	public BooleanAttribute buildBooleanAttribute() {
		return new BooleanAttribute(id, label, (Boolean) defaultValue, readonly, required, hidden, description, dependencies, validators);
	}

	public SelectableStringAttribute buildSelectableAttribute() {
		return new SelectableStringAttribute(id, label, (String) defaultValue, elementFilter, readonly, required, hidden, description, dependencies, validators);
	}

	public SelectableIntegerAttribute buildSelectableIntegerAttribute() {
		return new SelectableIntegerAttribute(id, label, (Integer) defaultValue, new HashMap<>(), readonly, required, hidden, description, dependencies, validators);
	}

	public ClassAttribute buildClassAttribute() {
		return new ClassAttribute(id, label, (Class<?>) defaultValue, readonly, required, hidden, description, dependencies, validators);
	}
	
	public IdAttribute buildIdAttribute() {
		return new IdAttribute(id, label, (String) defaultValue, readonly, true, hidden, description, dependencies, validators);
	}

	public <T> ListAttribute<T> buildListAttribute(Class<T> clasz) {
		return new ListAttribute<T>(id, label, clasz, (List<T>) defaultValue, readonly, required, hidden, description, dependencies, validators);
	}
}
