/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.shared;

import java.io.Serializable;
import java.lang.String;
import java.lang.String;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.List;

/**
 *
 * @author hoschi
 */
public class Relation implements Serializable{

	private List<String> columnNames;
	private List<List<String>> rows;
	private String name;

	public Relation(String name) {
		this.columnNames = new ArrayList<String>();
		this.rows = new ArrayList<List<String>>();
		this.name = name;
	}

	public List<String> getRow(int number) {
		return Collections.unmodifiableList(rows.get(number));
	}

	public void addRow(List<String> names) {
		if (names.size() != columnNames.size()) {
			throw new IllegalArgumentException("bad row size");
		}
		rows.add(new ArrayList<String>(names));
	}

	public void addColumn(String name) {
		columnNames.add(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object[] toArray() {
		return rows.toArray();
	}

	public String toString() {
		String s = "table '" + this.name + "'\n\tcolumns: '";
		for (String c : this.columnNames) {
			s += c + ", ";
		}
		s += "'\n\trows:\n";

		if (this.rows != null) {
			for (List<String> row : this.rows) {
				s += "\t\t";
				for (String value : row) {
					s += value + "\t";
				}
				s += "\n";
			}
		}

		return s;
	}

	public boolean hasColumn(String name) {
		return columnNames.contains(name);
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public int columnIndex(String name) {
		return columnNames.indexOf(name);
	}

	public int getRowCount() {
		return this.rows.size();
	}

	public String findSameColumn(Relation s) {
		return findSameColumn(s.getColumnNames());
	}

	public void filterDoubles() {
		for (int i = 0; i < columnNames.size(); ++i) {
			String name = columnNames.get(i);
			if (columnNames.lastIndexOf(name) != i) {
				removeColumnFromRows(i);
				columnNames.remove(i);
				filterDoubles();
				return;
			}
		}
	}

	private void removeColumnFromRows(int i) {
		for (List<String> row : rows) {
			row.remove(i);
		}
	}

	public String findSameColumn(List<String> columnNames) {
		for (String mycol : this.columnNames) {
			for (String yourcol : columnNames) {
				if (mycol.equals(yourcol)) {
					return mycol;
				}
			}
		}
		throw new RuntimeException("found no column");
	}
}
