/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author hoschi
 */
public interface ISortClient extends Remote {

	public List<String> sort(List<String> list) throws RemoteException;

	public void add(List<String> unsorted) throws RemoteException;

	public void sort() throws RemoteException;

	public List<String> getSortedBlock(int blockSize) throws RemoteException;
}
