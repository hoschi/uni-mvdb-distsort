/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.INode;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author hoschi
 */
class FetchAsNeededJoin extends AJoin {

	Relation join(INode node, Relation s, String columnR, String columnS) throws RemoteException {
		if (node == null || s == null) {
			throw new RuntimeException("no relation");
		}

		if (columnR == null || columnS == null) {
			throw new RuntimeException("no column");
		}

		initJoined(node.getColumnNames(), s);
		int indexR = node.columnIndex(columnR);
		int indexS = s.columnIndex(columnS);

		if (indexR < 0 || indexS < 0) {
			throw new RuntimeException("column not found");
		}

		// set up new relation

		// join it
		for (List<String> rowS : s.getRows()) {
			while (node.hasNext()) {
				List<String> rowR = node.next();
				String valueS = rowS.get(indexS);
				String valueR = rowR.get(indexR);
				if (valueR.equals(valueS)) {
					addToJoinedRelation(rowR, rowS);
				}
			}
			node.resetIterator();
		}
		return joined;
	}
}
