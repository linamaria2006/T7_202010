package test.data_structures;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import model.data_structures.MaxPQ;

public class TestMaxPQ extends TestCase
{
	private MaxPQ<Integer> pq;

	@Before
	public void setUp1()
	{
		pq = new MaxPQ<Integer>(10);

		pq.insert(1);
		pq.insert(2);
		pq.insert(3);
	}

	@Test
	public void testSize()
	{
		setUp1();
		assertEquals(3, pq.size());

	}

	@Test
	public void testInsert()
	{
		setUp1();
		pq.insert(9);
		assertTrue(9 == (Integer)pq.max());
		assertEquals(4, pq.size());
	}

	@Test
	public void testMax()
	{
		setUp1();
		assertTrue(3 == pq.max());
	}

	@Test
	public void testDelMax()
	{
		setUp1();
		int max = pq.delMax();
		assertTrue(3 == max);
		assertEquals(2, pq.size());
	}

}
