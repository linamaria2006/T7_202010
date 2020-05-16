package model.logic;

import java.util.Comparator;

public class ComparadorGravedad implements Comparator<Multa>
{

	public int compare(Multa o1, Multa o2)
	{
		int comparacion = 0;
		if(o1.getServicio().compareTo(o2.getServicio()) > 0)
		{
			comparacion = 1;
		}
		else if( o1.getServicio().compareTo(o2.getServicio()) < 0)
		{
			comparacion = -1;
		}
		else
		{
			if(o1.getInfraccion().compareTo(o2.getInfraccion()) > 0)
			{
				comparacion = 1;
			}
			else if(o1.getInfraccion().compareTo(o2.getInfraccion()) < 0)
			{
				comparacion = -1;
			}
		}
		return comparacion;
	}

}
