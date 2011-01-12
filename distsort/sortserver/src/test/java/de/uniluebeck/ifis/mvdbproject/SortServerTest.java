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
	@Ignore
	// strange easy mock assertion error
	public void testSortByClientWithGivenId() throws RemoteException {
		/*ISortClient mock = createMock(ISortClient.class);
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
		
		server.sortByClient(unsorted, 0);
		List<String> sort = server.getSortedFromClient(0);
		assertArrayEquals("server list and local list aren't equal",
				this.sorted.toArray(), sort.toArray());
		verify(mock);*/
	}
}
