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

	public void clear() throws RemoteException;

	public Relation getJoined() throws RemoteException;

	public void joinShipWhole(Relation r) throws RemoteException;

	public void joinFetchAsNeeded(String nodeName, int port) throws RemoteException;

	public int columnIndex(String columnR) throws RemoteException;

	public List<String> getColumnNames() throws RemoteException;

	public String getRmiName() throws RemoteException;

	public void bindNode() throws RemoteException;

	public boolean hasNext() throws RemoteException;

	public List<String> next() throws RemoteException;

	public void resetIterator() throws RemoteException;

	public int getPort() throws RemoteException;

	public void joinSemiV1V2(String rmiName, int port) throws RemoteException;

	public Relation semiJoinWith(Relation joinRelation) throws RemoteException;

	public void joinSemiV3(String rmiNameR, int portR, String rmiNameS, int portS) throws RemoteException;

	public String findSameColumn(List<String> columnNames) throws RemoteException;

	public void startSemiJoinV3(INode nodeS, String column) throws RemoteException;

	public Relation getSemiJoinedRelationV3() throws RemoteException;

	public Relation projectTo(String column) throws RemoteException;

	public void joinSemiV4(String rmiNameR, int portR, String rmiNameS, int portS) throws RemoteException;

	public void startSemiJoinV4(INode nodeR, INode nodeK, String column) throws RemoteException;

	public void sendSemiJoinedToNodeK(Relation joinRelation, INode nodeK) throws RemoteException;

	public void sendSemiJoinedToNodeK(Relation joinRelation, INode nodeK, INode nodeS, String column) throws RemoteException;

	public void takeSemiJoinFromNodeS(Relation joined) throws RemoteException;

	public void takeSemiJoinFromNodeR(Relation joined) throws RemoteException;
}
