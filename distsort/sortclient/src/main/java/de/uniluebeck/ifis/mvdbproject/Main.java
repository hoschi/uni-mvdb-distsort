/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoschi
 */
public class Main {

	public static void main(String[] args) throws RemoteException {
		String hostname = "blubb";
		if (args.length > 0) {
			hostname = args[0];
		}

		System.out.println("note: first arg is hostname!");
		System.out.println("adding me to server on //" + hostname + "/sortserver");
		try {
			System.out.println("\tif this is localhost or 127.0.0.1 this won't work over a real network -> " + java.net.InetAddress.getLocalHost());
		} catch (UnknownHostException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		ISortServer stub = null;
		try {
			stub = (ISortServer) Naming.lookup("rmi://" + hostname + "/sortserver");
			stub.addClient(new SortClient());
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}

		System.out.println("SUCCESS!");
	}
}
