package de.uniluebeck.ifis.mvdbproject;

import org.junit.*;
import java.util.List;
import java.util.ArrayList;

public class SortServer {
	List<String> unsortet;

	@Before
	public void setUp() throws Exception {
		this.unsortet = new ArrayList<String>();
		this.unsortet.add("a");
		this.unsortet.add("d");
		this.unsortet.add("c");
		this.unsortet.add("b");
	}

}
