package de.uniluebeck.ifis.mvdbproject;

import java.rmi.RemoteException;
import org.junit.*;
import java.util.*;

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
	public void testSort() throws RemoteException {
		SortClient client = new SortClient();
		List<String> sort = client.sort(unsorted);
		Assert.assertArrayEquals("client list and local list aren't equal",
				this.sorted.toArray(), sort.toArray());

	}

	@Test
	public void testAddAndSort() throws RemoteException {
		SortClient client = new SortClient();
		client.add(unsorted);

		client.sort();

		List<String> sort = new ArrayList<String>();
		boolean run = true;
		while (run) {
			List<String> sortedBlock = client.getSortedBlock(2);
			if (sortedBlock != null) {
				sort.addAll(sortedBlock);
			} else {
				run = false;
			}
		}
		Assert.assertArrayEquals("client list and local list aren't equal",
				this.sorted.toArray(), sort.toArray());

		client.add(unsorted);

		client.sort();

		sort = new ArrayList<String>();
		run = true;
		while (run) {
			List<String> sortedBlock = client.getSortedBlock(3);
			if (sortedBlock != null) {
				sort.addAll(sortedBlock);
			} else {
				run = false;
			}
		}
		Assert.assertArrayEquals("client list and local list aren't equal",
				this.sorted.toArray(), sort.toArray());

	}
}
