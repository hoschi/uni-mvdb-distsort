/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

/**
 *
 * @author hoschi
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("starting server on //localhost:1198/sortserver");
		SortServer.getInstance();
		System.out.println("The server is running!");
	}
}
