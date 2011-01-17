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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoschi
 */
public class Main {
	private static Relation r, s;

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
		joinShipWhole((JoinServer) server);

	}

	private static void joinShipWhole(JoinServer server) throws RemoteException {
		setUp(r, s);
		Relation test = server.joinShipWhole(r, s, "b", "d");
		System.out.print(test.toString());

	}

	private static void setUp(Relation r, Relation s) {
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
	}
}
