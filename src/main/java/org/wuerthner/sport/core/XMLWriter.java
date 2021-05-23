package org.wuerthner.sport.core;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.wuerthner.sport.api.ModelElement;

public class XMLWriter {
	private final static String timestampFormat = "yyyy-MM-dd HH:mm:ss";
	
	public void run(ModelElement element, OutputStream outputStream) {
		try {
			if (outputStream == null) {
				throw new NullPointerException("OutputStream must not be null!");
			}
			element.unifyIds();

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document document = docBuilder.newDocument();
			Element rootElement = document.createElement("Model");
			addAttribute("version", "1.0", rootElement, document);
			addAttribute("timestamp", createTimeStamp(), rootElement, document);
			document.appendChild(rootElement);
			
			writeElement(element, rootElement, document);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "US-ASCII");
			DOMSource source = new DOMSource(document);
			
			StreamResult result = new StreamResult(outputStream);
			
			transformer.transform(source, result);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void writeElement(ModelElement element, Element parent, Document document) {
		String parentType = element.getType();
		Element elementDef = document.createElement(parentType + "Def");
		parent.appendChild(elementDef);
		
		addAttribute("type", parentType, elementDef, document);
		addAttribute("id", String.valueOf(element.getTechnicalId()), elementDef, document);

		// Attributes:
		for (Map.Entry<String, String> entry : element.getAttributeMap().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			Class<?> type = element.getAttributeTypeMap().get(key);
			Element attributeElement = document.createElement("Attribute");
			elementDef.appendChild(attributeElement);
			addAttribute("key", key, attributeElement, document);
			addAttribute("type", type.getName(), attributeElement, document);
			addContent("value", value, attributeElement, document);
		}
		
		// children:
		for (ModelElement child : element.getChildren()) {
			writeElement(child, parent, document);
			
			String childType = child.getType();
			Element elementRef = document.createElement(childType + "Ref");
			elementDef.appendChild(elementRef);
			addAttribute("id", String.valueOf(child.getTechnicalId()), elementRef, document);
		}
	}
	
	private void addAttribute(String attributeName, String attributeValue, Element xmlElement, Document document) {
		Attr attribute = document.createAttribute(attributeName);
		attribute.setValue(attributeValue);
		xmlElement.setAttributeNode(attribute);
	}
	
	private void addContent(String attributeName, String attributeValue, Element xmlElement, Document document) {
		Text node = document.createTextNode(attributeValue);
		xmlElement.appendChild(node);
	}
	
	private String createTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat(timestampFormat, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String ts = sdf.format(new Date());
		return ts;
	}
}
