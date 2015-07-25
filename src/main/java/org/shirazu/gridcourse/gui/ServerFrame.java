package org.shirazu.gridcourse.gui;

//============================================================================
//Author      : Fatemeh Marzban, Amir Hossein Sojoodi
//Version     : 0.0.1
//Year        : 2014
//Copyright   : GNU
//Description : Grid Information Service in Java
//============================================================================

import org.shirazu.gridcourse.gis.ClientDynamicInfo;
import org.shirazu.gridcourse.gis.ClientStaticInfo;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import org.shirazu.gridcourse.net.Server;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static int registryPort;
	private static int defaultRegistryPort = 54321;

	private ClientStaticInfo clientStaticInfo;
	private ClientDynamicInfo clientDynamicInfo;

	private Server server;
	private ServerTaskManager serverTaskManager;
	private Thread serverMonitoring;

	private JMenuBar menuBar1;
	private JMenu helpMenu;
	private JMenuItem help_AboutMenuItem;
	private JMenu toolsMenu;
	private JMenuItem tools_QueryManagerMenuItem;
	private JMenuItem tools_ClearDBMenuItem;
	private JPanel buttonPanel;
	private JButton startButton;
	private JSeparator separator1;
	private JLabel generalInformationLabel1;
	private JSeparator separator3;
	private JPanel generalInformationPanel1;
	private JLabel nameLabel1;
	private JLabel nameLabel2;
	private JSeparator separator5;
	private JLabel resourceInformationLabel1;
	private JTabbedPane resourceInformationTabbedPane1;
	private JPanel cpuPanel;
	private JLabel cpu_VendorLabel1;
	private JLabel cpu_VendorLabel2;
	private JLabel cpu_ModelLabel1;
	private JLabel cpu_ModelLabel2;
	private JLabel cpu_SpeedLabel1;
	private JLabel cpu_SpeedLabel2;
	private JLabel cpu_UtilizationLabel1;
	private JLabel cpu_UtilizationLabel2;
	private JPanel RAMPanel;
	private JLabel RAM_TotalLabel1;
	private JLabel RAM_TotalLabel2;
	private JLabel RAM_FreeLabel1;
	private JLabel RAM_FreeLabel2;
	private JLabel RAM_UtilizationLabel1;
	private JLabel RAM_UtilizationLabel2;
	private JPanel OSPanel;
	private JLabel OS_NameLabel1;
	private JLabel OS_NameLabel2;
	private JPanel StoragePanel;
	private JLabel Storage_TotalLabel1;
	private JLabel Storage_TotalLabel2;
	private JLabel Storage_FreeLabel1;
	private JLabel Storage_FreeLabel2;
	private JLabel Storage_UtilizationLabel1;
	private JLabel Storage_UtilizationLabel2;
	private JPanel NetworkPanel;
	private JLabel Network_BandwithLabel1;
	private JLabel Network_BandwithLabel2;
	private JLabel Network_UtilizationLabel1;
	private JLabel Network_UtilizationLabel2;
	private JPanel GPUPanel;
	private JLabel GPU_SpeedLabel1;
	private JLabel GPU_SpeedLabel2;
	private JLabel GPU_MemoryLabel1;
	private JLabel GPU_MemoryLabel2;
	private JScrollPane NotifierScrollPane;
	private JTextPane notifierTextPane;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowServerGUI();
			}
		});
	}

	public static void createAndShowServerGUI() {
		final JFrame serverFrame = new ServerFrame();
		serverFrame.pack();
		serverFrame.setVisible(true);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public ServerFrame() {
		try {
			server = new Server();
		} catch (RemoteException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}
		clientStaticInfo = new ClientStaticInfo();
		clientDynamicInfo = new ClientDynamicInfo();

		setRegistryPort(defaultRegistryPort);

		menuBar1 = new JMenuBar();
		helpMenu = new JMenu();
		help_AboutMenuItem = new JMenuItem();
		toolsMenu = new JMenu();
		tools_QueryManagerMenuItem = new JMenuItem();
		tools_ClearDBMenuItem = new JMenuItem();
		buttonPanel = new JPanel();
		startButton = new JButton();
		separator1 = new JSeparator();
		generalInformationLabel1 = new JLabel();
		separator3 = new JSeparator();
		generalInformationPanel1 = new JPanel();
		nameLabel1 = new JLabel();
		nameLabel2 = new JLabel();
		separator5 = new JSeparator();
		resourceInformationLabel1 = new JLabel();
		resourceInformationTabbedPane1 = new JTabbedPane();
		cpuPanel = new JPanel();
		cpu_VendorLabel1 = new JLabel();
		cpu_VendorLabel2 = new JLabel();
		cpu_ModelLabel1 = new JLabel();
		cpu_ModelLabel2 = new JLabel();
		cpu_SpeedLabel1 = new JLabel();
		cpu_SpeedLabel2 = new JLabel();
		cpu_UtilizationLabel1 = new JLabel();
		cpu_UtilizationLabel2 = new JLabel();
		RAMPanel = new JPanel();
		RAM_TotalLabel1 = new JLabel();
		RAM_TotalLabel2 = new JLabel();
		RAM_FreeLabel1 = new JLabel();
		RAM_FreeLabel2 = new JLabel();
		RAM_UtilizationLabel1 = new JLabel();
		RAM_UtilizationLabel2 = new JLabel();
		OSPanel = new JPanel();
		OS_NameLabel1 = new JLabel();
		OS_NameLabel2 = new JLabel();
		StoragePanel = new JPanel();
		Storage_TotalLabel1 = new JLabel();
		Storage_TotalLabel2 = new JLabel();
		Storage_FreeLabel1 = new JLabel();
		Storage_FreeLabel2 = new JLabel();
		Storage_UtilizationLabel1 = new JLabel();
		Storage_UtilizationLabel2 = new JLabel();
		NetworkPanel = new JPanel();
		Network_BandwithLabel1 = new JLabel();
		Network_BandwithLabel2 = new JLabel();
		Network_UtilizationLabel1 = new JLabel();
		Network_UtilizationLabel2 = new JLabel();
		GPUPanel = new JPanel();
		GPU_SpeedLabel1 = new JLabel();
		GPU_SpeedLabel2 = new JLabel();
		GPU_MemoryLabel1 = new JLabel();
		GPU_MemoryLabel2 = new JLabel();
		NotifierScrollPane = new JScrollPane();
		notifierTextPane = new JTextPane();

		// ======== this ========

		setTitle("Server");
		setMinimumSize(new Dimension(350, 550));
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout("16*(default, $lcgap), default",
				"13*(default, $lgap), default"));
		// ======== menuBar1 ========
		{
			// ======== toolsMenu ========
			{
				toolsMenu.setText("Tools");

				// ---- tools_QueryManagerMenuItem ----
				tools_QueryManagerMenuItem.setText("Query Manager");
				tools_QueryManagerMenuItem
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								tools_QueryManagerMenuItemActionPerformed(e);
							}
						});

				toolsMenu.add(tools_QueryManagerMenuItem);

				// ---- tools_ClearDBMenuItem ----
				tools_ClearDBMenuItem.setText("Clear DataBase");
				tools_ClearDBMenuItem.setEnabled(true);
				tools_ClearDBMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tools_ClearDBMenuItemActionPerformed(e);
					}
				});

				toolsMenu.add(tools_ClearDBMenuItem);
			}
			menuBar1.add(toolsMenu);
			// ======== helpMenu ========
			{
				helpMenu.setText("Help");

				// ---- help_AboutMenuItem ----
				help_AboutMenuItem.setText("About");
				help_AboutMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						help_AboutMenuItemActionPerformed(e);
					}
				});

				helpMenu.add(help_AboutMenuItem);
			}
			menuBar1.add(helpMenu);
		}
		setJMenuBar(menuBar1);

		// ======== ButtonPanel ========
		{

			buttonPanel.setLayout(new FormLayout(
					"3*(default, $lcgap), default",
					"2*(default, $lgap), default"));

			// ---- StartButton ----
			startButton.setText("Start");
			startButton.setEnabled(true);

			startButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					startButtonActionPerformed(e);
				}
			});
			buttonPanel.add(startButton, CC.xy(1, 3));

		}
		contentPane.add(buttonPanel, CC.xy(3, 3));

		contentPane.add(separator1, CC.xywh(3, 7, 5, 1));

		// ---- GeneralInformationLabel1 ----
		generalInformationLabel1.setText("General Information");
		generalInformationLabel1.setForeground(new Color(204, 0, 102));
		contentPane.add(generalInformationLabel1, CC.xy(3, 9));
		contentPane.add(separator3, CC.xywh(3, 11, 5, 1));

		// ======== GeneralInformationPanel1 ========
		{
			generalInformationPanel1.setLayout(new FormLayout(
					"default, $lcgap, default", "default, $lgap, default"));

			// ---- NameLabel1 ----
			nameLabel1.setText("Name: ");
			generalInformationPanel1.add(nameLabel1, CC.xy(1, 3));

			// ---- NameLabel2 ----
			nameLabel2.setText(System.getProperty("os.name") + " "
					+ System.getProperty("os.arch"));
			generalInformationPanel1.add(nameLabel2, CC.xy(3, 3));
		}
		contentPane.add(generalInformationPanel1, CC.xywh(3, 13, 4, 1));
		contentPane.add(separator5, CC.xywh(3, 15, 5, 1));

		// ---- ResourceInformationLabel1 ----
		resourceInformationLabel1.setText("Resource Information");
		resourceInformationLabel1.setForeground(new Color(153, 0, 102));
		resourceInformationLabel1.setVisible(false);
		contentPane.add(resourceInformationLabel1, CC.xy(3, 17));

		// ======== ResourceInformationTabbedPane1 ========
		{
			// ======== CPUPanel ========
			{
				cpuPanel.setLayout(new FormLayout("default, $lcgap, default",
						"3*(default, $lgap), default"));

				// ---- CPU_VendorLabel1 ----
				cpu_VendorLabel1.setText("Vendor");
				cpu_VendorLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				cpu_VendorLabel1.setForeground(new Color(170, 34, 105));
				cpuPanel.add(cpu_VendorLabel1, CC.xy(1, 1));

				// ---- CPU_VendorLabel2 ----
				cpu_VendorLabel2.setText("Error");
				cpu_VendorLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				cpuPanel.add(cpu_VendorLabel2, CC.xy(3, 1));

				// ---- CPU_ModelLabel1 ----
				cpu_ModelLabel1.setText("Model");
				cpu_ModelLabel1
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				cpu_ModelLabel1.setForeground(new Color(170, 34, 105));
				cpuPanel.add(cpu_ModelLabel1, CC.xy(1, 3));

				// ---- CPU_ModelLabel2 ----
				cpu_ModelLabel2.setText("Error");
				cpu_ModelLabel2
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				cpuPanel.add(cpu_ModelLabel2, CC.xy(3, 3));

				// ---- CPU_SpeedLabel1 ----
				cpu_SpeedLabel1.setText("Speed");
				cpu_SpeedLabel1
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				cpu_SpeedLabel1.setForeground(new Color(170, 34, 105));
				cpuPanel.add(cpu_SpeedLabel1, CC.xy(1, 5));

				// ---- CPU_SpeedLabel2 ----
				cpu_SpeedLabel2.setText("Error");
				cpu_SpeedLabel2
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				cpuPanel.add(cpu_SpeedLabel2, CC.xy(3, 5));

				// ---- CPU_UtilizationLabel1 ----
				cpu_UtilizationLabel1.setText("Usage"); // Changed from
														// Utilization
				cpu_UtilizationLabel1.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				cpu_UtilizationLabel1.setForeground(new Color(170, 34, 105));
				cpuPanel.add(cpu_UtilizationLabel1, CC.xy(1, 7));

				// ---- CPU_UtilizationLabel2 ----
				cpu_UtilizationLabel2.setText("Error");
				cpu_UtilizationLabel2.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				cpuPanel.add(cpu_UtilizationLabel2, CC.xy(3, 7));
			}
			resourceInformationTabbedPane1.addTab("CPU", cpuPanel);

			// ======== RAMPanel ========
			{
				RAMPanel.setLayout(new FormLayout("default, $lcgap, default",
						"2*(default, $lgap), default"));

				// ---- RAM_TotalLabel1 ----
				RAM_TotalLabel1.setText("Total");
				RAM_TotalLabel1
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				RAM_TotalLabel1.setForeground(new Color(170, 34, 105));
				RAMPanel.add(RAM_TotalLabel1, CC.xy(1, 1));

				// ---- RAM_TotalLabel2 ----
				RAM_TotalLabel2.setText("Error");
				RAM_TotalLabel2
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				RAMPanel.add(RAM_TotalLabel2, CC.xy(3, 1));

				// ---- RAM_FreeLabel1 ----
				RAM_FreeLabel1.setText("Free");
				RAM_FreeLabel1
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				RAM_FreeLabel1.setForeground(new Color(170, 34, 105));
				RAMPanel.add(RAM_FreeLabel1, CC.xy(1, 3));

				// ---- RAM_FreeLabel2 ----
				RAM_FreeLabel2.setText("Error");
				RAM_FreeLabel2
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				RAMPanel.add(RAM_FreeLabel2, CC.xy(3, 3));

				// ---- RAM_UtilizationLabel1 ----
				RAM_UtilizationLabel1.setText("Usage"); // changed from
														// Utilization
				RAM_UtilizationLabel1.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				RAM_UtilizationLabel1.setForeground(new Color(170, 34, 105));
				RAMPanel.add(RAM_UtilizationLabel1, CC.xy(1, 5));

				// ---- RAM_UtilizationLabel2 ----
				RAM_UtilizationLabel2.setText("Error");
				RAM_UtilizationLabel2.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				RAMPanel.add(RAM_UtilizationLabel2, CC.xy(3, 5));
			}
			resourceInformationTabbedPane1.addTab("RAM", RAMPanel);

			// ======== OSPanel ========
			{
				OSPanel.setLayout(new FormLayout("default, $lcgap, default",
						"2*(default, $lgap), default"));

				// ---- OS_NameLabel1 ----
				OS_NameLabel1.setText("Name");
				OS_NameLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				OS_NameLabel1.setForeground(new Color(170, 34, 105));
				OSPanel.add(OS_NameLabel1, CC.xy(1, 1));

				// ---- OS_NameLabel2 ----
				OS_NameLabel2.setText("Error");
				OS_NameLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				OSPanel.add(OS_NameLabel2, CC.xy(3, 1));
			}
			resourceInformationTabbedPane1.addTab("OS", OSPanel);

			// ======== StoragePanel ========
			{
				StoragePanel.setLayout(new FormLayout(
						"default, $lcgap, default",
						"2*(default, $lgap), default"));

				// ---- Storage_TotalLabel1 ----
				Storage_TotalLabel1.setText("Total");
				Storage_TotalLabel1.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				Storage_TotalLabel1.setForeground(new Color(170, 34, 105));
				StoragePanel.add(Storage_TotalLabel1, CC.xy(1, 1));

				// ---- Storage_TotalLabel2 ----
				Storage_TotalLabel2.setText("Error");
				Storage_TotalLabel2.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				StoragePanel.add(Storage_TotalLabel2, CC.xy(3, 1));

				// ---- Storage_FreeLabel1 ----
				Storage_FreeLabel1.setText("Free");
				Storage_FreeLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				Storage_FreeLabel1.setForeground(new Color(170, 34, 105));
				StoragePanel.add(Storage_FreeLabel1, CC.xy(1, 3));

				// ---- Storage_FreeLabel2 ----
				Storage_FreeLabel2.setText("Error");
				Storage_FreeLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				StoragePanel.add(Storage_FreeLabel2, CC.xy(3, 3));

				// ---- Storage_UtilizationLabel1 ----
				Storage_UtilizationLabel1.setText("Usage"); // changed from
															// utilization
				Storage_UtilizationLabel1.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				Storage_UtilizationLabel1
						.setForeground(new Color(170, 34, 105));
				StoragePanel.add(Storage_UtilizationLabel1, CC.xy(1, 5));

				// ---- Storage_UtilizationLabel2 ----
				Storage_UtilizationLabel2.setText("Error");
				Storage_UtilizationLabel2.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				StoragePanel.add(Storage_UtilizationLabel2, CC.xy(3, 5));
			}
			resourceInformationTabbedPane1.addTab("Storage", StoragePanel);

			// ======== NetworkPanel ========
			{
				NetworkPanel.setLayout(new FormLayout(
						"default, $lcgap, default",
						"2*(default, $lgap), default"));

				// ---- Network_BandwithLabel1 ----
				Network_BandwithLabel1.setText("Bandwith");
				Network_BandwithLabel1.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				Network_BandwithLabel1.setForeground(new Color(170, 34, 105));
				NetworkPanel.add(Network_BandwithLabel1, CC.xy(1, 1));

				// ---- Network_BandwithLabel2 ----
				Network_BandwithLabel2.setText("Error");
				Network_BandwithLabel2.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				NetworkPanel.add(Network_BandwithLabel2, CC.xy(3, 1));

				// ---- Network_UtilizationLabel1 ----
				Network_UtilizationLabel1.setText("Utilization");
				Network_UtilizationLabel1.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				Network_UtilizationLabel1
						.setForeground(new Color(170, 34, 105));
				NetworkPanel.add(Network_UtilizationLabel1, CC.xy(1, 3));

				// ---- Network_UtilizationLabel2 ----
				Network_UtilizationLabel2.setText("Error");
				Network_UtilizationLabel2.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				NetworkPanel.add(Network_UtilizationLabel2, CC.xy(3, 3));
			}
			resourceInformationTabbedPane1.addTab("Network", NetworkPanel);

			// ======== GPUPanel ========
			{
				GPUPanel.setLayout(new FormLayout("default, $lcgap, default",
						"2*(default, $lgap), default"));

				// ---- GPU_SpeedLabel1 ----
				GPU_SpeedLabel1.setText("Speed");
				GPU_SpeedLabel1
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				GPU_SpeedLabel1.setForeground(new Color(170, 34, 105));
				GPUPanel.add(GPU_SpeedLabel1, CC.xy(1, 1));

				// ---- GPU_SpeedLabel2 ----
				GPU_SpeedLabel2.setText("Error");
				GPU_SpeedLabel2
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				GPUPanel.add(GPU_SpeedLabel2, CC.xy(3, 1));

				// ---- GPU_MemoryLabel1 ----
				GPU_MemoryLabel1.setText("Memory");
				GPU_MemoryLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				GPU_MemoryLabel1.setForeground(new Color(170, 34, 105));
				GPUPanel.add(GPU_MemoryLabel1, CC.xy(1, 3));

				// ---- GPU_MemoryLabel2 ----
				GPU_MemoryLabel2.setText("Error");
				GPU_MemoryLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				GPUPanel.add(GPU_MemoryLabel2, CC.xy(3, 3));
			}
			resourceInformationTabbedPane1.addTab("GPU", GPUPanel);
		}
		resourceInformationTabbedPane1.setVisible(false);
		contentPane.add(resourceInformationTabbedPane1, CC.xywh(3, 19, 29, 1));

		// ======== NotifierScrollPane ========
		{
			NotifierScrollPane.setViewportView(notifierTextPane);
			notifierTextPane.setEditable(false);
			notifierTextPane.setMinimumSize(new Dimension(300, 200));
			notifierTextPane.setMaximumSize(new Dimension(300, 200));
			notifierTextPane.setPreferredSize(new Dimension(300, 200));
			notifierTextPane.setForeground(new Color(0, 106, 107));
		}
		contentPane.add(NotifierScrollPane, CC.xy(3, 27));

		serverTaskManager = new ServerTaskManager(this,
				resourceInformationLabel1, resourceInformationTabbedPane1);

		serverTaskManager.start();

		setLocationRelativeTo(getOwner());

	}

	public void startButtonActionPerformed(ActionEvent e) {

		/*
		 * if (System.getSecurityManager() == null) {
		 * System.setSecurityManager(new RMISecurityManager()); }
		 */

		setRegistryPort(Integer.parseInt(JOptionPane
				.showInputDialog("Enter monitoring port:")));

		try {
			LocateRegistry.createRegistry(getRegistryPort());

			// LocateRegistry.createRegistry(registryPort, csf, ssf)
			//
			// Naming.rebind("//192.168.202.129" + ":" + registryPort +
			// "/GISServer",
			// ((ServerFrame) serverFrame).server);
			Naming.rebind("//127.0.0.1" + ":" + getRegistryPort()
					+ "/GISServer", server);

			// System.out.println("Server bind successfull..");

		} catch (RemoteException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		} catch (MalformedURLException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}

		server.clearDataBase();
		tools_ClearDBMenuItem.setEnabled(false);

		int simulationMinutes = Integer.parseInt(JOptionPane
				.showInputDialog("Enter simulation time in minute:"));
		int intervalSeconds = Integer.parseInt(JOptionPane
				.showInputDialog("Enter intervals in second:"));

		server.setServerFrame(this);
		server.setSimulationMinutes(simulationMinutes);
		server.setIntervalSeconds(intervalSeconds);

		startButton.setEnabled(false);
		startButton.setText("Running");

		serverMonitoring = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server.startMonitoring();
				} catch (RemoteException e) {
					// e.printStackTrace();
					// TODO - Error handling
					JOptionPane.showMessageDialog(null,
							"Error\n" + e.getMessage());
				}

			}

		});
		setNotifierText("Monitoring is started on port: " + getRegistryPort());
		serverMonitoring.start();

	}

	public void resetStartButton() {
		startButton.setEnabled(true);
		startButton.setText("Start");
	}

	public void setNotifierText(String text) {

		if (notifierTextPane.getText().length() > 250) {
			notifierTextPane.setText("");
		}
		String tmpText = notifierTextPane.getText();
		notifierTextPane.setText(tmpText + "\n~ " + text);
	}

	public void help_AboutMenuItemActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "GIS Connection Manager" + "\n"
				+ "Server Version: 0.0.1");
	}

	public void tools_QueryManagerMenuItemActionPerformed(ActionEvent e) {
		QueryManager queryManager = null;
		try {
			queryManager = new QueryManager(server.getIntervalSeconds());
		} catch (RemoteException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}
		queryManager.createAndShowQueryManager();
	}

	public void tools_ClearDBMenuItemActionPerformed(ActionEvent e) {
		server.clearDataBase();
		setNotifierText("DataBase cleared.");
	}

	public void setStaticInfo() {

		// ~~~ Set labels
		cpu_VendorLabel2.setText(clientStaticInfo.getCPUVendor());
		cpu_ModelLabel2.setText(clientStaticInfo.getCPUModel());
		cpu_SpeedLabel2.setText(clientStaticInfo.getCPUSpeed() + " MHz");
		RAM_TotalLabel2.setText(clientStaticInfo.getRAMInfo() + " MB");
		OS_NameLabel2.setText(clientStaticInfo.getOSInfo());
		Storage_TotalLabel2.setText(clientStaticInfo.getSStorageInfo() + " GB");
	}

	public void setDynamicInfo() {
		// ~~~ Set labels
		cpu_UtilizationLabel2.setText(clientDynamicInfo.getCPUUsage() + " %");
		RAM_FreeLabel2.setText(clientDynamicInfo.getRAMFreeInfo() + " MB");
		RAM_UtilizationLabel2.setText(clientDynamicInfo.getRAMUtilizationInfo()
				+ " %");
		Storage_FreeLabel2.setText(clientDynamicInfo.getSStorageFreeInfo()
				+ " GB");
		Storage_UtilizationLabel2.setText(clientDynamicInfo
				.getSStorageUtilizationInfo() + " %");
	}

	public int getSimulationTime() {

		String simulationTime = "";

		do {
			simulationTime = JOptionPane
					.showInputDialog("Enter simulation time in minutes: ");
		} while (simulationTime.equals(""));

		return Integer.parseInt(simulationTime);
	}

	public int getTimeInterval() {

		String timeInterval = "";

		do {
			timeInterval = JOptionPane
					.showInputDialog("Enter time interval in seconds: ");
		} while (timeInterval.equals(""));

		return Integer.parseInt(timeInterval);
	}

	public static int getRegistryPort() {
		return registryPort;
	}

	public static void setRegistryPort(int registryPort) {
		ServerFrame.registryPort = registryPort;
	}

	public JTextPane getNotifierTextPane() {
		return notifierTextPane;
	}

	public void setNotifierTextPane(JTextPane notifierTextPane) {
		this.notifierTextPane = notifierTextPane;
	}

}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
class ServerTaskManager extends Thread {
	private int localIntervalSeconds;
	private ServerFrame serverFrame;

	private JLabel ResourceInformationLabel1;
	private JTabbedPane ResourceInformationTabbedPane1;

	public ServerTaskManager(ServerFrame newJFrame, JLabel newJLabel,
			JTabbedPane newJTabbedPane) {
		localIntervalSeconds = 1;
		serverFrame = newJFrame;
		ResourceInformationLabel1 = newJLabel;
		ResourceInformationTabbedPane1 = newJTabbedPane;
	}

	public void run() {
		ResourceInformationTabbedPane1.setVisible(true);
		ResourceInformationLabel1.setVisible(true);
		serverFrame.setStaticInfo();
		serverFrame.setDynamicInfo();
		while (true) {
			try {
				Thread.sleep(localIntervalSeconds * 1000, 0);
			} catch (InterruptedException e) {
				// e.printStackTrace();
				// TODO - Error handling
				JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
			}
			serverFrame.setDynamicInfo();
		}
	}

	@SuppressWarnings("deprecation")
	public void ServerTaskManagerStop() {
		ResourceInformationTabbedPane1.setVisible(false);
		ResourceInformationLabel1.setVisible(false);
		this.stop();
	}

}