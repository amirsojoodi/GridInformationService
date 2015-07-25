package org.shirazu.gridcourse.net;

//============================================================================
//Author      : Fatemeh Marzban, Amir Hossein Sojoodi
//Version     : 0.0.1
//Year        : 2014
//Copyright   : GNU
//Description : Grid Information Service in Java
//============================================================================

import org.shirazu.gridcourse.gis.Attribute;
import org.shirazu.gridcourse.gis.AttributeType;
import org.shirazu.gridcourse.gis.ClientDynamicInfo;
import org.shirazu.gridcourse.gis.ClientStaticInfo;
import org.shirazu.gridcourse.gis.Mode;
import org.shirazu.gridcourse.gis.Node;
import org.shirazu.gridcourse.gis.Resource;
import org.shirazu.gridcourse.gis.ResourceType;
import org.shirazu.gridcourse.gui.ClientFrame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.shirazu.gridcourse.stax.StaxWriter;

public class Client extends Thread {

	private static final int defultIntervalSeconds = 30;

	Mode mode;

	private int intervalSeconds;
	private int simulationMinutes;
	private Node node;
	private long nodeID;
	private String nodeIP;
	private String nodeName;
	private ClientFrame newClientFrame;
	private ClientStaticInfo clientStaticInfo;
	private ClientDynamicInfo clientDynamicInfo;

	private RemoteServer remoteServer;

	private static final String defultFileToSend = "Node.xml";
	private StaxWriter staxWriter;
	private String fileToSend;
	private boolean serverStatus;
	private byte[] byteArray;

	public Client() {
		clientStaticInfo = new ClientStaticInfo();
		clientDynamicInfo = new ClientDynamicInfo();
		setFileToSend(defultFileToSend);
		setIntervalSeconds(defultIntervalSeconds);

		setNodeID(0);
		staxWriter = new StaxWriter();
		staxWriter.setFile(getFileToSend());
		node = new Node();

		serverStatus = true;
	}

	@SuppressWarnings("deprecation")
	public void clientStop() {
		this.stop();
	}

	@Override
	public void run() {
		printOutput(getMode(), "Sending static data to server");
		Path path;
		try {
			// ~~~ Set simulation time and time intervals
			setIntervalSeconds(remoteServer.getIntervalSeconds());
			setSimulationMinutes(remoteServer.getSimulationMinutes());
			printOutput(getMode(), "Interval Seconds = " + getIntervalSeconds());

			// ~~~ Save static information and set ID
			node.setResources(getStaticInfo());
			node.setId(0);
			node.setName(System.getProperty("os.name") + " "
					+ System.getProperty("os.arch"));

			staxWriter.saveNode(node);

			path = Paths.get(fileToSend);
			byteArray = Files.readAllBytes(path);

			setNodeID(remoteServer.saveStaticData(byteArray));
			node.setId(nodeID);

			printOutput(getMode(), "Get new ID = " + getNodeID());

			while (serverStatus) {

				sleep(intervalSeconds * 1000, 0);

				printOutput(getMode(), "Sending heart beat.");

				node.setResources(getDynamicInfo());
				staxWriter.saveNode(node);
				path = Paths.get(fileToSend);
				byteArray = Files.readAllBytes(path);
				serverStatus = remoteServer.saveDynamicData(byteArray);
			}
		} catch (RemoteException e) {
			// e.printStackTrace();
			// TODO - Error handling
			printError(getMode(), e.getMessage());
		} catch (IOException e) {
			// e.printStackTrace();
			// TODO - Error handling
			printError(getMode(), e.getMessage());
		} catch (InterruptedException e) {
			// e.printStackTrace();
			// TODO - Error handling
			printError(getMode(), e.getMessage());
		}
		printOutput(getMode(), "Server sent end signal..");
		printOutput(getMode(), "Monitoring ended");
	}

	public ArrayList<Resource> getStaticInfo() {
		// Create a list of resources
		ArrayList<Resource> resources = new ArrayList<Resource>();

		// ~~~ CPU
		Resource cpuResource = new Resource();
		cpuResource.setResourceType(ResourceType.CPU);
		cpuResource.setName(clientStaticInfo.getCPUVendor() + " CPU");

		Attribute cpuSpeedAttribute = new Attribute();
		cpuSpeedAttribute.setAttributeType(AttributeType.CPUSpeed);
		cpuSpeedAttribute.setValue(Double.parseDouble(clientStaticInfo
				.getCPUSpeed()));
		cpuSpeedAttribute.setDate(new Date());
		cpuResource.addAttribute(cpuSpeedAttribute);

		resources.add(cpuResource);
		// ~~~ RAM
		Resource ramResource = new Resource();
		ramResource.setResourceType(ResourceType.RAM);
		ramResource.setName("RAM");

		Attribute ramTotalAttribute = new Attribute();
		ramTotalAttribute.setAttributeType(AttributeType.TotalMemory);
		ramTotalAttribute.setValue(Double.parseDouble(clientStaticInfo
				.getRAMInfo()));
		ramTotalAttribute.setDate(new Date());
		ramResource.addAttribute(ramTotalAttribute);

		resources.add(ramResource);
		// ~~~ OS
		Resource osResource = new Resource();
		osResource.setResourceType(ResourceType.OS);
		osResource.setName(clientStaticInfo.getOSInfo());

		resources.add(osResource);
		// ~~~ SStorage
		Resource sstorageResource = new Resource();
		sstorageResource.setResourceType(ResourceType.SStorage);
		sstorageResource.setName("SSTORAGE");

		Attribute sstorageTotalAttribute = new Attribute();
		sstorageTotalAttribute.setAttributeType(AttributeType.TotalStorage);
		sstorageTotalAttribute.setValue(Double.parseDouble(clientStaticInfo
				.getSStorageInfo()));
		sstorageTotalAttribute.setDate(new Date());
		sstorageResource.addAttribute(sstorageTotalAttribute);

		resources.add(sstorageResource);

		return resources;
	}

