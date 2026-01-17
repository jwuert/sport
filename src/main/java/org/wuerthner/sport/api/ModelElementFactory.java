package org.wuerthner.sport.api;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ModelElementFactory {
	public  <T extends ModelElement> T createElement(String typeName);

	public List<ModelElement> createElementList(UserProvider userProvider);
	
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

	default String getRootElementType() {
		return null;
	}

	default ActionProvider getActionProvider() {
		return null;
	}

	default Optional<History> getHistory() {
		return Optional.empty();
	}

	default Optional<Clipboard> getClipboard() {
		return Optional.empty();
	}

	default Optional<Executor<File>> getPdfViewerExecutor() { return Optional.empty(); }

	default String getFileExtension() { return "sxml"; }

	default String getAppName() { return ""; }

    public Map<String,String> getUserMap();
}
