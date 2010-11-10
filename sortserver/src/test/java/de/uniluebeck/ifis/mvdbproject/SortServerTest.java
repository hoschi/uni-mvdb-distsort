package de.uniluebeck.ifis.mvdbproject;

import org.junit.*;
import java.util.List;
import java.util.ArrayList;

import de.uniluebeck.ifis.mvdbproject.*;

public class SortServerTest {
	List<String> unsorted;
	Server server;

	class DummySorter extends ASorter {
		public void sort() {
		}
	}

	class LocalSorter extends ASorter {
		public void sort() {
			this.
		}
	}

	@Before
	public void setUp() throws Exception {
		this.unsorted = new ArrayList<String>();
		this.unsorted.add("a");
		this.unsorted.add("d");
		this.unsorted.add("c");
		this.unsorted.add("b");

		server = Server.getInstance();
		for (String s : this.unsorted) {
			server.add(s);
		}
	}

	@After
	public void tearDown() throws Exception {
		server.clear();
	}

	@Test
	public void testAdd() {
		List<String> list = server.getList();
		Assert.assertArrayEquals("server list and local list aren't equal",
				this.unsorted.toArray(), list.toArray());
	}

	@Test
	public void testSort() {
		server.setSorter(new DummySorter());
		server.sort();
		Assert.assertArrayEquals("server list and local list aren't equal",
				this.unsorted.toArray(), server.getList().toArray());
	}

}
