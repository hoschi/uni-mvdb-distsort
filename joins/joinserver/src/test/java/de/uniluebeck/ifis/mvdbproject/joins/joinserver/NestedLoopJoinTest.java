/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.ifis.mvdbproject.joins.joinserver;

import java.util.List;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author hoschi
 */
public class NestedLoopJoinTest {
	Relation r,s,joined;

    // <editor-fold defaultstate="collapsed" desc="set up / tear down">
	public NestedLoopJoinTest() {
	}

	@org.junit.BeforeClass
	public static void setUpClass() throws Exception {
	}

	@org.junit.AfterClass
	public static void tearDownClass() throws Exception {
	}

	@org.junit.Before
	public void setUp() throws Exception {
		r = new Relation("r");
		r.addColumn("a");
		r.addColumn("b");

		s = new Relation("s");
		s.addColumn("c");
		s.addColumn("d");

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
		joined.addColumn("c");
		joined.addColumn("d");

		row.add("1");
		row.add("1");
		row.add("1");
		row.add("1");
		joined.addRow(row);
		row.clear();

		row.clear();
		row.add("2");
		row.add("2");
		row.add("2");
		row.add("2");
		joined.addRow(row);
		row.clear();

	}

	@org.junit.After
	public void tearDown() throws Exception {
	}// </editor-fold>

	@Test
	public void testJoin() {
		NestedLoopJoin instance = new NestedLoopJoin();
		Relation test = instance.join(r,s,"b","d");
		assertArrayEquals(joined.toArray(), test.toArray());
	}

}