package de.uniluebeck.ifis.mvdbproject;

import java.util.*;

public class SortClient {
	protected List<String> list;
	
	public List<String> getList() {
		return this.list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public void sort() {
		Collections.sort(this.list, String.CASE_INSENSITIVE_ORDER);
	}

}
