/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.server;

import de.uniluebeck.ifis.mvdbproject.joins.shared.IJoinServer;
import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeEntry;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeTracker;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.List;

/**
 *
 * @author hoschi
 */
public class JoinServer extends UnicastRemoteObject implements IJoinServer {

	List<INode> nodes;
	protected List<TimeEntry> measurements;
	TimeTracker tracker;

	public JoinServer() throws RemoteException {
		this.nodes = new ArrayList<INode>();

	}

	public void addNode(INode node) throws RemoteException {
		this.nodes.add(node);

	}

	public Relation joinShipWhole(Relation r, Relation s, String columnR, String columnS) throws RemoteException {
		if (nodes.isEmpty()) {
			throw new RuntimeException("not enought nodes to do that");
		}

		INode node = nodes.get(0);

		tracker.takeTime("invoke: add");
		node.add(s);
		tracker.takeTime("msg client -> server: add");

		tracker.takeTime("invoke: join");
		node.joinShipWhole(r, columnR, columnS);
		tracker.takeTime("msg client -> server: join");

		tracker.takeTime("invoke: getjoined");
		Relation joined = node.getJoined();
		tracker.takeTime("msg client -> server: getjoined");

		return joined;
	}

	public Relation joinFetchAsNeeded(Relation r, Relation s, String columnR, String columnS) throws RemoteException {
		if (nodes == null || nodes.size() < 2) {
			throw new RuntimeException("not enought nodes to do that");
		}

		INode nodeS = nodes.get(0);
		INode nodeR = nodes.get(1);

		// set up nodes
		nodeR.add(r);
		nodeS.add(s);

		tracker.takeTime("invoke: join");
		nodeS.joinFetchAsNeeded(nodeR.getRmiName(), nodeR.getPort(), columnR, columnS);
		tracker.takeTime("msg client -> server: join");

		tracker.takeTime("invoke: getjoined");
		Relation joined = nodeS.getJoined();
		tracker.takeTime("msg client -> server: getjoined");

		return joined;
	}

	@Override
	public void addMeasurment(TimeEntry entry) throws RemoteException {
		measurements.add(entry);
	}

	public void startMeasurements() throws RemoteException {
		tracker = new TimeTracker(this);
		measurements = new ArrayList<TimeEntry>();
	}

	public void printLastMeasurements() throws RemoteException {
		for (int i = 0; i < measurements.size(); ++i) {
			TimeEntry entry = measurements.get(i);
			System.out.print(entry.getMessage() + " - duration ");

			if (i > 0) {
				TimeEntry entryBefore = measurements.get(i - 1);
				Date dateBefore = entryBefore.getDate();
				long duration = entry.getDate().getTime() - measurements.get(i - 1).getDate().getTime();
				System.out.print(duration);
			}
			System.out.println(" ms");
		}
	}
}
