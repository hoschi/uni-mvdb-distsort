package de.uniluebeck.ifis.mvdbproject;

import java.util.List;

public abstract class ASorter implements ISorter {
	protected List<String> list;

	public void setList(List<String> list) {
		this.list = list;
	}

}
