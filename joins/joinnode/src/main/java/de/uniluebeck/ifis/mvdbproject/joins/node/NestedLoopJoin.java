/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import java.util.List;

/**
 *
 * @author hoschi
 */
public class NestedLoopJoin extends AJoin {

	public Relation join(Relation r, Relation s) {
		if (r == null || s == null) {
			throw new RuntimeException("no relation");
		}

		String column = r.findSameColumn(s);

		initJoined(r,s);
		int indexR = r.columnIndex(column);
		int indexS = s.columnIndex(column);

		if (indexR < 0 || indexS < 0) {
			throw new RuntimeException("column not found");
		}

		// set up new relation

		// join it
		for (List<String> rowR : r.getRows()) {
			for (List<String> rowS : s.getRows()) {
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
