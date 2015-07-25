package org.shirazu.gridcourse.gis;

//============================================================================
//Author      : Fatemeh Marzban, Amir Hossein Sojoodi
//Version     : 0.0.1
//Year        : 2014
//Copyright   : GNU
//Description : Grid Information Service in Java
//============================================================================

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class ClientDynamicInfo extends Thread {

	private static Sigar sigar = new Sigar();
	NumberFormat formatter = new DecimalFormat("#0.00");

	private long previousCPUIdle;
	private long previousCPUBusy;
	private Cpu cpu;

	public ClientDynamicInfo() {
		try {
			cpu = sigar.getCpuList()[0];
		} catch (SigarException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}
		previousCPUIdle = cpu.getIdle();
		previousCPUBusy = cpu.getSys() + cpu.getUser();
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getCPUVendor() {
		String model;
		CpuInfo[] cpuInfoList = null;
		try {
			cpuInfoList = sigar.getCpuInfoList();
		} catch (SigarException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}

		model = cpuInfoList[0].getVendor();

		return model;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getCPUUsage() {

		try {
			cpu = sigar.getCpu();/* sigar.getCpuList()[0]; */
		} catch (SigarException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}

		long idleTime = cpu.getIdle();
		long busyTime = cpu.getSys() + cpu.getUser();

		long idleInterval = idleTime - previousCPUIdle;
		long busyInterval = busyTime - previousCPUBusy;

		double totalUsage = ((double) busyInterval / (busyInterval + idleInterval)) * 100;

		previousCPUIdle = idleTime;
		previousCPUBusy = busyTime;

		return formatter.format(totalUsage);

		// getSystemCpuLoad()
		// Returns the "recent cpu usage" for the whole system.

		// CpuPerc cpuPerc = sigar.getCpuPerc();
		// cpuPerc.getCombined();

		// com.sun.management.OperatingSystemMXBean operatingSystemMXBean =
		// (com.sun.management.OperatingSystemMXBean) ManagementFactory
		// .getOperatingSystemMXBean();
		//
		// return formatter.format(operatingSystemMXBean.getSystemCpuLoad() *
		// 100);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getRAMFreeInfo() {

		Mem mem = null;
		try {
			mem = sigar.getMem();
		} catch (SigarException se) {
			// se.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + se.getMessage());
		}

		double freeMemory = mem.getActualFree() / 1024.0 / 1024.0;

		return formatter.format(freeMemory);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getRAMUtilizationInfo() {

		Mem mem = null;
		try {
			mem = sigar.getMem();
		} catch (SigarException se) {
			// se.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + se.getMessage());
		}

		double memoryUtilization = mem.getUsedPercent();

		return formatter.format(memoryUtilization);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getSStorageFreeInfo() {

		FileSystem[] fileSystemlist = null;
		try {
			fileSystemlist = sigar.getFileSystemList();
		} catch (SigarException e2) {
			// e2.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e2.getMessage());
		}

		FileSystemUsage filesystemusage = null;
		long totalAvailableStotage = 0;
		for (int i = 0; i < fileSystemlist.length; i++) {
			try {
				filesystemusage = sigar.getFileSystemUsage(fileSystemlist[i]
						.getDirName());
				totalAvailableStotage += filesystemusage.getFree();
			} catch (SigarException e) {
				// e.printStackTrace();
				// TODO - Error handling
				// JOptionPane.showMessageDialog(null,
				// "Error occurred in get free storage information..\n"
				// + e.getMessage());
			}
		}
		totalAvailableStotage = totalAvailableStotage / 1024 / 1024;
		return formatter.format(totalAvailableStotage);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getSStorageUtilizationInfo() {

		FileSystem[] fileSystemlist = null;
		try {
			fileSystemlist = sigar.getFileSystemList();
		} catch (SigarException e2) {
			// e2.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e2.getMessage());
		}

		FileSystemUsage filesystemusage = null;
		long totalStorage = 0;
		long totalUsedStorage = 0;
		double totalUsagePercent = 0;
		for (int i = 0; i < fileSystemlist.length; i++) {
			try {
				filesystemusage = sigar.getFileSystemUsage(fileSystemlist[i]
						.getDirName());
				totalStorage += filesystemusage.getTotal();
				totalUsedStorage += filesystemusage.getUsed();
			} catch (SigarException e) {
				// e.printStackTrace();
				// TODO - Error handling
				// JOptionPane.showMessageDialog(null,
				// "Error occurred in get storage utilization information.."
				//			+ e.getMessage());
			}

		}
		totalUsagePercent = (double) totalUsedStorage / totalStorage * 100.0;

		return formatter.format(totalUsagePercent);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public Attribute getNetworkInfo() {
		// TODO - get network information
		return null;
	}
}