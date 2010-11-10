package de.uniluebeck.ifis.mvdbproject;

import org.junit.*;
import java.util.*;

import de.uniluebeck.ifis.mvdbproject.*;

public class SortClientTest {
	List<String> unsorted;
	List<String> sorted;

	@Before
	public void setUp() throws Exception {
		this.unsorted = new ArrayList<String>();
		this.unsorted.add("a");
		this.unsorted.add("d");
		this.unsorted.add("c");
		this.unsorted.add("b");

		this.sorted = new ArrayList<String>();
		this.sorted.add("a");
		this.sorted.add("b");
		this.sorted.add("c");
		this.sorted.add("d");
	}

	@Test
	public void testSort() {
		Client client = new Client();
		client.setList(unsorted);

		client.sort();
		Assert.assertArrayEquals("client list and local list aren't equal",
				this.sorted.toArray(), client.getList().toArray());

	}

}
