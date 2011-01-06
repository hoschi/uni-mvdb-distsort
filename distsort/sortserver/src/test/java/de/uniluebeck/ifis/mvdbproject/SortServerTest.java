package de.uniluebeck.ifis.mvdbproject;

import java.rmi.RemoteException;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import java.util.*;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
	public void testSortByClient() throws RemoteException {
		ISortClient mock = createMock(ISortClient.class);
		expect(mock.sort(this.unsorted))
				.andAnswer(new IAnswer<List<String>>() {

			public List<String> answer() throws Throwable {
				LocalSorter l = new LocalSorter();
				l.setList((List<String>) getCurrentArguments()[0]);
				l.sort();
				return l.getSortedList();
			}
		});
		replay(mock);
		
		server.addClient(mock);
		List<String> sort = server.sortByClient(unsorted);
		assertArrayEquals("server list and local list aren't equal",
				this.sorted.toArray(), sort.toArray());
		verify(mock);
	}

	@Test
	@Ignore
	// strange easy mock assertion error
	public void testSortByClientWithGivenId() throws RemoteException {
		ISortClient mock = createMock(ISortClient.class);
		expect(mock.sort(anyObject(List.class)))
				.andAnswer(new IAnswer<List<String>>() {

			public List<String> answer() throws Throwable {
				LocalSorter l = new LocalSorter();
				l.setList((List<String>) getCurrentArguments()[0]);
				l.sort();
				return l.getSortedList();
			}
		});
		expectLastCall().anyTimes();
		replay(mock);

		server.addClient(mock);
		server.setBlockSize(2);
		
		List<String> sort = server.sortByClient(unsorted, 0);
		assertArrayEquals("server list and local list aren't equal",
				this.sorted.toArray(), sort.toArray());
		verify(mock);
	}
}
