package de.uniluebeck.ifis.mvdbproject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SortClient extends UnicastRemoteObject implements ISortClient {

	private enum State {

		ready, sort, getback
	};
	private List<String> list;
	private State state;

	protected SortClient() throws RemoteException {
		super();
		list = new ArrayList<String>();
		state = State.ready;
	}

	@Override
	public List<String> sort(final List<String> list) throws RemoteException {
		List<String> myList = new ArrayList<String>(list);
		Collections.sort(myList, String.CASE_INSENSITIVE_ORDER);
		return myList;
	}

	@Override
	public void add(List<String> unsorted) throws RemoteException {
		while (state != State.ready) {
			System.out.print(".");// wait
			try {
				Thread.currentThread().sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(SortClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("adding ... " + GregorianCalendar.getInstance().getTime());
		this.list.addAll(unsorted);
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("finished in " + (end - start) + "ms -> ");
		state = State.sort;
	}

	@Override
	public void sort() throws RemoteException {
		while (state != State.sort) {
			System.out.print(".");// wait
			try {
				Thread.currentThread().sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(SortClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("delegating ... " + GregorianCalendar.getInstance().getTime());

		Sorter s = new Sorter();
		s.setClient(this);
		Thread t = new Thread(s);
		t.start();

		long end = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("finished in " + (end - start) + "ms -> ");
	}

	private class Sorter implements Runnable {

		SortClient client;

		public void setClient(SortClient client) {
			this.client = client;
		}

		@Override
		public void run() {
			long start = GregorianCalendar.getInstance().getTimeInMillis();
			System.out.println("sorting ... " + GregorianCalendar.getInstance().getTime());
			Collections.sort(client.list, String.CASE_INSENSITIVE_ORDER);
			long end = GregorianCalendar.getInstance().getTimeInMillis();
			System.out.println("finished in " + (end - start) + "ms -> ");
			client.state = State.getback;

		}
	}

	private class Adder implements Runnable {

		SortClient client;

		public void setClient(SortClient client) {
			this.client = client;
		}

		@Override
		public void run() {
			long start = GregorianCalendar.getInstance().getTimeInMillis();
			System.out.println("adding ... " + GregorianCalendar.getInstance().getTime());
			//client.list.addAll(unsorted);
			long end = GregorianCalendar.getInstance().getTimeInMillis();
			System.out.println("finished in " + (end - start) + "ms -> ");
			client.state = State.sort;

		}
	}

	@Override
	public List<String> getSortedBlock(int blockSize) throws RemoteException {
		while (state != State.getback) {
			System.out.print(".");// wait
			try {
				Thread.currentThread().sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(SortClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println(".");
		List<String> block = null;
		if (blockSize <= this.list.size()) {
			block = new ArrayList<String>(this.list.subList(0, blockSize));
			for (int i = 0; i < blockSize; ++i) {
				this.list.remove(0);
			}
		} else if (this.list.size() > 0) { // last one, if odd
			block = new ArrayList<String>(this.list);
			this.list.clear();
		} else {
			this.list.clear();
			state = State.ready;
		}
		return block;
	}
}
