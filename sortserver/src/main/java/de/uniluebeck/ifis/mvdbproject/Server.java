package de.uniluebeck.ifis.mvdbproject;

import java.util.*;

public class Server {

	/****************** Singleton stuff *******************/
	/**
	 * Privates Klassenattribut, wird beim erstmaligen Gebrauch (nicht beim
	 * Laden) der Klasse erzeugt
     */
    private static Server instance;

	/** 
	 * Konstruktor ist privat, Klasse darf nicht von außen instanziiert
	 * werden. 
	 */
    private Server() {
		this.list = new ArrayList();
	}

	/**
	 * Statische Methode "getInstance()" liefert die einzige Instanz der Klasse
	 * zurück. Ist synchronisiert und somit thread-sicher.
	 */
	public synchronized static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	/****************** Singleton stuff *******************/

	protected List<String> list;
	protected ISorter sorter;

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

	void addClient(SortClient client) {
		
	}

	int getBlockSize() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	List<String> sortByClient(List<String> unsorted) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
