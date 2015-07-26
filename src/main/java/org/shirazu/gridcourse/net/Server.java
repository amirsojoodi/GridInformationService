package org.shirazu.gridcourse.net;

//============================================================================
//Author      : Fatemeh Marzban, Amir Hossein Sojoodi
//Version     : 0.0.1
//Year        : 2014
//Copyright   : GNU
//Description : Grid Information Service in Java
//============================================================================

import org.shirazu.gridcourse.gis.Attribute;
import org.shirazu.gridcourse.gis.Node;
import org.shirazu.gridcourse.gis.Resource;
import org.shirazu.gridcourse.gui.ServerFrame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

import org.shirazu.gridcourse.stax.StaxParser;

public class Server extends UnicastRemoteObject implements RemoteServer {

	private static final long serialVersionUID = 1L;
	private static final int defaultSimulationMinutes = 60;
	private static final int defaultIntervalSeconds = 5;
	private static final String defaultFileToReceive = "ReceivedNode.xml";

	// 30 minutes
	private static final long defaultMonitorWindowMillisec = 60000/* 1800000 */;

	private EntityManagerFactory emf;
	private static EntityManager em;

	private String fileToReceive;
	private Node node;
	private StaxParser staxParser;

	// private volatile boolean stopFlag;
	private ServerFrame serverFrame;
	private static int simulationMinutes;
	private static int intervalSeconds;
	private long firstRunTime;
	private long firstAvailableDataTime;
	private long monitorWindowMilliseconds;
	private boolean serverStatus;

	private Thread watcher;

	public Server() throws RemoteException {
		super(0);
		setEmf(Persistence.createEntityManagerFactory("$objectdb/db/GIS.odb"));
		setEm(emf.createEntityManager());

		setSimulationMinutes(defaultSimulationMinutes);
		setIntervalSeconds(defaultIntervalSeconds);
		setMonitorWindowMilliseconds(defaultMonitorWindowMillisec);
		setFileToReceive(defaultFileToReceive);
		staxParser = new StaxParser();
		setServerStatus(true);
	}

	@SuppressWarnings("deprecation")
	public void startMonitoring() throws RemoteException {

		// setEmf(Persistence.createEntityManagerFactory("$objectdb/db/GIS.odb"));
		// setEm(emf.createEntityManager());

		serverFrame.setNotifierText("Simulation Minutes = "
				+ getSimulationMinutes());
		serverFrame.setNotifierText("Interval Seconds = "
				+ getIntervalSeconds());
		watcher = new Thread(new Runnable() {
			@Override
			public void run() {
				while (getServerStatus() == true) {
					try {
						Thread.sleep(getMonitorWindowMilliseconds(), 0);
					} catch (InterruptedException e) {
						// e.printStackTrace();
						// TODO - Error handling
						JOptionPane.showMessageDialog(null,
								"Error\n" + e.getMessage());
					}
					em.getTransaction().begin();
					String queryStatement = "delete FROM Attribute a WHERE a.date < :date";

					// serverFrame.setNotifierText("Deleting records before: "
					// + new Date(getFirstAvailableDataTime()));

					TypedQuery<Attribute> queryAttr = em.createQuery(
							queryStatement, Attribute.class);

					queryAttr.setParameter("date",
							new Date(getFirstAvailableDataTime()),
							TemporalType.DATE).executeUpdate();

					em.getTransaction().commit();
				}

			}
		});

		long start = System.currentTimeMillis();
		int elapsedMinutes;
		int remainingTime = getSimulationMinutes();
		while (remainingTime > 0) {

			try {
				Thread.sleep(getIntervalSeconds() * 2000, 0);
			} catch (InterruptedException e) {
				// e.printStackTrace();
				// TODO - Error handling
				JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
			}

			elapsedMinutes = (int) ((System.currentTimeMillis() - start) / 60000);

			remainingTime = getSimulationMinutes() - elapsedMinutes;
			if (elapsedMinutes * 60000 > monitorWindowMilliseconds) {
				setFirstAvailableDataTime(System.currentTimeMillis()
						- monitorWindowMilliseconds);
			}
		}

		setServerStatus(false);
		serverFrame.setNotifierText("Simulation Time has ended..");

		try {
			Thread.sleep(getIntervalSeconds() * 1000, 0);
		} catch (InterruptedException e) {
			// e.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
		}

		watcher.stop();
		getEm().close();
		getEmf().close();
		serverFrame.resetStartButton();
	}

