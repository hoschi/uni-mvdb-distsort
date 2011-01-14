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

	private Random rand;
	private List<List<String>> buckets;
	private List<List<String>> sorted;
	private int counter;
	private Integer nextClient;

	public DistributionSorter(SortServer server, Random rand) {
		super(server);
		this.rand = rand;
		counter = 0;
		nextClient = null;
	}

	@Override
	public void sort() {
		if (server.getClientCount() < 2) {
			throw new RuntimeException("not enought clients to sort");
		}

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


		// sort
		for (int i = 0; i < this.buckets.size(); ++i) {
			server.sortByClient(this.buckets.get(i), i);
		}

		this.counter = this.list.size();
		this.list = null;
		buckets = null;
		nextClient = 0;
	}

	@Override
	public boolean hasNext() {
		return this.counter > 0;
	}

	@Override
	public String next() {
		// no more data to get
		if (nextClient == null) {
			this.counter = 0;
			return null;
		} else if (nextClient > server.getClientCount()) {
			nextClient = null;
			this.counter = 0;
			return null;
		}

		// get new list or exit
		if (this.list == null || this.list.size() <= 0) {
			this.list = new ArrayList<String>();
			this.list.addAll(server.getSortedFromClient(nextClient));
			++nextClient;
		}

		// return item
		String ret = this.list.get(0);
		this.list.remove(0);
		--counter;
		return ret;
	}
}
