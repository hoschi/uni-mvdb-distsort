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

	public void add(String s) {
		this.unsorted.add(s);
	}

	public List<String> getList() {
		return this.unsorted;
	}
}
