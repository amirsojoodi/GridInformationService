package org.shirazu.gridcourse.gui;

//============================================================================
//Author      : Fatemeh Marzban, Amir Hossein Sojoodi
//Version     : 0.0.1
//Year        : 2014
//Copyright   : GNU
//Description : Grid Information Service in Java
//============================================================================

import org.shirazu.gridcourse.gis.Attribute;
import org.shirazu.gridcourse.gis.Resource;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class NodeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private long nodeID;
	private JFrame nodeFrame;
	private EntityManagerFactory emf;
	private EntityManager em;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public NodeFrame() {
		nodeID = 0;
		do {
			nodeID = Long.parseLong(JOptionPane
					.showInputDialog("Enter Node's ID: "));
		} while (nodeID == 0);

		nodeFrame = new JFrame();
		nodeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		emf = Persistence.createEntityManagerFactory("$objectdb/db/GIS.odb");
		em = emf.createEntityManager();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@SuppressWarnings("unchecked")
	public void createAndShowNodeFrame() {

		List<Resource> resources;
		em.getTransaction().begin();
		resources = em.createQuery(
				"SELECT r FROM Resource r WHERE r.node.id = " + nodeID)
				.getResultList();
		em.getTransaction().commit();

		@SuppressWarnings("rawtypes")
		Vector<Vector> rowData = new Vector<Vector>();
		Vector<String> columnNames = new Vector<String>();

		columnNames.addElement("Resource Name");
		columnNames.addElement("Resource Type");
		columnNames.addElement("Attribute Type");
		columnNames.addElement("Attribute Value");
		columnNames.addElement("Insertion Date");

		for (Resource resource : resources) {

			Vector<String> row = new Vector<String>();

			row.addElement(resource.getName());
			row.addElement(resource.getResourceType().toString());
			row.addElement("");
			row.addElement("");
			rowData.addElement(row);

			String resourceName = resource.getName();
			List<Attribute> attributes;

			em.getTransaction().begin();
			attributes = em.createQuery(
					"SELECT a FROM Attribute a WHERE a.resource.node.id = "
							+ nodeID + " AND a.resource.name = '"
							+ resourceName + "'").getResultList();
			em.getTransaction().commit();

			for (Attribute attribute : attributes) {
				Vector<String> row1 = new Vector<String>();
				row1.addElement("");
				row1.addElement("—→");
				row1.addElement(attribute.getAttributeType().toString());
				row1.addElement(String.valueOf(attribute.getValue()));
				row1.addElement(String.valueOf(attribute.getDate()));
				rowData.addElement(row1);
			}

		}
		JTable table = new JTable(rowData, columnNames);
		table.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new CellRenderer());

		JScrollPane scrollPane = new JScrollPane(table);
		nodeFrame.add(scrollPane, BorderLayout.CENTER);
		nodeFrame.setMinimumSize(new Dimension(800, 400));
		nodeFrame.setLocation(400, 100);
		nodeFrame.setVisible(true);
	}
}

class CellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		if (column < 2) {
			c.setBackground(new Color(255, 247, 137));
		} else if (column > 1) {
			c.setForeground(new Color(166, 73, 73));
		}
		return c;
	}
}
