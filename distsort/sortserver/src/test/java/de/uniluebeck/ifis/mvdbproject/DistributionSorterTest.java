/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

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

		expect(server.getBlockSize()).andReturn(2);
		expectLastCall().anyTimes();
		
		expect(server.getClientCount()).andReturn(2);
		expect(server.sortByClient(anyObject(List.class), anyInt())).andAnswer(new IAnswer<List<String>>() {

			public List<String> answer() throws Throwable {
				LocalSorter l = new LocalSorter();
				l.setList((List<String>) getCurrentArguments()[0]);
				l.sort();
				return l.getSortedList();
			}
		});
		expectLastCall().anyTimes();
		replay(server);

		// test
		DistributionSorter instance = new DistributionSorter(server);
		instance.setList(this.unsorted);
		instance.sort();
		Assert.assertArrayEquals("server list and local list aren't equal",
				this.sorted.toArray(), instance.getSortedList().toArray());
		verify(server);
	}
}
