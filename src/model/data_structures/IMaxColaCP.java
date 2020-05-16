package model.data_structures;

import java.util.Iterator;

public interface IMaxColaCP<K extends Comparable<K>>
{
	
	public int size();// number of keys in the priority queue
	public void insert(K key);// insert a key into the priority queue
	public K max();// return the largest key
	K delMax();// return and remove the largest key
	public boolean isEmpty();// is the priority queue empty?
	 public Iterator<K> iterator();

}
