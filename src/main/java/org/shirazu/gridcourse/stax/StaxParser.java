package stax;

import gis.Attribute;
import gis.AttributeType;
import gis.Node;
import gis.Resource;
import gis.ResourceType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxParser {
	static final String NODE = "node";
	static final String NODEID = "nodeID";
	static final String NODENAME = "name";
	static final String RESOURCE = "resource";
	static final String RESOURCETYPE = "resourceType";
	static final String RESOURCENAME = "resourceName";
	static final String ATTRIBUTE = "attribute";
	static final String ATTRIBUTETYPE = "attributeType";
	static final String VALUE = "value";

	private boolean isStatic = false;

	@SuppressWarnings("unchecked")
	public Node readNode(String nodeFile) {
		ArrayList<Resource> resources = new ArrayList<Resource>();
		Node node = null;
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();

			InputStream inputStream = new FileInputStream(nodeFile);
			XMLEventReader eventReader = inputFactory
					.createXMLEventReader(inputStream);

			Resource resource = null;
			Attribute resourceAttribute = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();

					if (startElement.getName().getLocalPart() == (NODE)) {

						Iterator<javax.xml.stream.events.Attribute> xmlAttributes = startElement
								.getAttributes();
						while (xmlAttributes.hasNext()) {

							javax.xml.stream.events.Attribute attribute = xmlAttributes
									.next();

							if (attribute.getName().toString().equals(NODEID)) {
								if (Long.parseLong(attribute.getValue()) != 0) {
									node = Node.getNode(Long
											.parseLong(attribute.getValue()));
								} else {
									isStatic = true;
									node = new Node();
								}
							}

							if (attribute.getName().toString().equals(NODENAME)) {
								node.setName(attribute.getValue());
							}

						}

					}

					if (startElement.getName().getLocalPart() == (RESOURCE)) {

						resource = new Resource();

						Iterator<javax.xml.stream.events.Attribute> xmlAttributes = startElement
								.getAttributes();
						while (xmlAttributes.hasNext()) {

							javax.xml.stream.events.Attribute attribute = xmlAttributes
									.next();

							if (attribute.getName().toString()
									.equals(RESOURCENAME)) {
								resource.setName(attribute.getValue());
							}
						}

					}

					if (event.asStartElement().getName().getLocalPart()
							.equals(RESOURCETYPE)) {
						event = eventReader.nextEvent();
						resource.setResourceType(ResourceType.valueOf(event
								.asCharacters().getData()));
						continue;
					}

					if (event.isStartElement()) {
						startElement = event.asStartElement();
						if (startElement.getName().getLocalPart() == (ATTRIBUTE)) {
							resourceAttribute = new Attribute();
						}

						if (event.asStartElement().getName().getLocalPart()
								.equals(ATTRIBUTETYPE)) {
							event = eventReader.nextEvent();
							resourceAttribute.setAttributeType(AttributeType
									.valueOf(event.asCharacters().getData()));
							continue;
						}

						if (event.asStartElement().getName().getLocalPart()
								.equals(VALUE)) {
							event = eventReader.nextEvent();
							resourceAttribute.setValue(Double.parseDouble(event
									.asCharacters().getData()));
							continue;
						}
					}

				}

				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (NODE)) {
						if (isStatic) {
							node.setResources(resources);
							node.persist();
						} else {
							node.updateResources(resources);
						}
					}
				}

				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (RESOURCE)) {
						resources.add(resource);
					}
				}

				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (ATTRIBUTE)) {
						// set the date of the attribute
						resourceAttribute.setDate(new Date());
						resource.addAttribute(resourceAttribute);
					}
				}
			}

		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
		} catch (XMLStreamException e) {
			// e.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
		}

		return node;
	}
}