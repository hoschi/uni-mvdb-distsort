/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoschi
 */
public class TimeTracker extends UnicastRemoteObject {
	protected IJoinServer server;

	public TimeTracker(IJoinServer server) throws RemoteException {
		this.server = server;
	}

	protected void takeTime(String msg) throws RemoteException {
		if (server == null)
			throw new RuntimeException("no server connected");

		server.addMeasurment(
					new TimeEntry(msg,
					GregorianCalendar.getInstance().getTime()));
		System.out.println(msg);
	}
}
