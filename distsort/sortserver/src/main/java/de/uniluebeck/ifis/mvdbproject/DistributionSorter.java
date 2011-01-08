/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
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

	public DistributionSorter(SortServer server, Random rand) {
		super(server);
		this.rand = rand;
	}

	@Override
	public void sort() {
		long start = GregorianCalendar.getInstance().getTimeInMillis();
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
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("overhead: " + (end - start) + "ms");
		start = GregorianCalendar.getInstance().getTimeInMillis();

		this.list.clear();
		// sort
		for (int i = 0; i < this.buckets.size(); ++i) {
			server.sortByClient(this.buckets.get(i), i);
		}
		end = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("sorting: " + (end - start) + "ms");
		start = GregorianCalendar.getInstance().getTimeInMillis();
		// join
		for (int i = 0; i < this.buckets.size(); ++i) {
			list.addAll(server.getSortedFromClient(i));
			//this.buckets.set(i, server.getSortedFromClient(i));
		}
		end = GregorianCalendar.getInstance().getTimeInMillis();
		System.out.println("joining: " + (end - start) + "ms");
	}
}
