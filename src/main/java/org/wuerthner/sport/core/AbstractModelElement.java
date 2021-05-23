package org.wuerthner.sport.core;

import java.util.*;
import java.util.stream.Collectors;

import org.wuerthner.sport.api.*;
import org.wuerthner.sport.operation.AddChildOperation;
import org.wuerthner.sport.operation.RemoveChildOperation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

public class AbstractModelElement implements ModelElement {
	
	private final List<ModelElement> children;
	private final List<String> childTypes;
	private final List<Attribute<?>> attributes = new ArrayList<>();
	
	private Map<String, String> attributeMap = new HashMap<>();
	private long id;
	private String type;
	private boolean deleted = false;
	private boolean inClipboard = false;
	private ModelElement parent;

	public AbstractModelElement(String type, List<String> childTypes, List<Attribute<?>> attributes) {
		this.type = type;
		this.parent = this;
		this.children = new ArrayList<>();
		this.childTypes = childTypes;
		this.attributes.addAll(attributes);
		for (Attribute<?> attribute : attributes) {
			if (attribute.hasDefaultValue()) {
				attributeMap.put(attribute.getName(), attribute.getDefaultValue().get().toString());
			}
		}
		this.id = 0;
	}
	
	@Override
	public long getTechnicalId() {
		return id;
	}
	
	@Override
	public void setTechnicalId(long id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return ""+hashCode();
	}

	@Override
	public String getFullId() {
		if (this.equals(getParent())) {
			return "" + getId();
		} else {
			return getParent().getFullId() + "." + getId();
		}
	}

	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public ModelElement getParent() {
		return parent;
	}
	
	@Override
	public <T> T getAttributeValue(Attribute<T> attribute) {
		T value;
		String key = attribute.getName();
		String stringValue = attributeMap.get(key);
		value = attribute.getValue(stringValue);
		return value;
	}
	
	@Override
	public String getAttributeValue(String name) {
		String result = null;
		for (Attribute<?> attribute : attributes) {
			if (attribute.getName().equals(name)) {
				Object attributeValue = getAttributeValue(attribute);
				result = attributeValue == null ? "" : attributeValue.toString();
				break;
			}
		}
		return result;
	}
	
	@Override
	public Attribute<?>[] getAttributes() {
		return attributes.toArray(new Attribute<?>[] {});
	}
	
	@Override
	public boolean isDeleted() {
		return deleted;
	}
	
	@Override
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public boolean isInClipboard() {
		return inClipboard;
	}
	
	@Override
	public void setInClipboad(boolean inClipboard) {
		this.inClipboard = inClipboard;
	}
	
	@Override
	public List<ModelElement> getChildren() {
		return children;
	}

	@Override
	public List<ModelElement> getChildrenByType(String type) {
		return children.stream().filter(el -> el.getType().equals(type)).collect(Collectors.toList());
	}

	@Override
	public List<ModelElement> getChildrenByCategory(String category) {
		return children.stream().filter(el -> el.getCategory().equals(category)).collect(Collectors.toList());
	}

	@Override
	public <T> List<T> getChildrenByClass(Class<T> clasz) {
		List<T> theList = children.stream().filter(ge -> clasz.isAssignableFrom(ge.getClass()))
				.map(child -> (T) child)
				.collect(Collectors.toList());
		return theList;
	}

	@Override
	public void performTransientAddChildOperation(ModelElement child) {
		synchronized (child) {
			Operation operation = new AddChildOperation(this, child);
			this.performTransientOperation(operation);
		}
	}
	
	@Override
	public void performTransientRemoveChildOperation(ModelElement child) {
		synchronized (child) {
			Operation operation = new RemoveChildOperation(child);
			this.performTransientOperation(operation);
		}
	}
	
	@Override
	public <T> void performTransientSetAttributeValueOperation(Attribute<T> attribute, T value) {
		synchronized (this) {
			Operation operation = new SetAttributeValueOperation<T>(this, attribute, value);
			this.performTransientOperation(operation);
			Comparator comparator = getComparator();
		}
	}

	@Override
	public void performAddChildOperation(ModelElement child, History history) {
		synchronized (child) {
			Operation operation = new AddChildOperation(this, child);
			synchronized (history) {
				history.add(operation);
				this.performTransientOperation(operation);
			}
		}
	}

	@Override
	public void performRemoveChildOperation(ModelElement child, History history) {
		synchronized (child) {
			Operation operation = new RemoveChildOperation(child);
			synchronized (history) {
				history.add(operation);
				this.performTransientOperation(operation);
			}
		}
	}

	@Override
	public <T> void performSetAttributeValueOperation(Attribute<T> attribute, T value, History history) {
		Operation operation = new SetAttributeValueOperation<T>(this, attribute, value);
		synchronized (history) {
			history.add(operation);
			this.performTransientOperation(operation);
		}
	}

	@Override
	public void performTransaction(Transaction transaction, History history) {
		synchronized (history) {
			history.add(transaction);
			this.performTransientOperation(transaction);
		}
	}

	@Override
	public void sort() {
		Collections.sort(children, getComparator());
	}

	@Override
	public void sort(Attribute<?> attribute) {
		Comparator comparator = getComparator();
		if (!(comparator instanceof ModelElementComparator)
				|| !((ModelElementComparator)comparator).hasAttribute()
				|| ((ModelElementComparator)comparator).getAttributeName().equals(attribute.getName())) {
			Collections.sort(children, comparator);
		}
	}

	@Override
	public ModelElement getRoot() {
		ModelElement element = this;
		while (element.getParent() != element) {
			element = element.getParent();
		}
		return element;
	}
	
