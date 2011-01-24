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
public class TimeTracker {
	protected IJoinServer server;
	int tabwith;
	

	public TimeTracker(IJoinServer server) throws RemoteException {
		this.server = server;
	}

	public void takeTime(String msg, TimeEntry.Type type) throws RemoteException {
		if (server == null)
			throw new RuntimeException("no server connected");

		server.addMeasurment(
					new TimeEntry(msg,
					GregorianCalendar.getInstance().getTime(),
					type, false));
		System.out.println(msg + ": " + type);
	}

	public void takeTime(String msg, TimeEntry.Type type, boolean stuff) throws RemoteException {
		if (server == null)
			throw new RuntimeException("no server connected");

		server.addMeasurment(
					new TimeEntry(msg,
					GregorianCalendar.getInstance().getTime(),
					type, stuff));
		System.out.println(msg + ": " + type);
	}

}
