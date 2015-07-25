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
import org.shirazu.gridcourse.gis.Mode;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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

import org.shirazu.gridcourse.net.Client;
import org.shirazu.gridcourse.net.RemoteServer;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private int intervalSeconds;
	private int simulationMinutes;
	private String serverIP;
	private int socketPort;

	private Client client;
	private ClientTaskManager clientTaskManager;
	private ClientStaticInfo clientStaticInfo;
	private ClientDynamicInfo clientDynamicInfo;

	// Variables declaration
	private JMenuBar menuBar1;
	private JMenu helpMenu;
	private JMenuItem help_AboutMenuItem;
	private JPanel ButtonPanel;
	private JButton RegisterButton;
	private JButton PauseButton;
	private JButton ResumeButton;
	private JSeparator separator1;
	private JLabel GeneralInformationLabel1;
	private JSeparator separator3;
	private JPanel GeneralInformationPanel1;
	private JLabel NameLabel1;
	private JLabel NameLabel2;
	private JSeparator separator5;
	private JLabel ResourceInformationLabel1;
	private JTabbedPane ResourceInformationTabbedPane1;
	private JPanel CPUPanel;
	private JLabel CPU_VendorLabel1;
	private JLabel CPU_VendorLabel2;
	private JLabel CPU_ModelLabel1;
	private JLabel CPU_ModelLabel2;
	private JLabel CPU_SpeedLabel1;
	private JLabel CPU_SpeedLabel2;
	private JLabel CPU_UtilizationLabel1;
	private JLabel CPU_UtilizationLabel2;
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
	private JScrollPane NotifierscrollPane;
	private JTextPane notifierTextPane;

	private static ClientFrame clientFrame;
	private static NormalClient normalClient;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static void main(String[] args) {

		if (args.length > 0 && args[0].equals("--silent")) {
			System.out.println("Silent mode activated..");
			normalClient = new NormalClient();
			normalClient.runNormalClient();
		} else {
			clientFrame = new ClientFrame();
			clientFrame.createAndShowClientGUI();
		}

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void createAndShowClientGUI() {
		client.setMode(Mode.normal);
		JFrame clientFrame = new ClientFrame();
		clientFrame.pack();
		clientFrame.setVisible(true);
		clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void setStaticInfo() {
		// ~~~ Set labels
		CPU_VendorLabel2.setText(clientStaticInfo.getCPUVendor());
		CPU_ModelLabel2.setText(clientStaticInfo.getCPUModel());
		CPU_SpeedLabel2.setText(clientStaticInfo.getCPUSpeed() + " MHz");
		RAM_TotalLabel2.setText(clientStaticInfo.getRAMInfo() + " MB");
		OS_NameLabel2.setText(clientStaticInfo.getOSInfo());
		Storage_TotalLabel2.setText(clientStaticInfo.getSStorageInfo() + " GB");
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void setDynamicInfo() {
		// ~~~ Set labels
		CPU_UtilizationLabel2.setText(clientDynamicInfo.getCPUUsage() + " %");
		RAM_FreeLabel2.setText(clientDynamicInfo.getRAMFreeInfo() + " MB");
		RAM_UtilizationLabel2.setText(clientDynamicInfo.getRAMUtilizationInfo()
				+ " %");
		Storage_FreeLabel2.setText(clientDynamicInfo.getSStorageFreeInfo()
				+ " GB");
		Storage_UtilizationLabel2.setText(clientDynamicInfo
				.getSStorageUtilizationInfo() + " %");
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public ClientFrame() {
		client = new Client();
		clientDynamicInfo = new ClientDynamicInfo();
		clientStaticInfo = new ClientStaticInfo();

		// Component initialization
		menuBar1 = new JMenuBar();
		helpMenu = new JMenu();
		help_AboutMenuItem = new JMenuItem();
		ButtonPanel = new JPanel();
		RegisterButton = new JButton();
		PauseButton = new JButton();
		ResumeButton = new JButton();
		separator1 = new JSeparator();
		GeneralInformationLabel1 = new JLabel();
		separator3 = new JSeparator();
		GeneralInformationPanel1 = new JPanel();
		NameLabel1 = new JLabel();
		NameLabel2 = new JLabel();
		separator5 = new JSeparator();
		ResourceInformationLabel1 = new JLabel();
		ResourceInformationTabbedPane1 = new JTabbedPane();
		CPUPanel = new JPanel();
		CPU_VendorLabel1 = new JLabel();
		CPU_VendorLabel2 = new JLabel();
		CPU_ModelLabel1 = new JLabel();
		CPU_ModelLabel2 = new JLabel();
		CPU_SpeedLabel1 = new JLabel();
		CPU_SpeedLabel2 = new JLabel();
		CPU_UtilizationLabel1 = new JLabel();
		CPU_UtilizationLabel2 = new JLabel();
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
		NotifierscrollPane = new JScrollPane();
		notifierTextPane = new JTextPane();

		// ======== this ========
		setTitle("Client");
		setMinimumSize(new Dimension(350, 550));
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout("16*(default, $lcgap), "
				+ "default", "13*(default, $lgap), default"));
		// ======== menuBar1 ========
		{
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

			ButtonPanel.setLayout(new FormLayout(
					"2*(default, $lcgap), default",
					"2*(default, $lgap), default"));

			// ---- RegisterButton1 ----
			RegisterButton.setText("Register");
			RegisterButton.setEnabled(true);

			RegisterButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					RegisterButtonActionPerformed(e);
				}
			});
			ButtonPanel.add(RegisterButton, CC.xy(1, 3));

			// ---- PauseButton ----
			PauseButton.setText("Pause");
			PauseButton.setEnabled(false);

			PauseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					PauseButtonActionPerformed(e);
				}
			});
			ButtonPanel.add(PauseButton, CC.xy(3, 3));

			// ---- ResumButton ----
			ResumeButton.setText("Resume");
			ResumeButton.setEnabled(false);

			ResumeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ResumeButtonActionPerformed(e);
				}
			});
			ButtonPanel.add(ResumeButton, CC.xy(5, 3));
		}
		contentPane.add(ButtonPanel, CC.xy(3, 1));

		contentPane.add(separator1, CC.xywh(3, 7, 5, 1));

		// ---- GeneralInformationLabel1 ----
		GeneralInformationLabel1.setText("General Information");
		GeneralInformationLabel1.setForeground(new Color(204, 0, 102));
		contentPane.add(GeneralInformationLabel1, CC.xy(3, 9));
		contentPane.add(separator3, CC.xywh(3, 11, 5, 1));

		// ======== GeneralInformationPanel1 ========
		{
			GeneralInformationPanel1.setLayout(new FormLayout(
					"default, $lcgap, default", "default, $lgap, default"));

			// ---- NameLabel1 ----
			NameLabel1.setText("Name: ");
			GeneralInformationPanel1.add(NameLabel1, CC.xy(1, 3));

			// ---- NameLabel2 ----
			NameLabel2.setText(System.getProperty("os.name") + " "
					+ System.getProperty("os.arch"));
			GeneralInformationPanel1.add(NameLabel2, CC.xy(3, 3));
		}
		contentPane.add(GeneralInformationPanel1, CC.xywh(3, 13, 4, 1));
		contentPane.add(separator5, CC.xywh(3, 15, 5, 1));

		// ---- ResourceInformationLabel1 ----
		ResourceInformationLabel1.setText("Resource Information");
		ResourceInformationLabel1.setForeground(new Color(153, 0, 102));
		ResourceInformationLabel1.setVisible(false);
		contentPane.add(ResourceInformationLabel1, CC.xy(3, 17));

		// ======== ResourceInformationTabbedPane1 ========
		{
			// ======== CPUPanel ========
			{
				CPUPanel.setLayout(new FormLayout("default, $lcgap, default",
						"3*(default, $lgap), default"));

				// ---- CPU_VendorLabel1 ----
				CPU_VendorLabel1.setText("Vendor");
				CPU_VendorLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				CPU_VendorLabel1.setForeground(new Color(170, 34, 105));
				CPUPanel.add(CPU_VendorLabel1, CC.xy(1, 1));

				// ---- CPU_VendorLabel2 ----
				CPU_VendorLabel2.setText("Error");
				CPU_VendorLabel2.setFont(new Font("Trebuchet MS", Font.PLAIN,
						14));
				CPUPanel.add(CPU_VendorLabel2, CC.xy(3, 1));

				// ---- CPU_ModelLabel1 ----
				CPU_ModelLabel1.setText("Model");
				CPU_ModelLabel1
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				CPU_ModelLabel1.setForeground(new Color(170, 34, 105));
				CPUPanel.add(CPU_ModelLabel1, CC.xy(1, 3));

				// ---- CPU_ModelLabel2 ----
				CPU_ModelLabel2.setText("Error");
				CPU_ModelLabel2
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				CPUPanel.add(CPU_ModelLabel2, CC.xy(3, 3));

				// ---- CPU_SpeedLabel1 ----
				CPU_SpeedLabel1.setText("Speed");
				CPU_SpeedLabel1
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				CPU_SpeedLabel1.setForeground(new Color(170, 34, 105));
				CPUPanel.add(CPU_SpeedLabel1, CC.xy(1, 5));

				// ---- CPU_SpeedLabel2 ----
				CPU_SpeedLabel2.setText("Error");
				CPU_SpeedLabel2
						.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
				CPUPanel.add(CPU_SpeedLabel2, CC.xy(3, 5));

				// ---- CPU_UtilizationLabel1 ----
				CPU_UtilizationLabel1.setText("Usage");
				CPU_UtilizationLabel1.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				CPU_UtilizationLabel1.setForeground(new Color(170, 34, 105));
				CPUPanel.add(CPU_UtilizationLabel1, CC.xy(1, 7));

				// ---- CPU_UtilizationLabel2 ----
				CPU_UtilizationLabel2.setText("Error");
				CPU_UtilizationLabel2.setFont(new Font("Trebuchet MS",
						Font.PLAIN, 14));
				CPUPanel.add(CPU_UtilizationLabel2, CC.xy(3, 7));
			}
			ResourceInformationTabbedPane1.addTab("CPU", CPUPanel);

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
				RAM_UtilizationLabel1.setText("Usage");
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
			ResourceInformationTabbedPane1.addTab("RAM", RAMPanel);

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
			ResourceInformationTabbedPane1.addTab("OS", OSPanel);

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
				Storage_UtilizationLabel1.setText("Usage");
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
			ResourceInformationTabbedPane1.addTab("Storage", StoragePanel);

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
				Network_UtilizationLabel1.setText("Usage");
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
			ResourceInformationTabbedPane1.addTab("Network", NetworkPanel);

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
			ResourceInformationTabbedPane1.addTab("GPU", GPUPanel);
		}
		ResourceInformationTabbedPane1.setVisible(false);
		contentPane.add(ResourceInformationTabbedPane1, CC.xywh(3, 19, 29, 1));

		// ======== NotifierScrollPane ========
		{
			NotifierscrollPane.setViewportView(notifierTextPane);
			notifierTextPane.setEditable(false);
			notifierTextPane.setMinimumSize(new Dimension(300, 200));
			notifierTextPane.setMaximumSize(new Dimension(300, 200));
			notifierTextPane.setPreferredSize(new Dimension(300, 200));
			notifierTextPane.setForeground(new Color(0, 106, 107));
		}
		contentPane.add(NotifierscrollPane, CC.xy(3, 27));

		clientTaskManager = new ClientTaskManager(this,
				ResourceInformationLabel1, ResourceInformationTabbedPane1);

		clientTaskManager.start();

		setLocationRelativeTo(getOwner());
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void setNotifierText(String text) {
		if (notifierTextPane.getText().length() > 200) {
			notifierTextPane.setText("");
		}
		String tmpText = notifierTextPane.getText();
		notifierTextPane.setText(tmpText + "\n~ " + text);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void RegisterButtonActionPerformed(ActionEvent e) {

		serverIP = JOptionPane.showInputDialog("Enter server IP:");
		socketPort = Integer.parseInt(JOptionPane
				.showInputDialog("Enter socket port:"));
		client.setNewClientFrame(this);

		/*
		 * if (System.getSecurityManager() == null) {
		 * System.setSecurityManager(new RMISecurityManager()); }
		 */
		String gisServer = "//" + serverIP + ":" + socketPort + "/GISServer";
		try {
			RemoteServer remoteServerObject = (RemoteServer) Naming
					.lookup(gisServer);
			client.setRemoteServer(remoteServerObject);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}

		client.start();
		client.setMode(Mode.normal);
		RegisterButton.setEnabled(false);
		RegisterButton.setText("Registered");

		PauseButton.setEnabled(true);

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@SuppressWarnings("deprecation")
	public void PauseButtonActionPerformed(ActionEvent e) {
		client.suspend();
		// client.stop();
		PauseButton.setText("Paused");
		PauseButton.setEnabled(false);
		ResumeButton.setEnabled(true);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	@SuppressWarnings("deprecation")
	public void ResumeButtonActionPerformed(ActionEvent e) {
		client.resume();
		// client.start();
		ResumeButton.setEnabled(false);
		PauseButton.setText("Pause");
		PauseButton.setEnabled(true);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public void help_AboutMenuItemActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, "GIS Connection Manager" + "\n"
				+ "Version C.2.0");
	}

	public int getIntervalSeconds() {
		return intervalSeconds;
	}

	public void setIntervalSeconds(int intervalSeconds) {
		this.intervalSeconds = intervalSeconds;
	}

	public int getSimulationMinutes() {
		return simulationMinutes;
	}

	public void setSimulationMinutes(int simulationMinutes) {
		this.simulationMinutes = simulationMinutes;
	}

	public JTextPane getNotifierTextPane() {
		return notifierTextPane;
	}

	public void setNotifierTextPane(JTextPane notifierTextPane) {
		this.notifierTextPane = notifierTextPane;
	}

}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
class ClientTaskManager extends Thread {
	private int localIntervalSeconds;
	private ClientFrame clientFrame;

	private JLabel ResourceInformationLabel1;
	private JTabbedPane ResourceInformationTabbedPane1;

	public ClientTaskManager(ClientFrame newJFrame, JLabel newJLabel,
			JTabbedPane newJTabbedPane) {
		localIntervalSeconds = 1;
		clientFrame = newJFrame;
		ResourceInformationLabel1 = newJLabel;
		ResourceInformationTabbedPane1 = newJTabbedPane;
	}

	public void run() {
		ResourceInformationTabbedPane1.setVisible(true);
		ResourceInformationLabel1.setVisible(true);
		clientFrame.setStaticInfo();
		clientFrame.setDynamicInfo();
		while (true) {
			try {
				Thread.sleep(localIntervalSeconds * 1000, 0);
			} catch (InterruptedException e) {
				// e.printStackTrace();
				// TODO - Error handling
				JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
			}
			clientFrame.setDynamicInfo();
		}
	}

	@SuppressWarnings("deprecation")
	public void ClientTaskManagerStop() {
		ResourceInformationTabbedPane1.setVisible(false);
		ResourceInformationLabel1.setVisible(false);
		this.stop();
	}
}