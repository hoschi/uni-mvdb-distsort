/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.ifis.mvdbproject.joins.joinnode;

import de.uniluebeck.ifis.mvdbproject.joins.node.Node;
import de.uniluebeck.ifis.mvdbproject.joins.shared.Relation;
import java.util.ArrayList;
import java.util.List;
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

	@After
	public void tearDown() {
	}// </editor-fold>

	@Test
	public void testJoin() throws Exception {
		Node instance = new Node();
		instance.add(s);
		instance.join(r,"b","d");
		Relation test = instance.getJoined();
		assertArrayEquals(joined.toArray(), test.toArray());
	}
}