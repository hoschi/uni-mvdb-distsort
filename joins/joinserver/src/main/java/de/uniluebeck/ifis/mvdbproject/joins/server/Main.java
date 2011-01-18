/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.server;

import de.uniluebeck.ifis.mvdbproject.joins.shared.IJoinServer;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoschi
 */
public class Main {

	public static void main(String[] args) throws RemoteException {
		final IJoinServer server;
		System.out.println("starting server on //localhost/joinserver");
		try {
			final Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

			server = new JoinServer();
			Naming.rebind("joinserver", server);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
		try {
			System.out.println(java.net.InetAddress.getLocalHost());
		} catch (UnknownHostException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);


		}

		System.out.println("The server is running!");

		if (args.length == 1) {
			Integer i = new Integer(args[0]);

			while (i > 0) {
				System.out.println("waiting for nodes - " + i + "s");
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException ex) {
					Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
				}
				--i;
			}
		}
		System.out.println("started");
		//joinShipWholeTest((JoinServer) server);
		joinShipWhole((JoinServer) server);

	}

	private static void joinShipWholeTest(JoinServer server) throws RemoteException {
		server.startMeasurements();
		Relation r, s;
		r = new Relation("r");
		r.addColumn("a");
		r.addColumn("b");

		s = new Relation("s");
		s.addColumn("c");
		s.addColumn("d");

		List<String> row = new ArrayList<String>();
		row.add("1");
		row.add("1");
		r.addRow(row);
		s.addRow(row);
		row.clear();

		row.add("2");
		row.add("2");
		r.addRow(row);
		s.addRow(row);
		row.clear();

		// add rows that don't join
		row.add("3");
		row.add("-1");
		r.addRow(row);
		row.clear();

		row.add("3");
		row.add("-2");
		s.addRow(row);
		row.clear();

		row.add("4");
		row.add("-3");
		r.addRow(row);
		row.clear();

		row.add("4");
		row.add("-4");
		s.addRow(row);
		row.clear();
		Relation test = server.joinShipWhole(r, s, "b", "d");
		System.out.print(test.toString());
		server.printLastMeasurements();

	}

	private static void joinShipWhole(JoinServer server) throws RemoteException {
		server.startMeasurements();
		
		System.out.println("generate data");

		Relation r, s;
		r = new Relation("r");
		r.addColumn("a");
		r.addColumn("b");

		s = new Relation("s");
		s.addColumn("c");
		s.addColumn("d");

		int rows = 10000;
		for (int i = 0; i < rows; ++i) {
			List<String> list = randomData(2);
			r.addRow(list);
			System.out.print("*");
		}
		for (int i = 0; i < rows; ++i) {
			List<String> list = randomData(2);
			s.addRow(list);
			System.out.print("*");
		}

		System.out.println("generate data - finished");
		Relation test = server.joinShipWhole(r, s, "b", "d");
		server.printLastMeasurements();

		System.out.println("r has " + r.getRowCount() + " rows");
		System.out.println("s has " + s.getRowCount() + " rows");
		System.out.println("joined has " + test.getRowCount() + " rows");

	}

	private static List<String> randomData(int count) {
		List<String> list = new ArrayList<String>();

		Random rand = new Random();
		for (int i = 0; i
				< count; i++) {
			list.add(new Integer(rand.nextInt(count)).toString());


		}
		return list;

	}
}
