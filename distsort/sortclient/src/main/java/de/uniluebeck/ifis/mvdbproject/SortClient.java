package de.uniluebeck.ifis.mvdbproject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class SortClient extends UnicastRemoteObject implements ISortClient {
	protected SortClient() throws RemoteException {super();}
	public List<String> sort(final List<String> list) throws RemoteException {
		List<String> myList = new ArrayList<String>(list);
		Collections.sort(myList, String.CASE_INSENSITIVE_ORDER);
		System.out.println("sorting " + list);
		return myList;
	}

}
