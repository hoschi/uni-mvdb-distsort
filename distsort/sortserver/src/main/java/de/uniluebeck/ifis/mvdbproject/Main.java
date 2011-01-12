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
import java.util.GregorianCalendar;
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
		//sort();
		sortPlotting();


	}

	private static long messuredSorting(SortServer server) {
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("sorting ...");
		server.sort();
		server.getSortedList();
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		long solution = end - start;
		//System.out.println("finished in " + (end - start) + "ms -> ");
		return solution;
	}

	private static void sort() {
		SortServer server = null;
		try {
			server = SortServer.getInstance();
		} catch (RemoteException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

		server.setBlockSize(10);
		//server.setSorter(new MergeSorter(server));
		server.setSorter(new DistributionSorter(server, new Random()));

		//staticData(server);
		randomData(server, 10);
		messuredSorting(server);

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
		/*input.add(10);
		input.add(30);
		input.add(70);
		input.add(100);
		input.add(200);
		input.add(300);
		input.add(400);
		input.add(500);
		input.add(600);
		input.add(700);
		input.add(800);
		input.add(900);
		input.add(1000);
		input.add(5000);
		input.add(7000);*/
		input.add(10000);
		input.add(30000);
		input.add(100000);
		//input.add(1000000);


		server.setBlockSize(1000);
		for (Integer in : input) {
			System.out.println("generating data");
			List<String> randomData = randomData(in);

			System.out.println("local");

			server.setSorter(new LocalSorter());
			long time = 0;
			server.setList(randomData);
			time = messuredSorting(server);
			timesLocal.add(time);


			System.out.println("merge");

			time = 0;
			server.setSorter(new MergeSorter(server));
			server.setList(randomData);
			time = messuredSorting(server);
			timesMerge.add(time);


			System.out.println("dist");
			time = 0;
/*			server.setSorter(new DistributionSorter(server, new Random()));
			server.setList(randomData);
			time = messuredSorting(server);*/
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
