package org.wuerthner.sport.api;

import java.util.Map;

public interface ModelElementFactory {
	public  <T extends ModelElement> T createElement(String typeName);
	
	default <T> ModelElement copyTree(ModelElement original) {
		ModelElement copy = this.createElement(original.getType());
		for (Map.Entry<String,String> entry : original.getAttributeMap().entrySet()) {
			String key = entry.getKey();
			Class<T> clasz = (Class<T>) original.getAttributeTypeMap().get(key);
			T value = (T) entry.getValue();
			copy.forceAddAttribute(key, entry.getValue(), clasz);
			// copy.forceAddAttribute(key, value, clasz);
		}
		for (ModelElement child : original.getChildren()) {
			ModelElement childCopy = copyTree(child);
			copy.forceAddChild(childCopy);
		}
		return copy;
	}
}
