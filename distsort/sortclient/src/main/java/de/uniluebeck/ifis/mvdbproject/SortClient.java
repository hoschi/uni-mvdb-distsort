package de.uniluebeck.ifis.mvdbproject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class SortClient extends UnicastRemoteObject implements ISortClient {

	List<String> list;

	protected SortClient() throws RemoteException {
		super();
		list = new ArrayList<String>();
	}

	@Override
	public List<String> sort(final List<String> list) throws RemoteException {
		List<String> myList = new ArrayList<String>(list);
		Collections.sort(myList, String.CASE_INSENSITIVE_ORDER);
		System.out.println("sorting " + list + " to " + myList);
		return myList;
	}

	@Override
	public void add(List<String> unsorted) throws RemoteException {
		this.list.addAll(unsorted);
	}

	@Override
	public void sort() throws RemoteException {
		this.list = this.sort(this.list);
	}

	@Override
	public List<String> getSortedBlock(int blockSize) throws RemoteException {
		List<String> block = null;
		if (blockSize <= this.list.size()) {
			block = new ArrayList<String>(this.list.subList(0, blockSize));
			for (int i = 0; i < blockSize; ++i) {
				this.list.remove(0);
			}
		} else if (this.list.size() > 0) {
			block = new ArrayList<String>(this.list);
			this.list.clear();
		}
		return block;
	}
}
