/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.ifis.mvdbproject.joins.joinnode;

import org.junit.Ignore;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeTracker;
import de.uniluebeck.ifis.mvdbproject.joins.node.Node;
import de.uniluebeck.ifis.mvdbproject.joins.shared.IJoinServer;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeEntry;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
/**
 *
 * @author hoschi
 */
public class NodeTest {
	Relation r,s,joined;

    // <editor-fold defaultstate="collapsed" desc="set up / tear down">
	public NodeTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		r = new Relation("r");
		r.addColumn("a");
		r.addColumn("j");

		s = new Relation("s");
		s.addColumn("b");
		s.addColumn("j");

		List<String> row = new ArrayList<String>();
		row.add("1");
		row.add("1");
		r.addRow(row);
		row.clear();

		row.add("2");
		row.add("2");
		r.addRow(row);
		row.clear();

		row.add("10");
		row.add("2");
		r.addRow(row);
		row.clear();

		row.add("1");
		row.add("-1");
		r.addRow(row);
		row.clear();


		row.add("1");
		row.add("1");
		s.addRow(row);
		row.clear();

		row.add("10");
		row.add("1");
		s.addRow(row);
		row.clear();

		row.add("3");
		row.add("2");
		s.addRow(row);
		row.clear();

		row.add("1");
		row.add("-2");
		s.addRow(row);
		row.clear();



		joined = new Relation("joined");
		joined.addColumn("a");
		joined.addColumn("b");
		joined.addColumn("j");

		row.add("1");
		row.add("1");
		row.add("1");
		joined.addRow(row);
		row.clear();

		row.add("1");
		row.add("10");
		row.add("1");
		joined.addRow(row);
		row.clear();

		row.clear();
		row.add("2");
		row.add("3");
		row.add("2");
		joined.addRow(row);
		row.clear();

		row.clear();
		row.add("10");
		row.add("3");
		row.add("2");
		joined.addRow(row);
		row.clear();
	}

	@After
	public void tearDown() {
	}// </editor-fold>

	@Test
	public void testJoinShipWhole() throws Exception {
		System.out.println("ship whole test");
		IJoinServer server = createMock(IJoinServer.class);

		server.addMeasurment((TimeEntry) anyObject());
		expectLastCall().anyTimes();

		TimeTracker tracker = new TimeTracker(server);
		Node instance = new Node(tracker);
		instance.add(s);
		instance.joinShipWhole(r);
		Relation test = instance.getJoined();

		assertListEquals(joined.getRows(), test.getRows());
	}

	@Test
	public void testFetchAsNeeded() throws Exception {
		System.out.println("fetch as needed test");
		IJoinServer server = createMock(IJoinServer.class);

		server.addMeasurment((TimeEntry) anyObject());
		expectLastCall().anyTimes();

		TimeTracker tracker = new TimeTracker(server);
		Node instance = new Node(tracker);
		Node nodeR = new Node(tracker);
		nodeR.add(r);
		instance.add(s);
		instance.joinFetchAsNeeded(nodeR.getRmiName(), nodeR.getPort());
		Relation test = instance.getJoined();
		assertListEquals(joined.getRows(), test.getRows());
	}

	@Test
	public void testSemiJoinV1V2() throws Exception {
		System.out.println("semi join v1 and v3 test");
		IJoinServer server = createMock(IJoinServer.class);

		server.addMeasurment((TimeEntry) anyObject());
		expectLastCall().anyTimes();

		TimeTracker tracker = new TimeTracker(server);
		Node instance = new Node(tracker);
		Node nodeR = new Node(tracker);
		nodeR.add(r);
		instance.add(s);
		instance.joinSemiV1V2(nodeR.getRmiName(), nodeR.getPort());
		Relation test = instance.getJoined();
		assertListEquals(joined.getRows(), test.getRows());
	}

	@Test
	public void testSemiJoinV3() throws Exception {
		System.out.println("semi join v3 test");
		IJoinServer server = createMock(IJoinServer.class);

		server.addMeasurment((TimeEntry) anyObject());
		expectLastCall().anyTimes();

		TimeTracker tracker = new TimeTracker(server);
		Node instance = new Node(tracker);
		Node nodeR = new Node(tracker);
		Node nodeS = new Node(tracker);
		nodeR.add(r);
		nodeS.add(s);
		instance.joinSemiV3(nodeR.getRmiName(), nodeR.getPort(),
				nodeS.getRmiName(), nodeS.getPort());
		Relation test = instance.getJoined();
		assertListEquals(joined.getRows(), test.getRows());
	}

	@Test
	@Ignore // easymock can't work with threads :(
	public void testSemiJoinV4() throws Exception {
		System.out.println("semi join v4 test");
		IJoinServer server = createMock(IJoinServer.class);
		TimeTracker tracker = createMock(TimeTracker.class);

		tracker.takeTime((String)anyObject(), (TimeEntry.Type) anyObject());
		expectLastCall().anyTimes();

		Node instance = new Node(tracker);
		Node nodeR = new Node(tracker);
		Node nodeS = new Node(tracker);
		nodeR.add(r);
		nodeS.add(s);
		instance.joinSemiV4(nodeR.getRmiName(), nodeR.getPort(),
				nodeS.getRmiName(), nodeS.getPort());
		Relation test = instance.getJoined();
		assertListEquals(joined.getRows(), test.getRows());
	}

	private void assertListEquals(List<List<String>> expected, List<List<String>> given) {
		assertEquals(expected.size(), given.size());
		for (List<String> row : given) {
			assertTrue(expected.contains(row));
		}
	}
}