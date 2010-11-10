package de.uniluebeck.ifis.mvdbproject;

import java.util.*;

public final class Server {

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
		this.unsorted = new ArrayList();
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

	protected List<String> unsorted;
	protected ISorter sorter;

	public void add(String s) {
		this.unsorted.add(s);
	}
	
	public void clear() {
		this.unsorted.clear();
	}

	public List<String> getList() {
		return this.unsorted;
	}

	public void setSorter(ISorter sorter) {
		this.sorter = sorter;
	}

	public void sort() {
	}

}
