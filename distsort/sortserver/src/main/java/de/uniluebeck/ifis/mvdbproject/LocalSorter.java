/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import de.uniluebeck.ifis.mvdbproject.ASorter;
import de.uniluebeck.ifis.mvdbproject.ASorter;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author hoschi
 */
class LocalSorter extends ASorter {
	private Iterator<String> iterator;
	public LocalSorter() {
		super(null);
	}

	public void sort() {
		Collections.sort(this.list, String.CASE_INSENSITIVE_ORDER);
		iterator = this.list.iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public String next() {
		return iterator.next();
	}
}
