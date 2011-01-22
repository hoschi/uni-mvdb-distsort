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

/**
 *
 * @author hoschi
 */
class SemiJoinV3 extends AJoin {

	TimeTracker tracker;

	public SemiJoinV3(TimeTracker tracker) {
		this.tracker = tracker;
	}

	Relation join(INode nodeR, INode nodeS) throws RemoteException {

		String column = nodeR.findSameColumn(nodeS.getColumnNames());

		initJoined(nodeR.getColumnNames(), nodeS.getColumnNames());
		int indexR = nodeR.columnIndex(column);
		int indexS = nodeS.columnIndex(column);

		tracker.takeTime("start", Type.invoke, true);
		nodeR.startSemiJoinV3(nodeS, column);
		tracker.takeTime("start", Type.received, true);
		tracker.takeTime("start", Type.invoke, true);
		nodeS.startSemiJoinV3(nodeR, column);
		tracker.takeTime("start", Type.received, true);

		tracker.takeTime("semi join", Type.invoke, true);
		Relation rSemi = nodeR.getSemiJoinedRelationV3();
		tracker.takeTime("semi join", Type.received, true);

		tracker.takeTime("semi join", Type.invoke, true);
		Relation sSemi = nodeS.getSemiJoinedRelationV3();
		tracker.takeTime("semi join", Type.received, true);

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
		return joined;
	}
}
