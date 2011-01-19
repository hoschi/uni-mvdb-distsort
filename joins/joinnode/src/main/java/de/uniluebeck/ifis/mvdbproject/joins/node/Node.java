/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
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
	public void add(Relation r) throws RemoteException {
		tracker.takeTime("\tmsg server -> client: add");
		relations.add(r);
		tracker.takeTime("\tadding finished");
	}

	/*
	 * join method for "ship whole" mode
	 */
	public void joinShipWhole(Relation r, String columnR, String columnS)
			throws RemoteException {
		tracker.takeTime("\tmsg server -> client: join");
		if (relations == null || relations.isEmpty()
				|| relations.get(0).hasColumn(columnS) == false) {
			throw new RuntimeException("can't join this");
		}
		NestedLoopJoin join = new NestedLoopJoin();
		this.joined = join.join(r, relations.get(0), columnR, columnS);
		tracker.takeTime("\tjoining finished");
	}

	@Override
	public Relation getJoined() throws RemoteException {
		tracker.takeTime("\tmsg server -> client: getjoined");
		tracker.takeTime("\treturning finished");
		return this.joined;
	}

	public void joinFetchAsNeeded(String nodeName, int port, String columnR, String columnS) throws RemoteException {
		tracker.takeTime("\tmsg server -> client: join");
		if (relations == null || relations.isEmpty()
				|| relations.get(0).hasColumn(columnS) == false) {
			throw new RuntimeException("can't join this");
		}
		INode node = getNode(nodeName);
		if (node == null) {
			throw new RuntimeException("no node with name " + nodeName);
		}
		FetchAsNeededJoin join = new FetchAsNeededJoin();
		this.joined = join.join(node, relations.get(0), columnR, columnS);
		tracker.takeTime("\tjoining finished");
	}

	public int columnIndex(String columnR) {
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		}
		return this.relations.get(0).columnIndex(columnR);
	}

	public List<String> getColumnNames() {
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		}
		return this.relations.get(0).getColumnNames();
	}

	public boolean hasNext() throws RemoteException {
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		} else if (this.counter >= relations.get(0).getRowCount()) {
			return false;
		} else {
			return true;
		}
	}

	public List<String> next() throws RemoteException {
		if (relations == null || relations.isEmpty()) {
			throw new RuntimeException("no relation attached");
		} else {
			List<String> row = this.relations.get(0).getRow(this.counter);
			counter++;
			//System.out.println("\t\treturning row -> " + row.toString());
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
