/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoschi
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("starting server on //localhost/sortserver");
		try {
			final Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

			final ISortServer server = SortServer.getInstance();
			Naming.rebind("sortserver", server);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
		System.out.println("The server is running!");

		mergesort();
	}

	private static void mergesort() {
		SortServer server = null;
		try {
			server = SortServer.getInstance();
		} catch (RemoteException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

		server.setBlockSize(4);
		server.setSorter(new MergeSorter(server));
		
		server.add("a");
		server.add("d");
		server.add("c");
		server.add("b");
		server.add("a");
		server.add("d");
		server.add("c");
		server.add("b");
		server.add("a");
		server.add("d");
		server.add("c");
		server.add("b");
		server.add("a");
		server.add("d");
		server.add("c");
		server.add("b");

		server.sort();
		System.out.println(server.getList());

	}
}
