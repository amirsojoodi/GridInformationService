package gui;

import gis.AttributeType;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import de.progra.charting.ChartEncoder;
import de.progra.charting.ChartUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.DefaultChart;
import de.progra.charting.model.DefaultChartDataModel;
import de.progra.charting.model.DefaultDataSet;
import de.progra.charting.render.LineChartRenderer;

public class Graph extends JFrame {

	private int xDimension;
	private int yDimension;

	private int xLocation;
	private int yLocation;

	private static final long serialVersionUID = 1L;
	private long nodeID;
	private EntityManagerFactory emf;
	private EntityManager em;

	private JFrame chartFrame;
	private JLabel chartLabel;

	private double[] cpuUsageArray;
	private double[] ramUtilizationArray;
	private double[] columns;

	private static int NoOfData;

	public Graph() {
		nodeID = 0;
		do {
			nodeID = Long.parseLong(JOptionPane
					.showInputDialog("Enter Node's ID: "));
		} while (nodeID == 0);

		chartFrame = new JFrame();
		chartLabel = new JLabel();
		emf = Persistence.createEntityManagerFactory("$objectdb/db/GIS.odb");
		em = emf.createEntityManager();
		cpuUsageArray = null;
		ramUtilizationArray = null;
		columns = null;
		NoOfData = 0;

		setCpuInfo();
		setRamInfo();
		makeGraphFile();

		setxDimension(700);
		setyDimension(540);
		setxLocation();
		setyLocation();

		// ======== this ========
		chartFrame.setTitle("Monitoring Graph");
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout("3*(default, $lcgap), default",
				"3*(default, $lgap), default"));
		contentPane.setBackground(Color.WHITE);

		// ======== pictureLabel ========
		chartLabel.setVisible(true);
		chartLabel.setPreferredSize(new Dimension(640, 480));
		chartLabel.setIcon(new ImageIcon(System.getProperty("user.home")
				+ "/graph.png"));
		contentPane.add(chartLabel, CC.xy(3, 3));
		chartFrame.add(contentPane);

	}

	public void openGraph() {
		File file = new File(System.getProperty("user.home") + "/graph.png");
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			String cmd;
			try {
				cmd = "rundll32 url.dll,FileProtocolHandler "
						+ file.getCanonicalPath();
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e1) {
				// e1.printStackTrace();
				// TODO - Error handling
				JOptionPane
						.showMessageDialog(null, "Error\n" + e1.getMessage());
			}

		} else {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e1) {
				// e1.printStackTrace();
				// TODO - Error handling
				JOptionPane
						.showMessageDialog(null, "Error\n" + e1.getMessage());
			}
		}
	}

	public void createAndShowShartFrame() {

		chartFrame.setPreferredSize(new Dimension(getxDimension(),
				getyDimension()));
		chartFrame.setLocation(getxLocation(), getyLocation());
		chartFrame.setBackground(Color.WHITE);
		chartFrame.pack();
		chartFrame.setVisible(true);
		chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	public static void main(String[] args) {
		Graph chart = new Graph();
		chart.createAndShowShartFrame();
	}

	@SuppressWarnings({ "unchecked" })
	public void setRamInfo() {
		int i;

		List<Double> ramUtilizationList = null;

		em.getTransaction().begin();

		String ramQueryStatement = "SELECT a.value FROM Attribute a WHERE "
				+ "a.attributeType IN (:enumTypeList) AND a.resource.node.id = "
				+ nodeID;

		ramUtilizationList = (List<Double>) em
				.createQuery(ramQueryStatement)
				.setParameter("enumTypeList",
						EnumSet.of(AttributeType.MemoryUtilization))
				.getResultList();

		em.getTransaction().commit();
		ramUtilizationArray = new double[NoOfData];
		i = 0;
		for (Double d : ramUtilizationList) {
			ramUtilizationArray[i] = d.doubleValue();
			i++;
		}
		setNoOfData(i);

	}

	@SuppressWarnings({ "unchecked" })
	public void setCpuInfo() {
		int i;

		List<Double> cpuUsageList;

		em.getTransaction().begin();

		String cpuQueryStatement = "SELECT a.value FROM Attribute a WHERE "
				+ "a.attributeType IN (:enumTypeList) AND a.resource.node.id = "
				+ nodeID;

		cpuUsageList = (List<Double>) em
				.createQuery(cpuQueryStatement)
				.setParameter("enumTypeList",
						EnumSet.of(AttributeType.CPUUtilization))
				.getResultList();

		em.getTransaction().commit();

		setNoOfData(cpuUsageList.size());
		cpuUsageArray = new double[NoOfData];

		i = 0;
		for (Double d : cpuUsageList) {
			cpuUsageArray[i] = d;
			i++;
		}

	}

	public void makeGraphFile() {

		columns = new double[NoOfData];
		for (int i = 0; i < NoOfData; i++) {
			columns[i] = i * 5;
		}

		// Creating a data set array
		DefaultDataSet[] ds = new DefaultDataSet[2];

		ds[0] = new DefaultDataSet(
				ChartUtilities.transformArray(cpuUsageArray),
				ChartUtilities.transformArray(columns),
				CoordSystem.FIRST_YAXIS, "CPU Usage");

		ds[1] = new DefaultDataSet(
				ChartUtilities.transformArray(ramUtilizationArray),
				ChartUtilities.transformArray(columns),
				CoordSystem.FIRST_YAXIS, "RAM Utilization");

		String title = "Node " + nodeID;

		int width = 640;
		int height = 480;

		DefaultChartDataModel data = new DefaultChartDataModel(ds);

		data.setAutoScale(true);

		DefaultChart c = new DefaultChart(data, title,
				DefaultChart.LINEAR_X_LINEAR_Y);

		c.addChartRenderer(new LineChartRenderer(c.getCoordSystem(), data), 1);

		c.setBounds(new Rectangle(0, 0, width, height));

		try {
			ChartEncoder.createPNG(
					new FileOutputStream(System.getProperty("user.home")
							+ "/graph.png"), c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNoOfData() {
		return NoOfData;
	}

	public void setNoOfData(int noOfData) {
		NoOfData = noOfData;
	}

	public int getxLocation() {
		return xLocation;
	}

	public void setxLocation() {
		Dimension screenDimension = getScreenDimension();
		this.xLocation = ((screenDimension.width / 2) - (getxDimension() / 2));
	}

	public int getyLocation() {
		return yLocation;
	}

	public void setyLocation() {
		Dimension screenDimension = getScreenDimension();
		this.yLocation = ((screenDimension.height / 2) - (getyDimension() / 2));
	}

	public int getxDimension() {
		return xDimension;
	}

	public void setxDimension(int xDimension) {
		this.xDimension = xDimension;
	}

	public int getyDimension() {
		return yDimension;
	}

	public void setyDimension(int yDimension) {
		this.yDimension = yDimension;
	}

	public Dimension getScreenDimension() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

}
