package de.uniluebeck.ifis.mvdbproject;

import org.junit.*;
import java.util.List;
import java.util.ArrayList;

import de.uniluebeck.ifis.mvdbproject.Server;

public class SortServer {
	List<String> unsorted;

	@Before
	public void setUp() throws Exception {
		this.unsorted = new ArrayList<String>();
		this.unsorted.add("a");
		this.unsorted.add("d");
		this.unsorted.add("c");
		this.unsorted.add("b");
	}

	@Test
	public void testAddMethod() {
		Server server = new Server();
		for (String s : this.unsorted) {
			server.add(s);
		}
		
		List<String> list = server.getList();
		Assert.assertArrayEquals("server list and local list aren't equal",
				this.unsorted.toArray(), list.toArray());
	}

}
