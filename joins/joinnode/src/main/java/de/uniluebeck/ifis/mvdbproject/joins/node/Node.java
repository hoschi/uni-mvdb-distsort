/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeEntry.Type;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeTracker;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoschi
 */
public class Node extends UnicastRemoteObject implements INode {

	List<Relation> relations;
	Relation joined;
	String rmiName;
	boolean bound;
	int counter;
	TimeTracker tracker;
	int port;

	public Node(TimeTracker tracker) throws RemoteException {
		relations = new ArrayList<Relation>();
		this.rmiName = UUID.randomUUID().toString();
		bound = false;
		counter = 0;
		this.tracker = tracker;
		this.port = Registry.REGISTRY_PORT;
	}

	@Override
	public void clear() throws RemoteException {
		relations = new ArrayList<Relation>();
		counter = 0;
	}

	@Override
	public void add(Relation r) throws RemoteException {
		tracker.takeTime("add", Type.get);
		relations.add(r);
		tracker.takeTime("add", Type.replay);
	}

	@Override
	public Relation getJoined() throws RemoteException {
		tracker.takeTime("getjoined", Type.get);
		tracker.takeTime("getjoined", Type.replay);
		return this.joined;
	}

	/*
	 * join method for "ship whole" mode
	 */
	@Override
	public void joinShipWhole(Relation r)
			throws RemoteException {
		tracker.takeTime("join", Type.get);
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("can't join this");
		}
		NestedLoopJoin join = new NestedLoopJoin();
		this.joined = join.join(r, relations.get(0));
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public void joinSemiV1V2(String rmiName, int port) throws RemoteException {
		tracker.takeTime("join", Type.get);
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("can't join this");
		}
		INode node = getNode(rmiName);
		if (node == null) {
			throw new RuntimeException("no node with name " + rmiName);
		}
		SemiJoinV1V2 join = new SemiJoinV1V2(tracker);
		this.joined = join.join(node, relations.get(0));
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public void joinFetchAsNeeded(String rmiName, int port) throws RemoteException {
		tracker.takeTime("join", Type.get);
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("can't join this");
		}
		INode node = getNode(rmiName);
		if (node == null) {
			throw new RuntimeException("no node with name " + rmiName);
		}
		FetchAsNeededJoin join = new FetchAsNeededJoin(tracker);
		this.joined = join.join(node, relations.get(0));
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public Relation semiJoinWith(Relation joinRelation) throws RemoteException {
		NestedLoopJoin join = new NestedLoopJoin();
		this.joined = join.join(
				joinRelation,
				relations.get(0));
		this.joined.setName("semi joined");
		return joined;
	}

	@Override
	public int columnIndex(String columnR) {
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		}
		return this.relations.get(0).columnIndex(columnR);
	}

	@Override
	public List<String> getColumnNames() {
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		}
		return this.relations.get(0).getColumnNames();
	}

	@Override
	public boolean hasNext() throws RemoteException {
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		} else if (this.counter >= relations.get(0).getRowCount()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<String> next() throws RemoteException {
		tracker.takeTime("get row", Type.get, true);
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		} else {
			List<String> row = this.relations.get(0).getRow(this.counter);
			counter++;
			//System.out.println("row -> " + row.toString());
			tracker.takeTime("get row", Type.replay, true);
			return row;
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private INode getNode(String name) {
		INode node = null;
		try {
			node = (INode) Naming.lookup(name);
		} catch (NotBoundException ex) {
			Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
		} catch (RemoteException ex) {
			Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
		}
		return node;
	}

	@Override
	public String getRmiName() throws RemoteException {
		String name = null;
		if (bound == false) {
			bindNode();
		}

		try {
			InetAddress localHost = java.net.InetAddress.getLocalHost();
			name = "rmi://" + localHost.getHostAddress() + "/" + this.rmiName;
		} catch (UnknownHostException ex) {
			Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
		}
		return name;
	}

	@Override
	public void bindNode() throws RemoteException {
		while (available(port) == false) {
			Random random = new Random();
			this.port = Registry.REGISTRY_PORT + random.nextInt(1000);
		}
		final Registry reg = LocateRegistry.createRegistry(port);
		try {
			Naming.rebind(this.rmiName, this);
			bound = true;
			System.out.println("binding me to " + getRmiName() + " on port " + port);
		} catch (MalformedURLException ex) {
			Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void resetIterator() throws RemoteException {
		this.counter = 0;
	}

	@Override
	public int getPort() throws RemoteException {
		return port;
	}

	/**
	 * Checks to see if a specific port is available.
	 * http://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java
	 *
	 * @param port the port to check for availability
	 */
	private static boolean available(int port) {

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}
}
