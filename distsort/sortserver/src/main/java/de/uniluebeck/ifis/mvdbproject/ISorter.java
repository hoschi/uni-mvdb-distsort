package de.uniluebeck.ifis.mvdbproject;

import java.util.*;

public interface ISorter extends Iterator<String>{
	public void sort();
	public void setList(List<String> list);
	public List<String> getSortedList();
}
