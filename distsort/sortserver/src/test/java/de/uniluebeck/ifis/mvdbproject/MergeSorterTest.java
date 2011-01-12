/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.util.ArrayList;
import java.util.List;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author hoschi
 */
public class MergeSorterTest {

	List<String> unsorted;
	List<String> sorted;

	public MergeSorterTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
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
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of sort method, of class MergeSorter.
	 */
	@Test
	public void testSort() {
		// setup
		SortServer server = createMock(SortServer.class);

		expect(server.getBlockSize()).andReturn(2);
		expectLastCall().anyTimes();
		expect(server.getClientCount()).andReturn(2);
		expectLastCall().anyTimes();


		server.sortByClient(this.unsorted.subList(0, 2), 0);
		server.sortByClient(this.unsorted.subList(2, 4), 1);
		replay(server);

		// test
		MergeSorter instance = new MergeSorter(server);
		instance.setList(this.unsorted);
		instance.sort();
		verify(server);

	}

	@Test
	public void testHasNext() {
		// setup
		SortServer server = createMock(SortServer.class);

		expect(server.getBlockSize()).andReturn(2);
		expectLastCall().anyTimes();
		expect(server.getClientCount()).andReturn(2);
		expectLastCall().anyTimes();
		server.sortByClient(anyObject(List.class), anyInt());
		expectLastCall().anyTimes();
		expect(server.getSortedFromClient(anyInt())).andReturn(null).anyTimes();
		replay(server);

		// test
		MergeSorter instance = new MergeSorter(server);
		instance.setList(this.unsorted);
		instance.sort();

		int i = 0;
		while (instance.hasNext()) {
			instance.next();
			i++;
		}
		Assert.assertEquals(4, i);
		verify(server);
	}

	@Test
	public void testNext() {
		// setup
		SortServer server = createMock(SortServer.class);

		expect(server.getBlockSize()).andReturn(2);
		expectLastCall().anyTimes();
		expect(server.getClientCount()).andReturn(2);
		expectLastCall().anyTimes();
		server.sortByClient(this.unsorted.subList(0, 2), 0);
		server.sortByClient(this.unsorted.subList(2, 4), 1);
		expect(server.getSortedFromClient(0))
				.andReturn(this.sorted.subList(0,2));
		expect(server.getSortedFromClient(1))
				.andReturn(this.sorted.subList(2,4));
		replay(server);

		// test
		MergeSorter instance = new MergeSorter(server);
		instance.setList(this.unsorted);
		instance.sort();

		Assert.assertEquals("a", instance.next());
		Assert.assertEquals("b", instance.next());
		Assert.assertEquals("c", instance.next());
		Assert.assertEquals("d", instance.next());
		verify(server);
	}
}
