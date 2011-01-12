/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.easymock.EasyMock.*;

/**
 *
 * @author hoschi
 */
public class DistributionSorterTest {

	List<String> unsorted;
	List<String> sorted;

	public DistributionSorterTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
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
	 * Test of sort method, of class DistributionSorter.
	 */
	@Test
	public void testSort() {
		// setup
		SortServer server = createMock(SortServer.class);
		Random random = createMock(Random.class);

		expect(random.nextInt(4)).andReturn(2); // means c is the bound

		expect(server.getBlockSize()).andReturn(3).anyTimes();
		expect(server.getClientCount()).andReturn(2).anyTimes();

		List<String> client1 = new ArrayList<String>();
		client1.add("a");
		client1.add("c");
		client1.add("b");
		List<String> client2 = new ArrayList<String>();
		client2.add("d");

		server.sortByClient(client1, 0);
		server.sortByClient(client2, 1);

		replay(server);
		replay(random);

		// test
		DistributionSorter instance = new DistributionSorter(server, random);
		instance.setList(this.unsorted);
		instance.sort();
		verify(server);
		verify(random);
	}

	@Test
	public void testHasNext() {
		// setup
		SortServer server = createMock(SortServer.class);

		expect(server.getBlockSize()).andReturn(2).anyTimes();
		expect(server.getClientCount()).andReturn(2).anyTimes();

		server.sortByClient(anyObject(List.class), anyInt());
		expectLastCall().times(2);
		expect(server.getSortedFromClient(0)).andReturn(this.sorted.subList(0, 3)); // a, b, c
		expect(server.getSortedFromClient(1)).andReturn(this.sorted.subList(3, 4)); // d

		replay(server);


		// test
		DistributionSorter instance = new DistributionSorter(server, new Random());
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
		Random random = createMock(Random.class);
		expect(random.nextInt(4)).andReturn(2); // means c is the bound

		SortServer server = createMock(SortServer.class);

		expect(server.getBlockSize()).andReturn(2).anyTimes();
		expect(server.getClientCount()).andReturn(2).anyTimes();

		server.sortByClient(anyObject(List.class), anyInt());
		expectLastCall().times(2);
		expect(server.getSortedFromClient(0)).andReturn(this.sorted.subList(0, 3)); // a, b, c
		expect(server.getSortedFromClient(1)).andReturn(this.sorted.subList(3, 4)); // d

		replay(random);
		replay(server);

		// test
		DistributionSorter instance = new DistributionSorter(server, random);
		instance.setList(this.unsorted);
		instance.sort();

		Assert.assertEquals("a", instance.next());
		Assert.assertEquals("b", instance.next());
		Assert.assertEquals("c", instance.next());
		Assert.assertEquals("d", instance.next());
		verify(server);
		verify(random);
	}
}