	// TODO
	@SuppressWarnings("deprecation")
	public void stopMonitoring() {
		watcher.stop();
		getEm().close();
		getEmf().close();
	}

	public long saveStaticData(byte[] staticInfoByteArray)
			throws RemoteException {

		String nodeIP = null;
		try {
			nodeIP = getClientHost();
		} catch (ServerNotActiveException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}

		serverFrame.setNotifierText("A new Client arrived, " + nodeIP);

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(getFileToReceive());
			fileOutputStream.write(staticInfoByteArray);
			fileOutputStream.close();
		} catch (IOException e) {
			// e.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
		}

		node = staxParser.readNode(fileToReceive);
		node.setIP(nodeIP);

		serverFrame.setNotifierText("ID = " + node.getId()
				+ " is set for IP = " + nodeIP);

		return node.getId();
	}

	public boolean saveDynamicData(byte[] dynamicInfoByteArray)
			throws RemoteException {
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(getFileToReceive());
			fileOutputStream.write(dynamicInfoByteArray);
			fileOutputStream.close();
		} catch (IOException e) {
			// e.printStackTrace();
			// TODO - Error handling
			JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
		}

		node = staxParser.readNode(fileToReceive);

		serverFrame.setNotifierText("Node ID = " + node.getId() + " and IP = "
				+ node.getIP());

		return getServerStatus();
	}

	@SuppressWarnings("unchecked")
	public List<Resource> getResourcesFromDB() {
		List<Resource> resources;

		em.getTransaction().begin();

		resources = em.createQuery("SELECT r FROM Resource r").getResultList();

		em.getTransaction().commit();

		return resources;
	}

	public void clearDataBase() {
		em.getTransaction().begin();
		TypedQuery<Resource> query = em.createQuery("delete from Resource",
				Resource.class);
		query.executeUpdate();
		TypedQuery<Node> queryNode = em.createQuery("delete from Node",
				Node.class);
		queryNode.executeUpdate();
		TypedQuery<Attribute> queryAttr = em.createQuery(
				"delete from Attribute", Attribute.class);
		queryAttr.executeUpdate();
		em.getTransaction().commit();
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public static EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		Server.em = em;
	}

	public int getSimulationMinutes() throws RemoteException {
		return simulationMinutes;
	}

	public void setSimulationMinutes(int simulationMinutes) {
		Server.simulationMinutes = simulationMinutes;
	}

	public int getIntervalSeconds() throws RemoteException {
		return intervalSeconds;
	}

	public void setIntervalSeconds(int intervalSeconds) {
		Server.intervalSeconds = intervalSeconds;
	}

	public long getFirstRunTime() {
		return firstRunTime;
	}

	public void setFirstRunTime(long firstRunTime) {
		this.firstRunTime = firstRunTime;
	}

	public long getFirstAvailableDataTime() {
		return firstAvailableDataTime;
	}

	public void setFirstAvailableDataTime(long firstAvailableDataTime) {
		this.firstAvailableDataTime = firstAvailableDataTime;
	}

	public long getMonitorWindowMilliseconds() {
		return monitorWindowMilliseconds;
	}

	public void setMonitorWindowMilliseconds(long monitorWindowMilliseconds) {
		this.monitorWindowMilliseconds = monitorWindowMilliseconds;
	}

	public static int getDefultsimulationminutes() {
		return defaultSimulationMinutes;
	}

	public static int getDefultintervalseconds() {
		return defaultIntervalSeconds;
	}

	public ServerFrame getServerFrame() {
		return serverFrame;
	}

	public void setServerFrame(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}

	public String getFileToReceive() {
		return fileToReceive;
	}

	public void setFileToReceive(String fileToReceive) {
		this.fileToReceive = fileToReceive;
	}

	public boolean getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(boolean serverStatus) {
		this.serverStatus = serverStatus;
	}

}
