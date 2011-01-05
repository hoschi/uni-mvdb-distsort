package de.uniluebeck.ifis.mvdbproject;

import java.util.List;

public abstract class ASorter implements ISorter {

	protected List<String> list;
	protected SortServer server;

	public void setList(List<String> list) {
		this.list = list;
	}

	public List<String> getSortedList() {
		return this.list;
	}

	protected ASorter(SortServer server) {
		this.server = server;
	}
}
