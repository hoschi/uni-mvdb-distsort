package de.uniluebeck.ifis.mvdbproject;

import java.util.*;

public class SortServer {

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
    private SortServer() {
		this.list = new ArrayList();
	}

	/**
	 * Statische Methode "getInstance()" liefert die einzige Instanz der Klasse
	 * zurück. Ist synchronisiert und somit thread-sicher.
	 */
	public synchronized static SortServer getInstance() {
		if (instance == null) {
			instance = new SortServer();
		}
		return instance;
	}

	/****************** Singleton stuff *******************/

	private List<String> list;
	private ISorter sorter;
	private int blockSize;

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

	void addClient(SortClient client) {
		
	}

	List<String> sortByClient(List<String> unsorted) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
