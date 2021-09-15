package org.wuerthner.sport.core;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;

public class XMLReader {
	private final String rootElementName;
	private final String rootType;
	private final ModelElementFactory factory;
	private final double defaultVersion;
	FAModel faModel = null;

	public XMLReader(ModelElementFactory factory, String rootType, String rootElementName) {
		this.factory = factory;
		this.rootType = rootType;
		this.rootElementName = rootElementName;
		this.defaultVersion = 1.0;
	}
	
	public ModelElement run(InputStream inputStream) {
		ModelElement root = null;
		boolean validRoot = false;
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream, "US-ASCII");
			// read the XML document
			Map<Integer, ElementDef> elementDefMap = new HashMap<>();
			
			ElementDef currentDef = null;
			Attr currentAttr = null;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				
				if (event.isStartElement()) {
					
					StartElement startElement = event.asStartElement();
					String elementName = startElement.getName().getLocalPart();
					if (elementName.endsWith("Def")) {
						ElementDef elementDef = new ElementDef(startElement);
						elementDefMap.put(elementDef.id, elementDef);
						currentDef = elementDef;
						currentAttr = null;
					} else if (elementName.endsWith("Ref")) {
						ElementRef elementRef = new ElementRef(startElement);
						currentDef.add(elementRef);
						currentAttr = null;
					} else if (elementName.equals("Attribute")) {
						try {
							Attr attr = new Attr(startElement);
							currentDef.add(attr);
							currentAttr = attr;
						} catch (ClassNotFoundException e) {
							throw new RuntimeException("Invalid element: '" + elementName + "'");
						}
					} else if (elementName.equals(rootElementName)) {
						validRoot = true;
						faModel = new FAModel(startElement);
					} else {
						throw new RuntimeException("Invalid element: '" + elementName + "'");
					}
				}
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					String elementName = endElement.getName().getLocalPart();
					if (elementName.endsWith("Def")) {
						currentDef = null;
						currentAttr = null;
					}
				}
				if (event.isCharacters()) {
					if (currentAttr != null) {
						currentAttr.addValue(event.asCharacters().getData());
					}
				}
			}
			if (!validRoot) {
				throw new RuntimeException("Invalid study model - root element '" + rootElementName + "' missing!");
			}
			
			Map<Integer, ModelElement> elementMap = new HashMap<>();
			for (Map.Entry<Integer, ElementDef> entry : elementDefMap.entrySet()) {
				int id = entry.getKey();
				ElementDef elementDef = entry.getValue();
				String type = elementDef.type;
				int order = elementDef.order;
				type = shorten(type);
				ModelElement element = factory.createElement(type);
				for (Attr attr : elementDef.attrList) {
					((AbstractModelElement) element).setAttributeValue(attr.key.trim(), attr.value.trim());
				}
				elementMap.put(id, element);
				if (type.equals(rootType)) {
					root = element;
				}
			}
			for (ElementDef elementDef : elementDefMap.values()) {
				for (ElementRef elementRef : elementDef.elementRefList) {
					int defId = elementDef.id;
					int refId = elementRef.id;
					ModelElement parent = elementMap.get(defId);
					ModelElement child = elementMap.get(refId);
					if (child == null) {
						throw new RuntimeException("Child " + refId + " not found!");
					}
					((AbstractModelElement) parent).addChild(child);
				}
			}
			
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		
		return root;
	}

	private String shorten(String type) {
		int index = type.lastIndexOf('.');
		if (index<0) {
			return type;
		} else {
			return type.substring(index+1);
		}
	}

	public String getTimestamp() {
		if (faModel == null) {
			throw new RuntimeException("FA Model not available!");
		}
		return faModel.timestamp;
	}
	
	public double getVersion() {
		if (faModel == null) {
			throw new RuntimeException("FA Model not available!");
		}
		return faModel.version;
	}

	private class ElementDef {
		final int id;
		final String type;
		final int order;
		final List<Attr> attrList = new ArrayList<>();
		final List<ElementRef> elementRefList = new ArrayList<>();
		
		public ElementDef(StartElement startElement) {
			@SuppressWarnings("unchecked")
			Iterator<Attribute> attributes = startElement.getAttributes();
			int idAttribute = -1;
			String typeAttribute = null;
			int orderAttribute = -1;
			while (attributes.hasNext()) {
				Attribute attribute = attributes.next();
				if (attribute.getName().getLocalPart().equalsIgnoreCase("id")) {
					idAttribute = Integer.valueOf(attribute.getValue());
				} else if (attribute.getName().getLocalPart().equalsIgnoreCase("type")) {
					typeAttribute = attribute.getValue();
				} else if (attribute.getName().getLocalPart().equalsIgnoreCase("order")) {
					orderAttribute = Integer.valueOf(attribute.getValue());
				} else {
					throw new RuntimeException("Unknown attribute: '" + attribute.getName() + "'");
				}
			}
			if (idAttribute < 0) {
				throw new RuntimeException("Missing id attribute for: '" + startElement.getName().getLocalPart() + "'");
			} else if (typeAttribute == null) {
				throw new RuntimeException("Missing type attribute for: '" + startElement.getName().getLocalPart() + "'");
			}
			this.id = idAttribute;
			this.type = typeAttribute;
			this.order = orderAttribute;
		}
		
		void add(Attr attr) {
			attrList.add(attr);
		}
		
		void add(ElementRef elementRef) {
			elementRefList.add(elementRef);
		}
	}
	
	private class ElementRef {
		final int id;
		
		public ElementRef(StartElement startElement) {
			@SuppressWarnings("unchecked")
			Iterator<Attribute> attributes = startElement.getAttributes();
			int idAttribute = -1;
			while (attributes.hasNext()) {
				Attribute attribute = attributes.next();
				if (attribute.getName().getLocalPart().equalsIgnoreCase("id")) {
					idAttribute = Integer.valueOf(attribute.getValue());
				} else {
					throw new RuntimeException("Unknown attribute: " + attribute.getName());
				}
			}
			if (idAttribute < 0) {
				throw new RuntimeException("Missing id attribute for: " + startElement.getName().getLocalPart());
			}
			this.id = idAttribute;
		}
	}
	
	private class FAModel {
		final double version;
		final String timestamp;
		
		public FAModel(StartElement startElement) {
			@SuppressWarnings("unchecked")
			Iterator<Attribute> attributes = startElement.getAttributes();
			double versionAttribute = -1;
			String timestampAttribute = "";
			while (attributes.hasNext()) {
				Attribute attribute = attributes.next();
				if (attribute.getName().getLocalPart().equalsIgnoreCase("version")) {
					versionAttribute = Double.valueOf(attribute.getValue());
				} else if (attribute.getName().getLocalPart().equalsIgnoreCase("timestamp")) {
					timestampAttribute = attribute.getValue();
				} else {
					throw new RuntimeException("Unknown attribute: " + attribute.getName());
				}
			}
			if (versionAttribute < 0) {
				if (defaultVersion>=0) {
					versionAttribute = defaultVersion;
				} else {
					throw new RuntimeException("Missing version attribute for: " + startElement.getName().getLocalPart());
				}
			}
			if (timestampAttribute == null || timestampAttribute.equals("")) {
				timestampAttribute = new SimpleDateFormat(XMLWriter.timestampFormat).format(new Date());
				// throw new RuntimeException("Missing timestamp attribute for: " + startElement.getName().getLocalPart());
			}
			this.version = versionAttribute;
			this.timestamp = timestampAttribute;
		}
	}
	
	private class Attr {
		final String key;
		String value;
		Class<?> type;
		
		public Attr(StartElement startElement) throws ClassNotFoundException {
			@SuppressWarnings("unchecked")
			Iterator<Attribute> attributes = startElement.getAttributes();
			String keyAttribute = null;
			String typeAttribute = null;
			String valueAttribute = "";
			while (attributes.hasNext()) {
				Attribute attribute = attributes.next();
				if (attribute.getName().getLocalPart().equalsIgnoreCase("key")) {
					keyAttribute = attribute.getValue();
				} else if (attribute.getName().getLocalPart().equalsIgnoreCase("type")) {
					typeAttribute = attribute.getValue();
				} else if (attribute.getName().getLocalPart().equalsIgnoreCase("value")) {
					valueAttribute = attribute.getValue();
				} else {
					throw new RuntimeException("Unknown attribute: " + attribute.getName());
				}
			}
			if (keyAttribute == null) {
				throw new RuntimeException("Missing key attribute for: " + startElement.getName().getLocalPart());
			}
//			if (typeAttribute == null) {
//				throw new RuntimeException("Missing type attribute for: " + startElement.getName().getLocalPart());
//			}
// 			this.type = Class.forName(typeAttribute);
			this.key = keyAttribute;
			this.value = valueAttribute;
		}
		
		public void addValue(String data) {
			this.value += data.replaceAll("\n", "");
		}
	}
}
