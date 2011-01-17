/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoschi
 */
public class Node extends UnicastRemoteObject implements INode {

	List<Relation> relations;
	Relation joined;

	public Node() throws RemoteException {
		super();
		relations = new ArrayList<Relation>();
	}

	public void add(Relation r) throws RemoteException {
		relations.add(r);
	}

	/*
	 * join method for "ship whole" mode
	 */
	public void join(Relation r, String columnR, String columnS)
			throws RemoteException {
		if (relations.isEmpty()
				|| relations.get(0).hasColumn(columnS) == false) {
			throw new RuntimeException("can't join this");
		}
		NestedLoopJoin join = new NestedLoopJoin();
		this.joined = join.join(r, relations.get(0), columnR, columnS);
	}

	public Relation getJoined() throws RemoteException {
		return this.joined;
	}
}
