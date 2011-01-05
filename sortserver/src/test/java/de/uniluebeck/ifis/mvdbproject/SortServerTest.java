package de.uniluebeck.ifis.mvdbproject;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SortServerTest {
	List<String> unsorted;
	List<String> sorted;
	SortServer server;

	class DummySorter extends ASorter {
		public DummySorter() {
			super(null);
		}
		public void sort() {
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

		server = SortServer.getInstance();
		for (String s : this.unsorted) {
			server.add(s);
		}
	}

	@After
	public void tearDown() throws Exception {
		server.clear();
	}

	@Test
	public void testAddElementsToSort() {
		List<String> list = server.getList();
		assertArrayEquals("server list and local list aren't equal",
				this.unsorted.toArray(), list.toArray());
	}

	@Test
	public void testSettingDifferentSorter() {
		server.setSorter(new DummySorter());
		server.sort();
		assertArrayEquals("server list and local list aren't equal",
				this.unsorted.toArray(), server.getList().toArray());

		server.setSorter(new LocalSorter());
		server.sort();
		assertArrayEquals("server list and local list aren't equal",
				this.sorted.toArray(), server.getList().toArray());
	}

	@Test
	public void testSortByClient() {
		fail("not implemented");
	}
}
