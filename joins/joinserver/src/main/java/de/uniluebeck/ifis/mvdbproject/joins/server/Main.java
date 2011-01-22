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

	static int rows = 1000;
	static Relation r, s;

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
		/*
		generateRTest();
		generateSTest();

		joinShipWholeTest((JoinServer) server);
		joinFetchAsNeededTest((JoinServer) server);
		joinSemiVersion1Test((JoinServer) server);
		joinSemiVersion2Test((JoinServer) server);
		joinSemiVersion3Test((JoinServer) server);
		 */
		System.out.println("generate data");
		generateR();
		generateS();
		System.out.println("generate data - finished");
		System.out.println();
		System.out.println();
		System.out.println();

		joinShipWhole((JoinServer) server);
		//joinFetchAsNeeded((JoinServer) server);
		joinSemiVersion1((JoinServer) server);
		joinSemiVersion2((JoinServer) server);
		joinSemiVersion3((JoinServer) server);

	}

	private static void joinShipWholeTest(JoinServer server) throws RemoteException {
		System.out.println("joinShipWholeTest");
		server.startMeasurements();

		Relation test = server.joinShipWhole(r, s);
		System.out.print(test.toString());
		System.out.println();
		System.out.println();
		server.printLastMeasurementsWithDetails();

	}

	private static void joinFetchAsNeededTest(JoinServer server) throws RemoteException {
		System.out.println("joinFetchAsNeededTest");
		server.startMeasurements();

		Relation test = server.joinFetchAsNeeded(r, s);
		System.out.print(test.toString());
		System.out.println();
		System.out.println();
		server.printLastMeasurementsWithDetails();

	}

	private static void joinSemiVersion1Test(JoinServer server) throws RemoteException {
		System.out.println("joinSemiVersion1Test");
		server.startMeasurements();

		Relation test = server.joinSemiVersion1(r, s);
		System.out.print(test.toString());
		System.out.println();
		System.out.println();
		server.printLastMeasurementsWithDetails();

	}

	private static void joinSemiVersion2Test(JoinServer server) throws RemoteException {
		System.out.println("joinSemiVersion2Test");
		server.startMeasurements();

		Relation test = server.joinSemiVersion2(r, s);
		System.out.print(test.toString());
		System.out.println();
		System.out.println();
		server.printLastMeasurementsWithDetails();
	}

	private static void joinSemiVersion3Test(JoinServer server) throws RemoteException {
		System.out.println("joinSemiVersion3Test");
		server.startMeasurements();

		Relation test = server.joinSemiVersion3(r, s);
		System.out.print(test.toString());
		System.out.println();
		System.out.println();
		server.printLastMeasurementsWithDetails();

		System.out.println("r has " + r.getRowCount() + " rows");
		System.out.println("s has " + s.getRowCount() + " rows");
		System.out.println("joined has " + test.getRowCount() + " rows");
	}

	private static void joinShipWhole(JoinServer server) throws RemoteException {
		System.out.println("=================================");
		System.out.println("joinShipWhole");
		System.out.println("=================================");
		server.startMeasurements();

		Relation test = server.joinShipWhole(r, s);
		server.printLastMeasurements();

		System.out.println("r has " + r.getRowCount() + " rows");
		System.out.println("s has " + s.getRowCount() + " rows");
		System.out.println("joined has " + test.getRowCount() + " rows");

//		System.out.println(r.toString());
//		System.out.println(s.toString());
//		System.out.println(test.toString());

	}

	private static void joinFetchAsNeeded(JoinServer server) throws RemoteException {
		System.out.println("=================================");
		System.out.println("joinFetchAsNeeded");
		System.out.println("=================================");
		server.startMeasurements();

		Relation test = server.joinFetchAsNeeded(r, s);
		server.printLastMeasurements();

//		System.out.println("r has " + r.getRowCount() + " rows");
//		System.out.println("s has " + s.getRowCount() + " rows");
//		System.out.println("joined has " + test.getRowCount() + " rows");

	}

	private static void joinSemiVersion1(JoinServer server) throws RemoteException {
		System.out.println("=================================");
		System.out.println("joinSemiVersion1");
		System.out.println("=================================");
		server.startMeasurements();

		Relation test = server.joinSemiVersion1(r, s);
		server.printLastMeasurements();

		System.out.println("r has " + r.getRowCount() + " rows");
		System.out.println("s has " + s.getRowCount() + " rows");
		System.out.println("joined has " + test.getRowCount() + " rows");

//		System.out.println(r.toString());
//		System.out.println(s.toString());
//		System.out.println(test.toString());
	}

	private static void joinSemiVersion2(JoinServer server) throws RemoteException {
		System.out.println("=================================");
		System.out.println("joinSemiVersion2");
		System.out.println("=================================");
		server.startMeasurements();

		Relation test = server.joinSemiVersion2(r, s);
		server.printLastMeasurements();

		System.out.println("r has " + r.getRowCount() + " rows");
		System.out.println("s has " + s.getRowCount() + " rows");
		System.out.println("joined has " + test.getRowCount() + " rows");

//		System.out.println(r.toString());
//		System.out.println(s.toString());
//		System.out.println(test.toString());
	}

	private static void joinSemiVersion3(JoinServer server) throws RemoteException {
		System.out.println("=================================");
		System.out.println("joinSemiVersion3");
		System.out.println("=================================");
		server.startMeasurements();

		Relation test = server.joinSemiVersion3(r, s);
		server.printLastMeasurements();

		System.out.println("r has " + r.getRowCount() + " rows");
		System.out.println("s has " + s.getRowCount() + " rows");
		System.out.println("joined has " + test.getRowCount() + " rows");
		
//		System.out.println(r.toString());
//		System.out.println(s.toString());
//		System.out.println(test.toString());
	}

	private static void generateR() {
		r = new Relation("r");
		r.addColumn("a");
		r.addColumn("j");

		for (int i = 0; i < rows; ++i) {
			List<String> list = randomData(2, rows);
			r.addRow(list);
			//System.out.print("*");
		}
	}

	private static void generateS() {
		s = new Relation("s");
		s.addColumn("b");
		s.addColumn("j");

		for (int i = 0; i < rows; ++i) {
			List<String> list = randomData(2, rows);
			s.addRow(list);
			//System.out.print("*");
		}
	}

	private static void generateRTest() {
		r = new Relation("r");
		r.addColumn("a");
		r.addColumn("j");

		List<String> row = new ArrayList<String>();
		row.add("1");
		row.add("1");
		r.addRow(row);
		row.clear();

		row.add("2");
		row.add("2");
		r.addRow(row);
		row.clear();

		// add rows that don't join
		row.add("3");
		row.add("-1");
		r.addRow(row);
		row.clear();

		row.add("3");
		row.add("-2");
		row.clear();

		row.add("4");
		row.add("-3");
		r.addRow(row);
		row.clear();

		row.add("4");
		row.add("-4");
		row.clear();
	}

	private static void generateSTest() {
		s = new Relation("s");
		s.addColumn("b");
		s.addColumn("j");

		List<String> row = new ArrayList<String>();
		row.add("1");
		row.add("1");
		s.addRow(row);
		row.clear();

		row.add("2");
		row.add("2");
		s.addRow(row);
		row.clear();

		// add rows that don't join
		row.add("3");
		row.add("-1");
		row.clear();

		row.add("3");
		row.add("-2");
		s.addRow(row);
		row.clear();

		row.add("4");
		row.add("-3");
		row.clear();

		row.add("4");
		row.add("-4");
		s.addRow(row);
		row.clear();
	}

	private static List<String> randomData(int count, int border) {
		List<String> list = new ArrayList<String>();

		Random rand = new Random();
		for (int i = 0; i
				< count; i++) {
			list.add(new Integer(rand.nextInt(border)).toString());


		}
		return list;

	}
}
