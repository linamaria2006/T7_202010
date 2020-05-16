package model.data_structures;

import java.util.Iterator;

public interface IHashTable <K, V> 
{
	public void put(K key, V val);
	public V get(K key);
	public void delete (K key);
	
	Iterable<K> keys();
	Iterable<K> keys(K min, K max);
	

}
