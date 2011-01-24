/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeEntry.Type;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeTracker;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoschi
 */
class SemiJoinV4 extends AJoin implements Runnable {

	TimeTracker tracker;
	boolean nodeShasFinished, nodeRhasFinished;
	Relation rSemi, sSemi;
	INode nodeR, nodeS, nodeK;

	public SemiJoinV4(TimeTracker tracker, INode nodeR, INode nodeS, INode nodeK) {
		this.tracker = tracker;
		this.nodeR = nodeR;
		this.nodeS = nodeS;
		this.nodeK = nodeK;
	}

	synchronized private void join() throws RemoteException, InterruptedException {
		nodeShasFinished = false;
		nodeRhasFinished = false;
		rSemi = null;
		sSemi = null;

		String column = nodeR.findSameColumn(nodeS.getColumnNames());

		initJoined(nodeR.getColumnNames(), nodeS.getColumnNames());
		int indexR = nodeR.columnIndex(column);
		int indexS = nodeS.columnIndex(column);

		tracker.takeTime("start", Type.invoke, true);
		nodeS.startSemiJoinV4(nodeR, nodeK, column);
		tracker.takeTime("start", Type.received, true);

		System.out.println("wait for nodes");// wait
		while (nodeRhasFinished == false || nodeShasFinished == false) {
			wait();
		}

		if (rSemi == null) {
			throw new RuntimeException("no data from node r");
		}

		if (sSemi == null) {
			throw new RuntimeException("no data from node s");
		}

		for (List<String> rowR : rSemi.getRows()) {
			for (List<String> rowS : sSemi.getRows()) {
				String valueS = rowS.get(indexS);
				String valueR = rowR.get(indexR);
				if (valueR.equals(valueS)) {
					addToJoinedRelation(rowR, rowS);
				}
			}
		}

		joined.filterDoubleColumns();
	}

	synchronized public void setrSemi(Relation rSemi) {
		this.rSemi = rSemi;
		this.nodeRhasFinished = true;
		notify();
	}

	synchronized public void setsSemi(Relation sSemi) {
		this.sSemi = sSemi;
		this.nodeShasFinished = true;
		notify();
	}

	public Relation getJoined() {
		return joined;
	}



	@Override
	synchronized public void run() {
		try {
			this.join();
		} catch (RemoteException ex) {
			Logger.getLogger(SemiJoinV4.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(SemiJoinV4.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
