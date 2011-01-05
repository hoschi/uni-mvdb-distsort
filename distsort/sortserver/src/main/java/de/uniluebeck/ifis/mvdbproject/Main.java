/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
		System.out.println("The server is running!");
		
	}
}
