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
		LocalSorter l = new LocalSorter();
		Server server = createMock(Server.class);
		expect(server.getBlockSize()).andReturn(2);
		expectLastCall().anyTimes();
		expect(server.sortByClient(anyObject(List.class)))
				.andAnswer(new IAnswer<List<String>>() {

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
		MergeSorter instance = new MergeSorter(server);
		instance.setList(this.unsorted);
		instance.sort();
		Assert.assertArrayEquals("server list and local list aren't equal",
				this.sorted.toArray(), instance.getSortedList().toArray());


	}
}
