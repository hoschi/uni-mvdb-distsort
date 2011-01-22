/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.ifis.mvdbproject.joins.shared;

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
public class RelationTest {

    public RelationTest() {
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

	/**
	 * Test of filterDoubleRows method, of class Relation.
	 */ @Test
	public void testFilterDoubleRows() {
		System.out.println("filterDoubleRows");
		List<String> row = new ArrayList<String>();
		row.add("1");
		row.add("1");

		Relation instance = new Relation("test");
		instance.addColumn("a");
		instance.addColumn("b");
		instance.addRow(row);
		instance.addRow(row);
		instance.filterDoubleRows();
		
		assertEquals(1, instance.getRowCount());
		assertEquals(row, instance.getRows().get(0));
	}

}