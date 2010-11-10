package de.uniluebeck.ifis.mvdbproject;

import org.junit.*;
import java.util.*;

import de.uniluebeck.ifis.mvdbproject.*;

public class SortServerTest {
	List<String> unsorted;
	List<String> sorted;
	Server server;

	class DummySorter extends ASorter {
		public void sort() {
		}
	}

	class LocalSorter extends ASorter {
		public void sort() {
			Collections.sort(this.list, String.CASE_INSENSITIVE_ORDER);
		}
	}

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

		server.setSorter(new LocalSorter());
		server.sort();
		Assert.assertArrayEquals("server list and local list aren't equal",
				this.sorted.toArray(), server.getList().toArray());
	}

}
