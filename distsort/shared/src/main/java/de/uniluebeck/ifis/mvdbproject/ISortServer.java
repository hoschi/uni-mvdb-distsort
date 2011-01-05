/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.ifis.mvdbproject;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author hoschi
 */
public interface ISortServer extends Remote {

	void addClient(ISortClient client)  throws RemoteException;

}
