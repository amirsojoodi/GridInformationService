package stax;

import gis.Attribute;
import gis.Node;
import gis.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxWriter {
	private String nodeFile;

	public void setFile(String nodeFile) {
		this.nodeFile = nodeFile;
	}

	public void saveNode(Node node) {

		try {
			// create an XMLOutputFactory
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			// create XMLEventWriter
			XMLEventWriter eventWriter = outputFactory
					.createXMLEventWriter(new FileOutputStream(nodeFile));
			// create an EventFactory
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			// create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);

			// create config open tag
			StartElement configStartElement = eventFactory.createStartElement(
					"", "", "node");

			eventWriter.add(end);
			eventWriter.add(configStartElement);
			eventWriter.add(eventFactory.createAttribute("nodeID",
					String.valueOf(node.getId())));
			eventWriter
					.add(eventFactory.createAttribute("name", node.getName()));
			eventWriter.add(end);
			// Write the different nodes

			// createNode(eventWriter, "nodeID", String.valueOf(node.getId()),
			// 1);
			// createNode(eventWriter, "name", node.getName(), 1);

			for (Resource r : node.getResources()) {
				configStartElement = eventFactory.createStartElement("", "",
						"resource");

				eventWriter.add(tab);
				eventWriter.add(configStartElement);
				eventWriter.add(eventFactory.createAttribute("resourceName",
						r.getName()));
				eventWriter.add(end);
				createNode(eventWriter, "resourceType", r.getResourceType()
						.toString(), 2);
				for (Attribute attr : r.getAttributes()) {
					configStartElement = eventFactory.createStartElement("",
							"", "attribute");
					eventWriter.add(tab);
					eventWriter.add(tab);
					eventWriter.add(configStartElement);
					eventWriter.add(end);

					createNode(eventWriter, "attributeType", attr
							.getAttributeType().toString(), 3);
					createNode(eventWriter, "value",
							Double.toString(attr.getValue()), 3);

					eventWriter.add(tab);
					eventWriter.add(tab);
					eventWriter.add(eventFactory.createEndElement("", "",
							"attribute"));
					eventWriter.add(end);
				}

				eventWriter.add(tab);
				eventWriter.add(eventFactory.createEndElement("", "",
						"resource"));
				eventWriter.add(end);
			}
			eventWriter.add(eventFactory.createEndElement("", "", "node"));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
		} catch (XMLStreamException e) {
			// e.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
		}
	}

	private void createNode(XMLEventWriter eventWriter, String name,
			String value, int tabNum) throws XMLStreamException {

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		// create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		for (int i = 0; i < tabNum; i++) {
			eventWriter.add(tab);
		}
		eventWriter.add(sElement);
		// create Content
		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);
		// create End node
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

}