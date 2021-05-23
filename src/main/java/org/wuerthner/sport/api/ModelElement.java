package org.wuerthner.sport.api;

import org.wuerthner.sport.operation.Transaction;

import java.util.*;

public interface ModelElement {
	
	long getTechnicalId();
	
	void setTechnicalId(long id);

	String getId();

	String getFullId();

	public Comparator<ModelElement> getComparator();

	String getType();
	
	default String getCategory() {
		return getType();
	}
	
	void setType(String type);

	ModelElement getParent();
	
	<T> T getAttributeValue(Attribute<T> attribute);
	
	String getAttributeValue(String name);
	
	Attribute<?>[] getAttributes();

	Map<String, String> getAttributeMap();

	Map<String, Class<?>> getAttributeTypeMap();

	boolean isDeleted();

	void setDeleted(boolean deleted);

	boolean isInClipboard();
	
	void setInClipboad(boolean inClipboard);
	
	// internal operations
	
	List<ModelElement> getChildren();
	
	List<ModelElement> getChildrenByType(String type);

	List<ModelElement> getChildrenByCategory(String category);

	<T> List<T> getChildrenByClass(Class<T> clasz);

	// transient operations
	public void performTransientAddChildOperation(ModelElement element);
	
	public void performTransientRemoveChildOperation(ModelElement element);
	
	public <T> void performTransientSetAttributeValueOperation(Attribute<T> attribute, T value);

	public void performAddChildOperation(ModelElement child, History history);

	public void performRemoveChildOperation(ModelElement child, History history);

	public <T> void performSetAttributeValueOperation(Attribute<T> attribute, T value, History history);

	public void performTransaction(Transaction transaction, History history);

	public ModelElement forceAddChild(ModelElement child);

	public <T> void forceAddAttribute(String key, T value, Class<T> clasz);

	ModelElement getRoot();

	List<String> getChildTypes();

	List<String> getChildCategories();

	List<ModelElement> lookupByType(String type);
	
	List<ModelElement> lookupByType(String type, int depth);

	Optional<ModelElement> lookupByTypeAndId(String type, String id);

	Optional<ModelElement> lookupByTypeAndId(String type, String id, int depth);

	void unifyIds();

	void sort();

	void sort(Attribute<?> attribute);

	// ----------------------------------------------------
	
	static String make(char c, int size) {
		String result = new String(new char[size]).replace('\0', c);
		return result;
	}
}
