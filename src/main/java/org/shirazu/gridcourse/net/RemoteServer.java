package org.shirazu.gridcourse.net;

//============================================================================
//Author      : Fatemeh Marzban, Amir Hossein Sojoodi
//Version     : 0.0.1
//Year        : 2014
//Copyright   : GNU
//Description : Grid Information Service in Java
//============================================================================

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

	public long saveStaticData(byte[] staticInfoByteArray) throws RemoteException;

	public boolean saveDynamicData(byte[] dynamicInfoByteArray) throws RemoteException;

	public int getIntervalSeconds() throws RemoteException;

	public int getSimulationMinutes() throws RemoteException;
}
