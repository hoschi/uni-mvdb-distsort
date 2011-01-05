/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author hoschi
 */
public class MergeSorter extends ASorter {

	public MergeSorter(SortServer server) {
		super(server);
	}

	@Override
	public void sort() {
		this.list = mergeSort(this.list);
	}

	private List<String> mergeSort(List<String> unsorted) {
		if (unsorted.size() <= server.getBlockSize()) {
			return server.sortByClient(unsorted);
		} else {
			// divide in two
			int half = unsorted.size() / 2;
			List<String> left = unsorted.subList(0, half);
			List<String> right = unsorted.subList(half, unsorted.size());

			// sort/divide it
			left = this.mergeSort(left);
			right = this.mergeSort(right);

			// merge it
			List<String> sorted = new ArrayList<String>();
			int counterLeft = left.size();
			int counterRight = right.size();

			while (counterLeft != 0 || counterRight != 0) {
				if (counterLeft == 0) {// last element is on right side
					sorted.add(right.get(0));
					right = right.subList(1, right.size());
					counterRight--;
				} else if (counterRight == 0) { // last element is on left side
					sorted.add(left.get(0));
					left = left.subList(1, left.size());
					counterLeft--;
				} else if (left.get(0).compareToIgnoreCase(right.get(0)) <= 0) {
					sorted.add(left.get(0));
					left = left.subList(1, left.size());
					counterLeft--;
				} else {
					sorted.add(right.get(0));
					right = right.subList(1, right.size());
					counterRight--;
				}
			}
			return sorted;
		}
	}
}
