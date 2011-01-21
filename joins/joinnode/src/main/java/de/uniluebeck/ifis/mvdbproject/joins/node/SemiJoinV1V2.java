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
class SemiJoinV1V2 extends AJoin {

	TimeTracker tracker;

	public SemiJoinV1V2(TimeTracker tracker) {
		this.tracker = tracker;
	}

	Relation join(INode node, Relation s) throws RemoteException {
		if (node == null || s == null) {
			throw new RuntimeException("no nodes or relation");
		}

		String column = s.findSameColumn(node.getColumnNames());

		initJoined(node.getColumnNames(), s);
		int indexR = node.columnIndex(column);
		int indexS = s.columnIndex(column);

		Relation shrinked = projectTo(s, column);
		tracker.takeTime("semi join", Type.invoke, true);
		Relation rSemi = node.semiJoinWith(shrinked);
		tracker.takeTime("semi join", Type.received, true);

		// join it
		for (List<String> rowR : rSemi.getRows()) {
			for (List<String> rowS : s.getRows()) {
				String valueS = rowS.get(indexS);
				String valueR = rowR.get(indexR);
				if (valueR.equals(valueS)) {
					addToJoinedRelation(rowR, rowS);
				}
			}
		}
		joined.filterDoubles();
		return joined;
	}




}
