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

	private int counter;
	private List<List<String>> buckets;

	public MergeSorter(SortServer server) {
		super(server);
		counter = 0;
		buckets = null;
	}

	@Override
	public void sort() {

		int chunk = (int) Math.ceil(this.list.size() / server.getClientCount());
		// move stuff to clients
		for (int i = 0; i < server.getClientCount(); ++i) {
			List<String> temp = new ArrayList<String>();
			temp.addAll(this.list.subList(i * chunk, i * chunk + chunk));
			server.sortByClient(temp, i);
		}
		this.counter = this.list.size();
		this.list.clear();
		buckets = null;
	}

	@Override
	public boolean hasNext() {
		return this.counter > 0;
	}

	@Override
	public String next() {

		if (buckets == null) {// initialize
			buckets = new ArrayList<List<String>>();
			for (int i = 0; i < server.getClientCount(); ++i) {
				List<String> sortedFromClient = server.getSortedFromClient(i);
				if (sortedFromClient != null) {
					buckets.add(new ArrayList<String>(sortedFromClient));
				}
			}
		}

		String ret = null;
		List<String> correspondingBucket = null;
		for (List<String> bucket : buckets) {
			if (bucket == null || bucket.size() <= 0) {
				continue;
			}

			if (ret == null) {
				ret = bucket.get(0);
				correspondingBucket = bucket;
			} else if (bucket.get(0).compareToIgnoreCase(ret) < 0) {
				ret = bucket.get(0);
				correspondingBucket = bucket;
			}
		}

		if (correspondingBucket != null && !correspondingBucket.isEmpty()) {
			correspondingBucket.remove(0);
		}

		--counter;
		return ret;
	}

}
