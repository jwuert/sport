package org.wuerthner.sport.api;

import org.wuerthner.sport.operation.Transaction;

import java.util.*;

public interface ModelElement {

	void addAttribute(Attribute<?> attribute);

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

	ModelElement getReference();

	public void setReference(ModelElement reference);

	<T> T getAttributeValue(Attribute<T> attribute);

	<T> T getAttributeValue(Attribute<T> attribute, T defaultValue);

	String getAttributeValue(String name);
	
	Attribute<?>[] getAttributes();

	Map<String, String> getAttributeMap();

	Map<String, Class<?>> getAttributeTypeMap();

	public Attribute<?> getAttribute(String name);

	boolean isDeleted();

	void setDeleted(boolean deleted);

	boolean isInClipboard();
	
	void setInClipboad(boolean inClipboard);
	
	// internal operations
	
	List<ModelElement> getChildren();
	
	List<ModelElement> getChildrenByType(String type);

	List<ModelElement> getChildrenByCategory(String category);

	<T> List<T> getChildrenByClass(Class<T> clasz);

	default List<Attribute<?>> getCategoryAttributes() {
		return new ArrayList<>();
	}

	// transient operations

	public void performTransientAddChildOperation(ModelElement element);
	
	public void performTransientRemoveChildOperation(ModelElement element);
	
	public <T> void performTransientSetAttributeValueOperation(Attribute<T> attribute, T value);

	public void performTransientCutToClipboardOperation(Clipboard clipboard, List<? extends ModelElement> elementList);

	public void performTransientCopyToClipboardOperation(Clipboard clipboard, List<? extends ModelElement> elementList, ModelElementFactory factory);

	public void performTransientPasteClipboardOperation(Clipboard clipboard, ModelElementFactory factory);

	public <Element extends ModelElement> void performTransientModifyPasteClipboardOperation(Clipboard clipboard, ModelElementFactory factory, Modifier<Element> modifier);

	public <Element extends ModelElement> void performTransientModifyPasteClipboardToReferenceOperation(Clipboard clipboard, ModelElementFactory factory, Modifier<Element> modifier);

	// non-transient operations

	public void performAddChildOperation(ModelElement child, History history);

	public void performRemoveChildOperation(ModelElement child, History history);

	public <T> void performSetAttributeValueOperation(Attribute<T> attribute, T value, History history);

	public void performCutToClipboardOperation(Clipboard clipboard, List<? extends ModelElement> elementList, History history);

	public void performCopyToClipboardOperation(Clipboard clipboard, List<? extends ModelElement> elementList, ModelElementFactory factory, History history);

	public void performPasteClipboardOperation(Clipboard clipboard, ModelElementFactory factory, History history);

	public <Element extends ModelElement> void performModifyPasteClipboardOperation(Clipboard clipboard, ModelElementFactory factory, History history, Modifier<Element> modifier);

	public <Element extends ModelElement> void performModifyPasteClipboardToReferenceOperation(Clipboard clipboard, ModelElementFactory factory, History history, Modifier<Element> modifier);

	public void performTransaction(Transaction transaction, History history);

	public ModelElement forceAddChild(ModelElement child);

	public <T> void forceAddAttribute(String key, String value, Class<T> clasz);

	ModelElement getRoot();

	List<String> getChildTypes();

	List<String> getChildCategories();

	List<ModelElement> lookupByType(String type);
	
	List<ModelElement> lookupByType(String type, int depth);

	Optional<ModelElement> lookupByTypeAndId(String type, String id);

	Optional<ModelElement> lookupByTypeAndId(String type, String id, int depth);

	public Optional<ModelElement> lookupByFullId(String fid);

//	public Optional<ModelElement> lookupByTechnicalId(long id);
//
//	public Optional<ModelElement> lookupByTechnicalId(long id, int depth);

	void unifyIds();

	void sort();

	void sort(Attribute<?> attribute);

	Date getModified();

	void setModified(Date modified);

	long getModifiedBy();

	void setModifiedBy(long modifiedBy);

	Date getCreated();

	void setCreated(Date created);

	long getCreatedBy();

	void setCreatedBy(long createdBy);

	default List<String> getDescription() {
		return new ArrayList<>();
	}

	Map<Attribute<?>, List<Check>> getDependencies();

	Map<Attribute<?>, List<Check>> getValidatorMap();

	// ----------------------------------------------------
	
	static String make(char c, int size) {
		String result = new String(new char[size]).replace('\0', c);
		return result;
	}
}
