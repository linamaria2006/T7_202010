package test.data_structures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import model.data_structures.LinearProbingHashST;
import model.data_structures.Queue;
import model.data_structures.SeparateChainingHashST;

public class TestLinearProbingHashST  extends TestCase
{
	private LinearProbingHashST<Integer, String> hash;
	
	@Before
	public void setUp1()
	{
		hash = new LinearProbingHashST<Integer, String>();
		
		hash.put(1, "A");
		hash.put(2,"B");
		hash.put(0, "Z");
	}
	
	@Test
	public void testSize()
	{
		setUp1();
		assertEquals(3, hash.size());
	}
	
	@Test
	public void testPut()
	{
		setUp1();
		hash.put(10, "X");
		assertEquals("X", hash.get(10));
		
	}
	
	@Test
	public void testGet()
	{
		setUp1();
		assertEquals("A", hash.get(1));
	}
	
	@Test
	public void testDelete()
	{
		setUp1();
		hash.delete(1);
		assertEquals(2, hash.size());
	}
	
	@Test
	public void testKeys()
	{
		setUp1();
		Queue q = (Queue) hash.keys();
		assertTrue(q.dequeue() != null);
	}


}
