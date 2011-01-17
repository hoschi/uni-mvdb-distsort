/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoschi
 */
abstract class AJoin {

	protected Relation joined;

	abstract public Relation join(
			Relation r,
			Relation s,
			String columnR,
			String columnS);

	protected void initJoined(Relation r, Relation s) {
		this.joined = new Relation("joined");
		for (String name : r.getColumnNames()) {
			joined.addColumn(name);
		}
		for (String name : s.getColumnNames()) {
			joined.addColumn(name);
		}
	}

	protected void addToJoinedRelation(
			List<String> rowR,
			List<String> rowS) {
		List<String> newRow = new ArrayList<String>();
		for (String s : rowR) {
			newRow.add(s);
		}
		for (String s : rowS) {
			newRow.add(s);
		}
		this.joined.addRow(newRow);
	}
}
