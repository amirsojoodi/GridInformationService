package gui;

import gis.Mode;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import net.Client;
import net.RemoteServer;

public class NormalClient {
	private Client client;
	private String serverIP;
	private int socketPort;
	private Scanner scanner;
	
	public NormalClient() {
		scanner = new Scanner(System.in);
		client = new Client();
		client.setMode(Mode.silent);
		
	}
	
	public void runNormalClient(){
		
		System.out.println("Enter server IP: ");
		serverIP = scanner.nextLine();
		System.out.println("Enter socket port: ");
		socketPort = scanner.nextInt();

		String gisServer = "//" + serverIP + ":" + socketPort + "/GISServer";
		try {
			RemoteServer remoteServerObject = (RemoteServer) Naming
					.lookup(gisServer);
			client.setRemoteServer(remoteServerObject);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			// e1.printStackTrace();
			// TODO - Error handling
			System.out.println("Error\n" + e1.getMessage());
		}

		client.start();
	}
}
