/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author hoschi
 */
public interface INode extends Remote {

	public void add(Relation r) throws RemoteException;

	public Relation getJoined() throws RemoteException;

	public void joinShipWhole(Relation r, String columnR, String columnS) throws RemoteException;

	public void joinFetchAsNeeded(String nodeName, int port, String columnR, String columnS) throws RemoteException;

	public int columnIndex(String columnR) throws RemoteException;

	public List<String> getColumnNames() throws RemoteException;

	public String getRmiName() throws RemoteException;

	public void bindNode() throws RemoteException;

	public boolean hasNext() throws RemoteException;

	public List<String> next() throws RemoteException;

	public void resetIterator() throws RemoteException;

	public int getPort()  throws RemoteException;
}
