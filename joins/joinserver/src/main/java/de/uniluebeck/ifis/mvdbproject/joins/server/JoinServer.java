/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.ifis.mvdbproject.joins.server;

import de.uniluebeck.ifis.mvdbproject.joins.shared.IJoinServer;
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
public class JoinServer extends UnicastRemoteObject implements IJoinServer {
	List<INode> nodes;

	public JoinServer() throws RemoteException {
		this.nodes = new ArrayList<INode>();
	}
	
	

	public void addNode(INode node)  throws RemoteException {
		this.nodes.add(node);
	}

	public Relation joinShipWhole(Relation r, Relation s, String columnR, String columnS) throws RemoteException {
		if (nodes.isEmpty()) {
			throw new RuntimeException("not enought nodes to do that");
		}
		INode node = nodes.get(0);
		node.add(s);
		node.join(r, columnR, columnS);
		Relation joined = node.getJoined();

		return joined;
	}
}
