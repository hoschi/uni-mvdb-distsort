/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author hoschi
 */
public interface IJoinServer extends Remote {

	void addNode(INode node)  throws RemoteException;
	public void addMeasurment(TimeEntry entry) throws RemoteException;
}