	public ArrayList<Resource> getDynamicInfo() {
		// Create a list of resources
		ArrayList<Resource> resources = new ArrayList<Resource>();
		// ~~~ CPU
		Resource cpuResource = new Resource();
		cpuResource.setResourceType(ResourceType.CPU);
		cpuResource.setName(clientDynamicInfo.getCPUVendor() + " CPU");

		Attribute cpuUtilizationAttribute = new Attribute();
		cpuUtilizationAttribute.setAttributeType(AttributeType.CPUUtilization);
		cpuUtilizationAttribute.setValue(Double.parseDouble(clientDynamicInfo
				.getCPUUsage()));
		cpuUtilizationAttribute.setDate(new Date());
		cpuResource.addAttribute(cpuUtilizationAttribute);

		resources.add(cpuResource);
		// ~~~ RAM
		Resource ramResource = new Resource();
		ramResource.setResourceType(ResourceType.RAM);
		ramResource.setName("RAM");

		Attribute ramFreeAttribute = new Attribute();
		ramFreeAttribute.setAttributeType(AttributeType.FreeMemory);
		ramFreeAttribute.setValue(Double.parseDouble(clientDynamicInfo
				.getRAMFreeInfo()));

		ramResource.addAttribute(ramFreeAttribute);

		ramFreeAttribute.setDate(new Date());

		Attribute ramUtilizationAttribute = new Attribute();
		ramUtilizationAttribute
				.setAttributeType(AttributeType.MemoryUtilization);
		ramUtilizationAttribute.setValue(Double.parseDouble(clientDynamicInfo
				.getRAMUtilizationInfo()));
		ramResource.addAttribute(ramUtilizationAttribute);

		resources.add(ramResource);
		// ~~~ SStorage
		Resource sstorageResource = new Resource();
		sstorageResource.setResourceType(ResourceType.SStorage);
		sstorageResource.setName("SSTORAGE");

		Attribute sstorageFreeAttribute = new Attribute();
		sstorageFreeAttribute.setAttributeType(AttributeType.FreeStorage);
		sstorageFreeAttribute.setValue(Double.parseDouble(clientDynamicInfo
				.getSStorageFreeInfo()));
		sstorageFreeAttribute.setDate(new Date());
		sstorageResource.addAttribute(sstorageFreeAttribute);

		Attribute sstorageUtilizationAttribute = new Attribute();
		sstorageUtilizationAttribute
				.setAttributeType(AttributeType.StorageUtilization);
		sstorageUtilizationAttribute.setValue(Double
				.parseDouble(clientDynamicInfo.getSStorageUtilizationInfo()));
		sstorageUtilizationAttribute.setDate(new Date());
		sstorageResource.addAttribute(sstorageUtilizationAttribute);

		resources.add(sstorageResource);

		return resources;
	}

	public void printOutput(Mode newMode, String message) {
		if (newMode.equals(Mode.silent)) {
			System.out.println(message);
		} else if (newMode.equals(Mode.normal)) {
			newClientFrame.setNotifierText(message);
		}
	}

	public void printError(Mode newMode, String errorMessage) {
		if (newMode.equals(Mode.silent)) {
			System.out.println(errorMessage);
		} else if (newMode.equals(Mode.normal)) {
			JOptionPane.showMessageDialog(null, "Error\n" + errorMessage);
		}
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

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public long getNodeID() {
		return nodeID;
	}

	public void setNodeID(long nodeID) {
		this.nodeID = nodeID;
	}

	public String getNodeIP() {
		return nodeIP;
	}

	public void setNodeIP(String nodeIP) {
		this.nodeIP = nodeIP;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public ClientFrame getNewClientFrame() {
		return newClientFrame;
	}

	public void setNewClientFrame(ClientFrame newClientFrame) {
		this.newClientFrame = newClientFrame;
	}

	public RemoteServer getRemoteServer() {
		return remoteServer;
	}

	public void setRemoteServer(RemoteServer remoteServer) {
		this.remoteServer = remoteServer;
	}

	public String getFileToSend() {
		return fileToSend;
	}

	public void setFileToSend(String fileToSend) {
		this.fileToSend = fileToSend;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

}