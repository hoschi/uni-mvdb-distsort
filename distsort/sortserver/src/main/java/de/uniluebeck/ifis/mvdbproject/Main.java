/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
		try {
			System.out.println(java.net.InetAddress.getLocalHost());
		} catch (UnknownHostException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

		}
		System.out.println("The server is running!");

		if (args.length == 1) {
			Integer i = new Integer(args[0]);

			while (i > 0) {
				System.out.println("waiting for clients - " + i + "s");
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException ex) {
					Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
				}
				--i;
			}
		}
		sortPlotting();


	}

	private static long messuredSorting(SortServer server) {
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("sorting ...");
		server.sort();
		Iterator<String> iterator = server.iterator();
		while (iterator.hasNext()) {
			iterator.next();
		}
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		long solution = end - start;
		//System.out.println("finished in " + (end - start) + "ms -> ");
		return solution;
	}

	private static void sortPlotting() {
		SortServer server = null;
		try {
			server = SortServer.getInstance();
		} catch (RemoteException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

		List<Integer> input = new ArrayList<Integer>();
		List<Long> timesLocal = new ArrayList<Long>();
		List<Long> timesMerge = new ArrayList<Long>();
		List<Long> timesDist = new ArrayList<Long>();

		input.add(1000);
		input.add(5000);
		input.add(7000);
		input.add(10000);
		input.add(30000);
		input.add(50000);
		input.add(70000);
		input.add(100000);

		server.setBlockSize(100);
		for (Integer in : input) {
			System.out.println("generating data");
			List<String> randomData = randomData(in);
			List<String> copy = new ArrayList<String>();

			System.out.println("local");

			server.setSorter(new LocalSorter());
			long time = 0;
			copy = new ArrayList<String>();
			copy.addAll(randomData);
			server.setList(copy);
			time = messuredSorting(server);
			timesLocal.add(time);


			System.out.println("merge");

			time = 0;
			server.setSorter(new MergeSorter(server));
			copy = new ArrayList<String>();
			copy.addAll(randomData);
			server.setList(copy);
			time = messuredSorting(server);
			timesMerge.add(time);


			System.out.println("dist");
			time = 0;
			server.setSorter(new DistributionSorter(server, new Random()));
			copy = new ArrayList<String>();
			copy.addAll(randomData);
			server.setList(copy);
			time = messuredSorting(server);
			timesDist.add(time);
		}

		System.out.println(timesLocal);
		System.out.println(timesMerge);
		System.out.println(timesDist);
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
		Random rand = new Random();
		for (int i = 0; i
				< count; i++) {
			server.add(new Integer(rand.nextInt()).toString());


		}
	}

	private static List<String> randomData(int count) {
		List<String> list = new ArrayList<String>();

		Random rand = new Random();
		for (int i = 0; i
				< count; i++) {
			list.add(new Integer(rand.nextInt()).toString());


		}
		return list;

	}
}
