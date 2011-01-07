/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.GregorianCalendar;
import java.util.UUID;
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

		sort();
	}


	private static void sort() {
		SortServer server = null;
		try {
			server = SortServer.getInstance();
		} catch (RemoteException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

		server.setBlockSize(3);
		//server.setSorter(new MergeSorter(server));
		server.setSorter(new DistributionSorter(server));

		staticData(server);
		//randomData(server, 1024);

		long start = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("sorting:" + server.getList());
		server.sort();
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("finished in "+ (end - start) + "ms -> " + server.getList());

	}

	private static void staticData(SortServer server) {
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
	}

	private static void randomData(SortServer server, int count) {
		for (int i = 0; i < count; i++) {
			server.add(UUID.randomUUID().toString());
		}
	}
}