	@Override
	public List<String> getChildTypes() {
		return childTypes;
	}

	@Override
	public List<String> getChildCategories() {
		return new ArrayList<>(children.stream().map(el -> el.getCategory()).collect(Collectors.toSet()));
	}

	// internal methods
	private boolean acceptsChild(ModelElement element) {
		return (childTypes.contains(element.getType()));
	}

	public AbstractModelElement addChild(ModelElement child) {
		if (acceptsChild(child)) {
			children.add(child);
			((AbstractModelElement) child).setParent(this);
			return this;
		} else {
			throw new RuntimeException("Invalid attempt to add child of type: " + child.getType() + " to parent of type: " + this.getType());
		}
	}
	@Override
	public ModelElement forceAddChild(ModelElement child) {
		children.add(child);
		((AbstractModelElement) child).setParent(this);
		return this;
	}

	public <T> void forceAddAttribute(String key, T value, Class<T> clasz) {
		Optional<Attribute<?>> optionalAttribute = lookupAttribute(key);
		Attribute<T> attribute = (Attribute<T>) optionalAttribute.get();
		String stringPresentation = attribute.getStringPresentation(value);
		attributeMap.put(key, stringPresentation);
	}

	public void removeChild(ModelElement child) {
		boolean ok = children.remove(child);
		((AbstractModelElement) child).removeParent();
	}

	public <T> void setAttributeValue(Attribute<T> attribute, T value) {
		String key = attribute.getName();
		if (value == null || value.equals("")) {
			attributeMap.remove(key);
		} else {
			verifyAttribute(attribute, value);
			String stringPresentation = attribute.getStringPresentation(value);
			attributeMap.put(key, stringPresentation);
		}
	}
	
	public void setAttributeValue(String key, String value) {
		attributeMap.put(key, value);
	}

	@Override
	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	@Override
	public Map<String, Class<?>> getAttributeTypeMap() {
		Map<String, Class<?>> map = new HashMap<>();
		for (Attribute<?> attribute : this.getAttributes()) {
			map.put(attribute.getName(), attribute.getAttributeType());
		}
		return map;
	}
	
	public void setAttributeMap(Map<String, String> attributeMap) {
		for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
			verifyAttribute(entry.getKey(), entry.getValue());
		}
		this.attributeMap = attributeMap;
	}
	
	private void performTransientOperation(Operation operation) {
		operation.execute();
	}
	
	private void setParent(ModelElement modelElement) {
		parent = modelElement;
	}
	
	private void removeParent() {
		parent = this;
	}
	
	private <T> void verifyAttribute(Attribute<T> attribute, T value) {
		Optional<Attribute<?>> optionalAttribute = lookupAttribute(attribute.getName());
		if (optionalAttribute.isPresent()) {
			if (value != null) {
				if (!optionalAttribute.get().getAttributeType().isAssignableFrom(value.getClass())) {
					throw new RuntimeException("Value '" + value + "' cannot be assigned to attribute '" + attribute.getName() + "' of type " + attribute.getAttributeType());
				}
			}
		} else {
			throw new RuntimeException("Attribute '" + attribute.getName() + "' does not exist in element '" + this.getId() + ":" + this.getType());
		}
	}
	
	private void verifyAttribute(String key, String value) {
		Optional<Attribute<?>> optionalAttribute = lookupAttribute(key);
		if (optionalAttribute.isPresent()) {
			if (value != null) {
				try {
					optionalAttribute.get().getValue(value);
				} catch (Exception e) {
					throw new RuntimeException("Value '" + value + "' cannot be assigned to attribute '" + key + "' of type " + optionalAttribute.get().getAttributeType());
				}
			}
		} else {
			throw new RuntimeException("Attribute '" + key + "' does not exist in element '" + this.getId() + ":" + this.getType());
		}
	}
	
	private Optional<Attribute<?>> lookupAttribute(String name) {
		Attribute<?> result = null;
		for (Attribute<?> attr : attributes) {
			if (attr.getName().equals(name)) {
				result = attr;
				break;
			}
		}
		return Optional.ofNullable(result);
	}
	
	@Override
	public List<ModelElement> lookupByType(String type) {
		return lookupByType(type, -1);
	}
	
	@Override
	public List<ModelElement> lookupByType(String type, int depth) {
		List<ModelElement> list = new ArrayList<>();
		if (this.getType().equals(type)) {
			list.add(this);
		}
		if (depth != 0) {
			for (ModelElement child : children) {
				List<ModelElement> lookup = child.lookupByType(type, depth - 1);
				list.addAll(lookup);
			}
		}
		return list;
	}

	@Override
	public Optional<ModelElement> lookupByTypeAndId(String type, String id) {
		return lookupByTypeAndId(type, id, -1);
	}

	@Override
	public Optional<ModelElement> lookupByTypeAndId(String type, String id, int depth) {
		Optional<ModelElement> optional = Optional.empty();
		if (this.getType().equals(type) && this.getId().equals(id)) {
			optional = Optional.of(this);
		} else {
			if (depth != 0) {
				for (ModelElement child : children) {
					optional = child.lookupByTypeAndId(type, id, depth - 1);
					if (optional.isPresent()) {
						break;
					}
				}
			}
		}
		return optional;
	}

	public void unifyIds() {
		long offset = 1000;
		setUniqueId(this, offset);
	}

	public Comparator<ModelElement> getComparator() {
		return ModelElementComparator.DEFAULT;
	}

	private long setUniqueId(ModelElement element, long id) {
		element.setTechnicalId(id++);
		for (ModelElement child : element.getChildren()) {
			id = setUniqueId(child, id);
		}
		return id;
	}
}
