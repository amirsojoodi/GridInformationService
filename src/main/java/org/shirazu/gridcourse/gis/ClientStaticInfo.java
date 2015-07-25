package gis;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class ClientStaticInfo {

	private static Sigar sigar = new Sigar();
	NumberFormat formatter = new DecimalFormat("#0.00");

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getCPUSpeed() {

		CpuInfo[] cpuInfoList = null;
		try {
			cpuInfoList = sigar.getCpuInfoList();
		} catch (SigarException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}

		double cpuSpeed = cpuInfoList[0].getMhz();
		return formatter.format(cpuSpeed);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getCPUModel() {
		String model;
		CpuInfo[] cpuInfoList = null;
		try {
			cpuInfoList = sigar.getCpuInfoList();
		} catch (SigarException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e1.getMessage());
		}

		model = cpuInfoList[0].getModel();

		return model;
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
	public String getRAMInfo() {

		Mem mem = null;
		try {
			mem = sigar.getMem();
		} catch (SigarException se) {
			// se.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + se.getMessage());
		}

		double totalmemory = mem.getTotal() / 1024.0 / 1024.0;

		return formatter.format(totalmemory);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getSStorageInfo() {

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
		for (int i = 0; i < fileSystemlist.length; i++) {
			try {
				filesystemusage = sigar.getFileSystemUsage(fileSystemlist[i]
						.getDirName());
				totalStorage += filesystemusage.getTotal();
			} catch (SigarException e) {
				// e.printStackTrace();
				// System.out
				// .println("Error occurred in get total storage information..");
				// TODO - Error handling
				// JOptionPane.showMessageDialog(null, "Error\n" +
				// e.getMessage());
			}

			filesystemusage.getFree();
			filesystemusage.getUsed();
		}
		totalStorage = totalStorage / 1024 / 1024;

		return formatter.format(totalStorage);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String getOSInfo() {

		String osInfo = System.getProperty("os.name") + " "
				+ System.getProperty("os.version") + " "
				+ System.getProperty("os.arch");
		return osInfo;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public Attribute getNetworkInfo() {
		// TODO - get network information
		return null;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public Attribute getGPUInfo() {
		// TODO - get GPU information
		return null;
	}

	public String getIP() {
		try {
			return InetAddress.getLoopbackAddress().toString();
		} catch (Exception e) {
			// e.printStackTrace();
			// TODO - Error handling
			// JOptionPane.showMessageDialog(null, "Error\n" + e.getMessage());
			return null;
		}
	}
}
