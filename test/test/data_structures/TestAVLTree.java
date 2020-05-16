package test.data_structures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import model.data_structures.AVLTreeST;
import model.data_structures.Queue;
import model.data_structures.SeparateChainingHashST;

public class TestAVLTree extends TestCase
{

	private AVLTreeST<Integer, String> arbol;
	
	@Before
	public void setUp1()
	{
		arbol = new AVLTreeST<Integer, String>();
		
		arbol.put(1, "A");
		arbol.put(2,"B");
		arbol.put(0, "Z");
	}
	
	@Test
	public void testSize()
	{
		setUp1();
		assertEquals(3, arbol.size());
	}
	
	@Test
	public void testPut()
	{
		setUp1();
		arbol.put(10, "X");
		assertEquals("X", arbol.get(10));
		
	}
	
	@Test
	public void testGet()
	{
		setUp1();
		assertEquals("A", arbol.get(1));
	}
	
	@Test
	public void testDelete()
	{
		setUp1();
		arbol.delete(1);
		assertEquals(2, arbol.size());
	}
	
	@Test
	public void testKeys()
	{
		setUp1();
		Queue q = (Queue) arbol.keys();
		assertTrue(q.dequeue() != null);
	}

}
