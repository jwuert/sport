package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectableStringAttribute extends AbstractAttribute<String>{
	private final ElementFilter elementFilter;
	private String rootType;
	
	public SelectableStringAttribute(String name, String label, String defaultValue, ElementFilter elementFilter, boolean readonly, boolean required, boolean hidden, String description,
			List<Check> dependencies, List<Check> validators) {
		super(name, label, String.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
		this.elementFilter = elementFilter;
	}
	
	public Map<String, String> getValueMap() {
		Map<String, String> map = new HashMap<>();
		return map;
	}
	
	public String getElementFilterType() {
		return (elementFilter != null ? elementFilter.type : "");
	}
	
	// public Check getElementFilterCheck() { return (elementFilter != null ? elementFilter.filter : new True()); }
	
	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}
	
	public static class ElementFilter {
		public final String type;
		public final Check filter;
		
		public ElementFilter(String type, Check filter) {
			this.type = type;
			this.filter = filter;
		}
	}
}
