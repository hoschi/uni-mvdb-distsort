package de.uniluebeck.ifis.mvdbproject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SortServer extends UnicastRemoteObject implements ISortServer {

	/****************** Singleton stuff *******************/
	/**
	 * Privates Klassenattribut, wird beim erstmaligen Gebrauch (nicht beim
	 * Laden) der Klasse erzeugt
	 */
	private static SortServer instance;

	/** 
	 * Konstruktor ist privat, Klasse darf nicht von außen instanziiert
	 * werden. 
	 */
	private SortServer() throws RemoteException {
		super();
		this.list = new ArrayList();
		this.clients = new ArrayList();
	}

	/**
	 * Statische Methode "getInstance()" liefert die einzige Instanz der Klasse
	 * zurück. Ist synchronisiert und somit thread-sicher.
	 */
	public synchronized static SortServer getInstance() throws RemoteException {
		if (instance == null) {
			instance = new SortServer();
		}
		return instance;
	}
	/****************** Singleton stuff *******************/
	private List<String> list;
	private List<ISortClient> clients;
	private ISorter sorter;
	private int blockSize;
	private int lastClient = 0;

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public void add(String s) {
		this.list.add(s);
	}

	public void clear() {
		this.list.clear();
	}

	public List<String> getSortedList() {
		this.list = new ArrayList<String>();
		while (this.sorter.hasNext()) {
			this.list.add(this.sorter.next());
		}
		return this.list;
	}

	public void setSorter(ISorter sorter) {
		this.sorter = sorter;
	}

	public void sort() {
		if (this.sorter != null) {
			this.sorter.setList(this.list);
			this.sorter.sort();
			this.list = sorter.getSortedList();
		}
	}

	@Override
	public void addClient(ISortClient client) throws RemoteException {
		this.clients.add(client);
		System.out.println("new client added");
	}

	public void sortByClient(List<String> unsorted, int id) {
		if (id < 0 || id >= this.clients.size()) {
			throw new ArrayIndexOutOfBoundsException("no client with this number");
		}

		long start = GregorianCalendar.getInstance().getTimeInMillis();
		ISortClient client = this.clients.get(id);
		try {
			int i = 0;
			List<String> block = null;
			if (unsorted.size() <= this.blockSize) {
				client.add(unsorted);
				long end = GregorianCalendar.getInstance().getTimeInMillis();
				System.out.println("	adding: " + (end - start) + "ms");
				start = GregorianCalendar.getInstance().getTimeInMillis();

				client.sort();
				
				end = GregorianCalendar.getInstance().getTimeInMillis();
				System.out.println("	client-sort: " + (end - start) + "ms");
			} else {
				block = new ArrayList<String>();
				for (String s : unsorted) {
					block.add(s);
					if (i >= this.blockSize) {
						client.add(block);
						block.clear();
					}
				}
				client.add(block);
				client.sort();
			}
		} catch (RemoteException ex) {
			Logger.getLogger(SortServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public List<String> getSortedFromClient(int id) {
		if (id < 0 || id >= this.clients.size()) {
			throw new ArrayIndexOutOfBoundsException("no client with this number");
		}

		List<String> sort = new ArrayList<String>();
		boolean run = true;
		ISortClient client = this.clients.get(id);

		while (run) {
			try {
				List<String> sortedBlock = client.getSortedBlock(this.getBlockSize());
				if (sortedBlock != null) {
					sort.addAll(sortedBlock);
				} else {
					run = false;
				}
			} catch (RemoteException ex) {
				Logger.getLogger(SortServer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return sort;
	}

	public int getClientCount() {
		return this.clients.size();
	}

	public Iterator<String> iterator() {
		return this.sorter;
	}

	void setList(List<String> list) {
		this.list = list;
	}
}
