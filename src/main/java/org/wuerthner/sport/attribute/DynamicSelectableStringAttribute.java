package org.wuerthner.sport.attribute;

import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.attributetype.DynamicMapping;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.check.True;
import org.wuerthner.sport.core.ElementFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicSelectableStringAttribute extends AbstractAttribute<String, DynamicSelectableStringAttribute, DynamicMapping> implements DynamicMapping {
	private ElementFilter elementFilter;
	private String rootType;

	public DynamicSelectableStringAttribute(String name) {
		super(name, String.class, DynamicMapping.class);
	}

	public DynamicSelectableStringAttribute addFilter(ElementFilter elementFilter) {
		this.elementFilter = elementFilter;
		return this;
	}

	@Override
	public ElementFilter getElementFilter() { return elementFilter; }

	@Override
	public Map<String, Object> getValueMap(ModelElement selectedElement) {
		ModelElement root = selectedElement.getRoot();
		List<ModelElement> resultList = root.lookupByType(elementFilter.type)
				.stream()
				.filter(el -> elementFilter.filter.evaluate(el, null))
				.collect(Collectors.toList());

		Map<String, Object> map = new HashMap<>();
		for (ModelElement el : resultList) {
			map.put(el.getAttributeValue("id"), el.getAttributeValue("id"));
		}
		return map;
	}

	@Override
	public String getValue(String stringValue) {
		return stringValue;
	}
}
