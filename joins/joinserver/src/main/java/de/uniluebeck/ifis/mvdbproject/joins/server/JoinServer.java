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

	@Override
	public void addNode(INode node) throws RemoteException {
		this.nodes.add(node);

	}

	public Relation joinShipWhole(Relation r, Relation s, String columnR, String columnS) throws RemoteException {
		if (nodes.isEmpty()) {
			throw new RuntimeException("not enought nodes to do that");
		}

		INode node = nodes.get(0);

		tracker.takeTime("add", TimeEntry.Type.invoke);
		node.add(s);
		tracker.takeTime("add", TimeEntry.Type.received);

		tracker.takeTime("join", TimeEntry.Type.invoke);
		node.joinShipWhole(r, columnR, columnS);
		tracker.takeTime("join", TimeEntry.Type.received);

		tracker.takeTime("getjoined", TimeEntry.Type.invoke);
		Relation joined = node.getJoined();
		tracker.takeTime("getjoined", TimeEntry.Type.received);

		return joined;
	}

	public Relation joinFetchAsNeeded(Relation r, Relation s, String columnR, String columnS) throws RemoteException {
		if (nodes == null || nodes.size() < 2) {
			throw new RuntimeException("not enought nodes to do that");
		}

		
		INode nodeS = nodes.get(0);
		INode nodeR = nodes.get(1);

		// set up nodes
		tracker.takeTime("add", TimeEntry.Type.invoke);
		nodeR.add(r);
		tracker.takeTime("add", TimeEntry.Type.received);

		tracker.takeTime("add", TimeEntry.Type.invoke);
		nodeS.add(s);
		tracker.takeTime("add", TimeEntry.Type.received);

		tracker.takeTime("join", TimeEntry.Type.invoke);
		nodeS.joinFetchAsNeeded(nodeR.getRmiName(), nodeR.getPort(), columnR, columnS);
		tracker.takeTime("join", TimeEntry.Type.received);

		tracker.takeTime("getjoined", TimeEntry.Type.invoke);
		Relation joined = nodeS.getJoined();
		tracker.takeTime("getjoined", TimeEntry.Type.received);

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

	public long countNetworkTraffic() {
		List<TimeEntry> mess = new ArrayList<TimeEntry>();
		long traffic = 0;
		long start, end;

		// remove node traffic, because this is too much
		for (int i = 0; i < measurements.size(); ++i) {
			TimeEntry current = measurements.get(i);
			if (current.isStuff() == false) {
				mess.add(current);
				continue;
			}

			if (current.getType().equals(TimeEntry.Type.invoke)
					&& measurements.get(i + 1).getType().equals(TimeEntry.Type.get)) {
				// merge into new typed entries
				start = current.getDate().getTime();
				end = measurements.get(i + 1).getDate().getTime();
				traffic += end - start;
			}

			if (current.getType().equals(TimeEntry.Type.replay)
					&& measurements.get(i + 1).getType().equals(TimeEntry.Type.received)) {
				// merge into new typed entries
				start = current.getDate().getTime();
				end = measurements.get(i + 1).getDate().getTime();
				traffic += end - start;
			}
		}

		this.measurements = mess;
		mess = new ArrayList<TimeEntry>();

		// summarize logic stuff and count network traffic between server and main node
		for (int i = 0; i < measurements.size() - 1; ++i) {
			TimeEntry current = measurements.get(i);

			if (current.getType().equals(TimeEntry.Type.invoke)
					&& measurements.get(i + 1).getType().equals(TimeEntry.Type.get)) {
				// merge into new typed entries
				start = current.getDate().getTime();
				end = measurements.get(i + 1).getDate().getTime();
				mess.add(new TimeEntry("msg -> : " + current.getMessage(),
						new Date(end - start),
						TimeEntry.Type.traffic, false));
				traffic += end - start;
			}

			if (current.getType().equals(TimeEntry.Type.get)
					&& measurements.get(i + 1).getType().equals(TimeEntry.Type.replay)) {
				// merge into new typed entries
				start = current.getDate().getTime();
				end = measurements.get(i + 1).getDate().getTime();
				mess.add(new TimeEntry(current.getMessage(),
						new Date(end - start),
						TimeEntry.Type.logic, false));
			}

			if (current.getType().equals(TimeEntry.Type.replay)
					&& measurements.get(i + 1).getType().equals(TimeEntry.Type.received)) {

				// merge into new typed entries
				start = current.getDate().getTime();
				end = measurements.get(i + 1).getDate().getTime();
				mess.add(new TimeEntry("msg <- : " + current.getMessage(),
						new Date(end - start),
						TimeEntry.Type.traffic, false));
				traffic += end - start;
			}
		}
		this.measurements = mess;
		return traffic;
	}

	public void printLastMeasurements() throws RemoteException {
		long traffic = this.countNetworkTraffic();
		for (int i = 0; i < measurements.size(); ++i) {
			TimeEntry entry = measurements.get(i);
			System.out.println(entry.getMessage() + " - duration " + entry.getDate().getTime() + " ms");
		}
		System.out.println("\nAll commulated traffic between server and nodes are " + traffic + " ms");
	}

	public void printLastMeasurementsWithDetails() throws RemoteException {
		for (int i = 0; i < measurements.size(); ++i) {
			TimeEntry entry = measurements.get(i);
			System.out.print(entry.getMessage() + ": "+entry.getType()+" - duration ");

			if (i > 0) {
				TimeEntry entryBefore = measurements.get(i - 1);
				Date dateBefore = entryBefore.getDate();
				long duration = entry.getDate().getTime() - dateBefore.getTime();
				System.out.print(duration);
			}
			System.out.println(" ms");

		}
		System.out.println("------------------------------------------");
		this.printLastMeasurements();
	}
}
