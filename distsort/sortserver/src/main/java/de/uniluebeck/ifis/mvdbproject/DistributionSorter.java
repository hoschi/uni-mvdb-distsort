/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author hoschi
 */
public class DistributionSorter extends ASorter {

	Random rand;
	List<List<String>> buckets;
	List<List<String>> sorted;

	public DistributionSorter(SortServer server) {
		super(server);
		rand = new Random();
	}

	@Override
	public void sort() {

		List<String> bounds = new ArrayList<String>();
		int k = server.getClientCount() - 1;
		buckets = new ArrayList<List<String>>();
		sorted = new ArrayList<List<String>>();

		// distcinct m_0 ... m_k
		for (int i = 0; i < k; ++i) {
			bounds.add(this.list.get(rand.nextInt(this.list.size())));
			buckets.add(new ArrayList<String>());
			sorted.add(new ArrayList<String>());
		}
		buckets.add(new ArrayList<String>());
		sorted.add(new ArrayList<String>());
		Collections.sort(bounds, String.CASE_INSENSITIVE_ORDER);


		// fill server buckets
		for (String s : this.list) {
			// e <= m_i and i = 0
			if (s.compareToIgnoreCase(bounds.get(0)) <= 0) {
				this.buckets.get(0).add(s);
			}

			// rest
			for (int i = 1; i < k; ++i) {
				if (s.compareToIgnoreCase(bounds.get(i - 1)) > 0
						&& s.compareToIgnoreCase(bounds.get(i)) <= 0) {
					this.buckets.get(i).add(s);
					break;
				}
			}

			// e > m_k and i = k+1
			if (s.compareToIgnoreCase(bounds.get(k - 1)) > 0) {
				this.buckets.get(k).add(s);
			}
		}

		// join it
		this.list.clear();
		for (int i = 0; i < this.buckets.size(); ++i) {
			this.list.addAll(server.sortByClient(this.buckets.get(i), i));
		}
	}

}
