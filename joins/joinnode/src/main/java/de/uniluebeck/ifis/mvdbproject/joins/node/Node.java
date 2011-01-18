/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.IJoinServer;
import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeTracker;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoschi
 */
public class Node extends TimeTracker implements INode {

	List<Relation> relations;
	Relation joined;
	

	public Node(IJoinServer server) throws RemoteException {
		super(server);
		relations = new ArrayList<Relation>();
		
	}

	@Override
	public void add(Relation r) throws RemoteException {
		takeTime("\tmsg server -> client: add");
		//relations.add(r.copy());
		relations.add(r);
		takeTime("\tadding finished");
	}

	/*
	 * join method for "ship whole" mode
	 */
	@Override
	public void join(Relation r, String columnR, String columnS)
			throws RemoteException {
		takeTime("\tmsg server -> client: join");
		if (relations == null || relations.isEmpty()
				|| relations.get(0).hasColumn(columnS) == false) {
			throw new RuntimeException("can't join this");
		}
		NestedLoopJoin join = new NestedLoopJoin();
		this.joined = join.join(r, relations.get(0), columnR, columnS);
		takeTime("\tjoining finished");
	}

	@Override
	public Relation getJoined() throws RemoteException {
		takeTime("\tmsg server -> client: getjoined");
		takeTime("\treturning finished");
		return this.joined;
	}

	
}
