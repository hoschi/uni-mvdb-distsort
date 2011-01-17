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
public interface INode  extends Remote{

	void add(Relation r) throws RemoteException;

	Relation getJoined() throws RemoteException;

	void join(Relation r, String columnR, String columnS) throws RemoteException;

}
