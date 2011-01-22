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
class FetchAsNeededJoin extends AJoin {
	TimeTracker tracker;

	public FetchAsNeededJoin(TimeTracker tracker) {
		this.tracker = tracker;
	}


	Relation join(INode node, Relation s) throws RemoteException {
		if (node == null || s == null) {
			throw new RuntimeException("no relation");
		}

		String column = s.findSameColumn(node.getColumnNames());

		initJoined(node.getColumnNames(), s);
		int indexR = node.columnIndex(column);
		int indexS = s.columnIndex(column);

		if (indexR < 0 || indexS < 0) {
			throw new RuntimeException("column not found");
		}

		// set up new relation

		// join it
		for (List<String> rowS : s.getRows()) {
			while (node.hasNext()) {
				tracker.takeTime("get row", Type.invoke, true);
				List<String> rowR = node.next();
				tracker.takeTime("get row", Type.received, true);
				String valueS = rowS.get(indexS);
				String valueR = rowR.get(indexR);
				if (valueR.equals(valueS)) {
					addToJoinedRelation(rowR, rowS);
				}
			}
			node.resetIterator();
		}
		joined.filterDoubleColumns();
		return joined;
	}
}
