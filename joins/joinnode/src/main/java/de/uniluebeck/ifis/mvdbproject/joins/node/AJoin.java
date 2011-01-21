/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.node;

import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeTracker;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoschi
 */
abstract class AJoin {

	protected Relation joined;

	protected void initJoined(Relation r, Relation s) {
		this.joined = new Relation("joined");
		for (String name : r.getColumnNames()) {
			joined.addColumn(name);
		}
		for (String name : s.getColumnNames()) {
			joined.addColumn(name);
		}
	}

	protected void initJoined(List<String> columnNamesOfR, Relation s) {
		this.joined = new Relation("joined");
		for (String name : columnNamesOfR) {
			joined.addColumn(name);
		}
		for (String name : s.getColumnNames()) {
			joined.addColumn(name);
		}
	}

	protected void initJoined(List<String> columnNames, List<String> columnNames0) {
		this.joined = new Relation("joined");
		for (String name : columnNames) {
			joined.addColumn(name);
		}
		for (String name : columnNames0) {
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

	protected Relation projectTo(Relation s, String columnS) {
		int index = s.columnIndex(columnS);
		Relation ret = new Relation("project to " + columnS);
		ret.addColumn(columnS);

		List<String> myrow;
		for (List<String> row : s.getRows()) {
			myrow = new ArrayList<String>();
			myrow.add(row.get(index));
			ret.addRow(myrow);
		}
		return ret;
	}

}
