/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.ifis.mvdbproject.joins.joinnode;

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
		s.addRow(row);
		row.clear();

		row.add("2");
		row.add("2");
		r.addRow(row);
		s.addRow(row);
		row.clear();

		// add rows that don't join
		row.add("3");
		row.add("-1");
		r.addRow(row);
		row.clear();

		row.add("3");
		row.add("-2");
		s.addRow(row);
		row.clear();

		row.add("4");
		row.add("-3");
		r.addRow(row);
		row.clear();

		row.add("4");
		row.add("-4");
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

		row.clear();
		row.add("2");
		row.add("2");
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
		assertArrayEquals(joined.toArray(), test.toArray());
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
		assertArrayEquals(joined.toArray(), test.toArray());
	}

	@Test
	public void testSemiJoinV1V2() throws Exception {
		System.out.println("fetch as needed test");
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
		assertArrayEquals(joined.toArray(), test.toArray());
	}
}