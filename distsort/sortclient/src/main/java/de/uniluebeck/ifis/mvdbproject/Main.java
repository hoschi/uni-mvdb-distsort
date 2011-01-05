/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author hoschi
 */
public class Main {

	public static void main(String[] args) throws RemoteException {
		System.out.println("adding me to server on //localhost/sortserver");
		ISortServer stub = null;
		try {
			stub = (ISortServer) Naming.lookup("//localhost/sortserver");
			stub.addClient(new SortClient());
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
		
		System.out.println("SUCCESS!");
	}
}
