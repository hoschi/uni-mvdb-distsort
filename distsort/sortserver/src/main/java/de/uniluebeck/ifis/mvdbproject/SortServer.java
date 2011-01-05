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

	public List<String> getList() {
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

	public List<String> sortByClient(List<String> unsorted) {
		int next = ++this.lastClient;
		if (next >= this.clients.size()) {
			next = 0;
		}
		ISortClient client = this.clients.get(next);
		try {
			return client.sort(unsorted);
		} catch (RemoteException ex) {
			Logger.getLogger(SortServer.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
