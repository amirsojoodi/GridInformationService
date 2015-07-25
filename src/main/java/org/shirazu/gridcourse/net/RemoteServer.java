package net;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

	public long saveStaticData(byte[] staticInfoByteArray) throws RemoteException;

	public boolean saveDynamicData(byte[] dynamicInfoByteArray) throws RemoteException;

	public int getIntervalSeconds() throws RemoteException;

	public int getSimulationMinutes() throws RemoteException;
}
