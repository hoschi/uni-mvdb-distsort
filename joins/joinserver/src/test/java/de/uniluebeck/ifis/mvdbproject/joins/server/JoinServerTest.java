/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.server;

import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeEntry;
import de.uniluebeck.ifis.mvdbproject.joins.shared.TimeTracker;
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hoschi
 */
public class JoinServerTest{

	public JoinServerTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCountNetworkTraffic() throws RemoteException {
		System.out.println("test network traffic");
		// setup
		JoinServer instance = new JoinServer();
		instance.startMeasurements();
		TimeTracker tracker = new TimeTracker(instance);

		tracker.takeTime("test", TimeEntry.Type.invoke);
		tracker.takeTime("test", TimeEntry.Type.get);
		tracker.takeTime("test", TimeEntry.Type.replay);
		tracker.takeTime("test", TimeEntry.Type.received);

		// test
		TimeEntry entry;
		instance.countNetworkTraffic();
		assertEquals(3, instance.measurements.size());

		entry = instance.measurements.get(0);
		assertEquals(TimeEntry.Type.traffic, entry.getType());
		entry = instance.measurements.get(1);
		assertEquals(TimeEntry.Type.logic, entry.getType());
		entry = instance.measurements.get(2);
		assertEquals(TimeEntry.Type.traffic, entry.getType());
	}

	@Test
	public void testCountNetworkTrafficWithNodeCommunication() throws RemoteException {
		System.out.println("test with node communication");
		// setup
		JoinServer instance = new JoinServer();
		instance.startMeasurements();
		TimeTracker tracker = new TimeTracker(instance);

		tracker.takeTime("test", TimeEntry.Type.invoke);
		tracker.takeTime("test", TimeEntry.Type.get);
		tracker.takeTime("join", TimeEntry.Type.invoke, true);
		tracker.takeTime("join", TimeEntry.Type.get, true);
		tracker.takeTime("join", TimeEntry.Type.replay, true);
		tracker.takeTime("join", TimeEntry.Type.received, true);
		tracker.takeTime("test", TimeEntry.Type.replay);
		tracker.takeTime("test", TimeEntry.Type.received);

		// test
		TimeEntry entry;
		instance.countNetworkTraffic();
		assertEquals(3, instance.measurements.size());
		
		entry = instance.measurements.get(0);
		assertEquals(TimeEntry.Type.traffic, entry.getType());
		entry = instance.measurements.get(1);
		assertEquals(TimeEntry.Type.logic, entry.getType());
		entry = instance.measurements.get(2);
		assertEquals(TimeEntry.Type.traffic, entry.getType());
	}
}
