/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeEntry;
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

	private class Fetcher implements Runnable {

		Node node;
		INode otherNode;
		String column;

		public Fetcher(Node node, INode otherNode, String column) {
			this.node = node;
			this.otherNode = otherNode;
			this.column = column;

		}

		@Override
		public void run() {
			try {
				node.relationSemiJoined = otherNode.projectTo(column);
			} catch (RemoteException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
			node.state = State.running;
		}
	}

	private class startSemiJoinV4thread extends Thread {

		INode nodeR, nodeK, me;
		String column;
		TimeTracker tracker;

		public startSemiJoinV4thread(TimeTracker tracker, INode nodeR, INode nodeK, INode me, String column) {
			this.nodeR = nodeR;
			this.nodeK = nodeK;
			this.me = me;
			this.column = column;
			this.tracker = tracker;
		}

		@Override
		public void run() {
			try {
				tracker.takeTime("sendSemiJoinedToNodeK", Type.invoke, true);
				nodeR.sendSemiJoinedToNodeK(relation.projectTo(column), nodeK, me, column);
				tracker.takeTime("sendSemiJoinedToNodeK", Type.received, true);
			} catch (RemoteException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}


	Relation relation, joined, relationSemiJoined;
	String rmiName;
	boolean bound;
	int counter;
	TimeTracker tracker;
	int port;
	SemiJoinV4 semiJoinV4;

	enum State {

		waiting, running
	};
	State state;

	public Node(TimeTracker tracker) throws RemoteException {
		state = State.running;
		this.rmiName = UUID.randomUUID().toString();
		bound = false;
		counter = 0;
		this.tracker = tracker;
		this.port = Registry.REGISTRY_PORT;
		semiJoinV4 = null;
	}

	@Override
	public void clear() throws RemoteException {
		relation = null;
		counter = 0;
	}

	@Override
	public void add(Relation r) throws RemoteException {
		tracker.takeTime("add", Type.get);
		relation = r;
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
		if (relation == null) {
			throw new RuntimeException("can't join this");
		}
		NestedLoopJoin join = new NestedLoopJoin();
		this.joined = join.join(r, relation);
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public void joinFetchAsNeeded(String rmiName, int port) throws RemoteException {
		tracker.takeTime("join", Type.get);
		if (relation == null) {
			throw new RuntimeException("can't join this");
		}
		INode node = getNode(rmiName);
		if (node == null) {
			throw new RuntimeException("no node with name " + rmiName);
		}
		FetchAsNeededJoin join = new FetchAsNeededJoin(tracker);
		this.joined = join.join(node, relation);
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public void joinSemiV1V2(String rmiName, int port) throws RemoteException {
		tracker.takeTime("join", Type.get);
		if (relation == null) {
			throw new RuntimeException("can't join this");
		}
		INode node = getNode(rmiName);
		if (node == null) {
			throw new RuntimeException("no node with name " + rmiName);
		}
		SemiJoinV1V2 join = new SemiJoinV1V2(tracker);
		this.joined = join.join(node, relation);
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public void joinSemiV3(String rmiNameR, int portR, String rmiNameS, int portS) throws RemoteException {
		tracker.takeTime("join", Type.get);

		this.joined = null;

		INode nodeR = getNode(rmiNameR);
		INode nodeS = getNode(rmiNameS);
		if (nodeR == null) {
			throw new RuntimeException("no node with name " + rmiNameR);
		}
		if (nodeS == null) {
			throw new RuntimeException("no node with name " + rmiNameS);
		}

		SemiJoinV3 join = new SemiJoinV3(tracker);
		this.joined = join.join(nodeR, nodeS);
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public void startSemiJoinV3(INode node, String column) throws RemoteException {
		tracker.takeTime("start", Type.get, true);
		state = State.waiting;
		Fetcher fetcher = new Fetcher(this, node, column);
		Thread t = new Thread(fetcher);
		t.start();
		tracker.takeTime("start", Type.replay, true);
	}

	@Override
	public Relation getSemiJoinedRelationV3() throws RemoteException {
		tracker.takeTime("semi join", Type.get, true);
		System.out.print("wait: ");// wait
		while (state != State.running) {
			System.out.print(".");// wait
			try {
				Thread.currentThread().sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println();
		Relation semiJoinWith = semiJoinWith(relationSemiJoined);
		tracker.takeTime("semi join", Type.replay, true);
		return semiJoinWith;
	}

	@Override
	public void joinSemiV4(String rmiNameR, int portR, String rmiNameS, int portS) throws RemoteException {
		tracker.takeTime("join", Type.get);

		INode nodeR = getNode(rmiNameR);
		INode nodeS = getNode(rmiNameS);
		bindNode();
		INode nodeK = getNode(this.rmiName);

		if (nodeR == null) {
			throw new RuntimeException("no node with name " + rmiNameR);
		}
		if (nodeS == null) {
			throw new RuntimeException("no node with name " + rmiNameS);
		}
		if (nodeK == null) {
			throw new RuntimeException("no node with name " + rmiName);
		}

		this.semiJoinV4 = new SemiJoinV4(tracker, nodeR, nodeS, nodeK);
		Thread t = new Thread(this.semiJoinV4);
		t.run();
		
		this.joined = semiJoinV4.getJoined();
		tracker.takeTime("join", Type.replay);
	}

	

	@Override
	public void startSemiJoinV4(INode nodeR, INode nodeK, String column) throws RemoteException {
		tracker.takeTime("join", Type.get);
		INode me = getNode(rmiName);
		startSemiJoinV4thread t = new startSemiJoinV4thread(tracker, nodeR, nodeK, me, column);
		t.start();
		tracker.takeTime("join", Type.replay);
	}

	@Override
	public void sendSemiJoinedToNodeK(Relation joinRelation, INode nodeK) throws RemoteException {
		// this is for node S
		tracker.takeTime("sendSemiJoinedToNodeK", Type.get, true);

		Relation semiJoinWith = this.semiJoinWith(joinRelation);

		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.invoke, true);
		nodeK.takeSemiJoinFromNodeS(semiJoinWith);
		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.received, true);

		tracker.takeTime("sendSemiJoinedToNodeK", Type.replay, true);
	}

	@Override
	public void sendSemiJoinedToNodeK(Relation joinRelation, INode nodeK, INode nodeS, String column) throws RemoteException {
		// this is for node R
		tracker.takeTime("sendSemiJoinedToNodeK", Type.get, true);

		Relation semiJoinWith = this.semiJoinWith(joinRelation);

		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.invoke, true);
		nodeK.takeSemiJoinFromNodeR(semiJoinWith);
		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.received, true);

		tracker.takeTime("sendSemiJoinedToNodeK", Type.invoke, true);
		nodeS.sendSemiJoinedToNodeK(relation.projectTo(column), nodeK);
		tracker.takeTime("sendSemiJoinedToNodeK", Type.received, true);

		tracker.takeTime("sendSemiJoinedToNodeK", Type.replay, true);
	}

	@Override
	public void takeSemiJoinFromNodeR(Relation joined) throws RemoteException {
		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.get);
		semiJoinV4.setrSemi(joined);
		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.replay);
	}

	@Override
	public void takeSemiJoinFromNodeS(Relation joined) throws RemoteException {
		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.get);
		semiJoinV4.setsSemi(joined);
		tracker.takeTime("takeSemiJoinFromNode", TimeEntry.Type.replay);
	}

	@Override
	public Relation semiJoinWith(Relation joinRelation) throws RemoteException {
		NestedLoopJoin join = new NestedLoopJoin();
		this.joined = join.join(
				joinRelation,
				relation);
		this.joined.setName("semi joined");
		this.joined.filterDoubleRows();
		return joined;
	}

	@Override
	public String findSameColumn(List<String> columnNames) throws RemoteException {
		if (relation == null) {
			throw new RuntimeException("no relation attached");
		}
		return this.relation.findSameColumn(columnNames);
	}

	@Override
	public Relation projectTo(String column) throws RemoteException {
		return this.relation.projectTo(column);
	}

	@Override
	public int columnIndex(String columnR) {
		if (relation == null) {
			throw new RuntimeException("no relation attached");
		}
		return this.relation.columnIndex(columnR);
	}

	@Override
	public List<String> getColumnNames() {
		if (relation == null) {
			throw new RuntimeException("no relation attached");
		}
		return this.relation.getColumnNames();
	}

	@Override
	public boolean hasNext() throws RemoteException {
		if (relation == null) {
			throw new RuntimeException("no relation attached");
		} else if (this.counter >= relation.getRowCount()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<String> next() throws RemoteException {
		tracker.takeTime("get row", Type.get, true);
		if (relation == null) {
			throw new RuntimeException("no relation attached");
		} else {
			List<String> row = this.relation.getRow(this.counter);
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
