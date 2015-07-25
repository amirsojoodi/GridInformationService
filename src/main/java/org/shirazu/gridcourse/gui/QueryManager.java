package gui;

import gis.AttributeType;
import gis.Node;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class QueryManager extends JFrame {

	private static final long serialVersionUID = 1L;

	private int reportDurationMinutes;

	private Date pivotDate;
	private int intervalseconds;

	private JMenuBar menuBar1;
	private JMenu HelpMenu;
	private JMenuItem Help_AboutMenuItem;
	private JMenu toolsMenu;
	private JMenuItem tools_GraphMenuItem;
	private JPanel panel1;
	private JButton CompleteNodeInfoButton;
	private JLabel ReportTimeLabel;
	private JScrollPane scrollPane1;
	private JTable table1;

	private JFrame mainFrame;
	NumberFormat formatter = new DecimalFormat("#0.00");

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public QueryManager(int seconds) {

		mainFrame = new JFrame();

		intervalseconds = seconds;
		menuBar1 = new JMenuBar();
		HelpMenu = new JMenu();
		Help_AboutMenuItem = new JMenuItem();
		toolsMenu = new JMenu();
		tools_GraphMenuItem = new JMenuItem();
		panel1 = new JPanel();
		CompleteNodeInfoButton = new JButton();
		ReportTimeLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();

		// ======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout("default, $lcgap, default",
				"2*(default, $lgap), default"));

		// ======== menuBar1 ========
		{

			// ======== HelpMenu ========
			{
				HelpMenu.setText("Help");

				// ---- Help_AboutMenuItem ----
				Help_AboutMenuItem.setText("About");
				Help_AboutMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Help_AboutMenuItemActionPerformed(e);
					}
				});
				HelpMenu.add(Help_AboutMenuItem);
			}
			menuBar1.add(HelpMenu);
			// ======== ToolsMenu ========
			{
				toolsMenu.setText("Tools");

				// ---- tools_GraphMenuItem ----
				tools_GraphMenuItem.setText("Monitoring Graph");
				tools_GraphMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tools_GraphMenuItemActionPerformed(e);
					}
				});
				toolsMenu.add(tools_GraphMenuItem);
			}
			menuBar1.add(toolsMenu);

		}
		mainFrame.setJMenuBar(menuBar1);

		// ======== panel1 ========
		{

			panel1.setLayout(new FormLayout("default, $lcgap, default",
					"2*(default, $lgap), default"));

			// ---- CompleteNodeInfoButton ----
			CompleteNodeInfoButton.setText("Complete Node Information");
			CompleteNodeInfoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nodeButtonActionPerformed(e);
				}
			});
			panel1.add(CompleteNodeInfoButton, CC.xy(1, 3));

			// ---- StartTimeLabel ----
			ReportTimeLabel.setText("");
			panel1.add(ReportTimeLabel, CC.xy(3, 3));
		}
		contentPane.add(panel1, CC.xy(3, 1));

		// ======== scrollPane1 ========
		{
			table1 = getAllNodes();
			table1.setRowHeight(20);
			table1.setEnabled(false);
			scrollPane1.setViewportView(table1);
		}
		contentPane.add(scrollPane1, CC.xy(3, 5));
		setLocationRelativeTo(getOwner());
		mainFrame.add(contentPane);

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			QueryManager queryManager = new QueryManager(5);

			public void run() {
				queryManager.createAndShowQueryManager();
			}
		});

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@SuppressWarnings("unchecked")
	public JTable getAllNodes() {

		List<Node> nodes;
		Date startDate;
		double cpuUtilizationAvg;
		double ramUtilizationAvg = 0;
		long nodeID;

		long realReportCount = 0;
		long expectedReportCount = 0;
		long timeOfNow;

		double upTimePercent;
		double downTimePercent;

		double upTimeSeconds;
		double downTimeSeconds;

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("$objectdb/db/GIS.odb");
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		String queryStatement = "SELECT MIN(a.date) FROM Attribute a WHERE "
				+ "a.attributeType IN (:enumTypeList)";

		startDate = (Date) em
				.createQuery(queryStatement)
				.setParameter("enumTypeList",
						EnumSet.of(AttributeType.CPUUtilization))
				.getSingleResult();
		em.getTransaction().commit();

		reportDurationMinutes = 0;
		do {
			reportDurationMinutes = Integer.parseInt(JOptionPane
					.showInputDialog("\nFirst entry saved at \"" + startDate
							+ "\"" + "\nEnter report duration in minute:"));
		} while (reportDurationMinutes == 0);

		// reportDurationMinutes minutes ago
		timeOfNow = System.currentTimeMillis();
		pivotDate = new Date(timeOfNow - (1000 * 60 * reportDurationMinutes));
		expectedReportCount = ((timeOfNow - pivotDate.getTime()) / 1000)
				/ intervalseconds;

		ReportTimeLabel
				.setText("In last " + reportDurationMinutes + " minutes");

		em.getTransaction().begin();
		nodes = em.createQuery("SELECT n FROM Node n").getResultList();
		em.getTransaction().commit();

		@SuppressWarnings("rawtypes")
		Vector<Vector> rowData = new Vector<Vector>();
		Vector<String> columnNames = new Vector<String>();
		columnNames.addElement("ID");
		columnNames.addElement("Name");
		columnNames.addElement("IP");
		columnNames.addElement("CPU Usage");
		columnNames.addElement("RAM Utilization");
		columnNames.addElement("Uptime");
		columnNames.addElement("Downtime");

		for (Node node : nodes) {

			nodeID = node.getId();
			// ~~~ Average CPU Usage
			em.getTransaction().begin();
			// ~~~ CPU Usage
			String queryStatement1 = "SELECT AVG(a.value) FROM Attribute a WHERE "
					+ "a.resource.node.id = "
					+ nodeID
					+ " AND a.attributeType IN "
					+ "(:enumTypeList) AND a.date > :date";

			cpuUtilizationAvg = (double) em
					.createQuery(queryStatement1)
					.setParameter("enumTypeList",
							EnumSet.of(AttributeType.CPUUtilization))
					.setParameter("date", pivotDate, TemporalType.DATE)
					.getSingleResult();
			// ~~~ RAM Utilization
			String queryStatement2 = "SELECT AVG(a.value) FROM Attribute a WHERE "
					+ "a.resource.node.id = "
					+ nodeID
					+ " AND a.attributeType IN "
					+ "(:enumTypeList) AND a.date > :date";

			ramUtilizationAvg = (double) em
					.createQuery(queryStatement2)
					.setParameter("enumTypeList",
							EnumSet.of(AttributeType.MemoryUtilization))
					.setParameter("date", pivotDate, TemporalType.DATE)
					.getSingleResult();
			// ~~~ No. of reports
			String queryStatement3 = "SELECT COUNT(a) FROM Attribute a WHERE "
					+ "a.resource.node.id = " + nodeID
					+ " AND a.attributeType IN "
					+ "(:enumTypeList) AND a.date > :date";
			realReportCount = (long) em
					.createQuery(queryStatement3)
					.setParameter("date", pivotDate, TemporalType.TIMESTAMP)
					.setParameter("enumTypeList",
							EnumSet.of(AttributeType.CPUUtilization))
					.getSingleResult();

			em.getTransaction().commit();

			upTimePercent = realReportCount / (double) expectedReportCount;
			downTimePercent = 1 - upTimePercent;

			upTimeSeconds = reportDurationMinutes * upTimePercent;
			downTimeSeconds = reportDurationMinutes * downTimePercent;

			// ~~~ Fill table rows
			Vector<String> row = new Vector<String>();
			row.addElement(String.valueOf(nodeID));
			row.addElement(node.getName());
			row.addElement(node.getIP());
			row.addElement(String.valueOf(formatter.format(cpuUtilizationAvg))
					+ " %");
			row.addElement(String.valueOf(formatter.format(ramUtilizationAvg))
					+ " %");
			row.addElement(String.valueOf(formatter.format(upTimeSeconds))
					+ " min");
			row.addElement(String.valueOf(formatter.format(downTimeSeconds))
					+ " min");
			rowData.addElement(row);
		}

		JTable table = new JTable(rowData, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(650, 500));

		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(130);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);

		return table;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void createAndShowQueryManager() {
		mainFrame.setPreferredSize(new Dimension(770, 500));
		mainFrame.setLocation(400, 100);
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void File_CloseMenuItemActionPerformed(ActionEvent e) {
		this.dispose();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void Help_AboutMenuItemActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "DataBase Query Manager" + "\n"
				+ "Version S.2.0");
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void tools_GraphMenuItemActionPerformed(ActionEvent e) {
		Graph chart = new Graph();
		chart.openGraph();
		
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void nodeButtonActionPerformed(ActionEvent e) {
		NodeFrame newNodeFrame = new NodeFrame();
		newNodeFrame.createAndShowNodeFrame();

	}
}