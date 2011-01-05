/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.util.Collections;

/**
 *
 * @author hoschi
 */
class LocalSorter extends ASorter {
	public LocalSorter() {
		super(null);
	}

	public void sort() {
		Collections.sort(this.list, String.CASE_INSENSITIVE_ORDER);
	}
}
