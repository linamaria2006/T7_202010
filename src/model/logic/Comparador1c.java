package model.logic;

import java.util.Comparator;

public class Comparador1c implements Comparator<Multa>
{

	@Override
	public int compare(Multa o1, Multa o2) {
		
		
		int comparacion = 0;
		if (o1.getNumeroDiasEspera() > 3 && o2.getNumeroDiasEspera() <= 3)
		{
			comparacion = 1;
		}
		
		else if ( o1.getNumeroDiasEspera() <= 3 && o2.getNumeroDiasEspera() >3)
		{
			comparacion = -1;
		}
		else {
			
			boolean condicion1 = o1.getDescripcion().contains("SERA INMOVILIZADO") || o1.getDescripcion().contains("SERÁ INMOVILIZADO");
			boolean condicion2 = !o2.getDescripcion().contains("SERÁ INMOVILIZADO")  || !o2.getDescripcion().contains("SERA INMOVILIZADO");
			boolean condicion3 = o2.getDescripcion().contains("SERÁ INMOVILIZADO")  || o2.getDescripcion().contains("SERA INMOVILIZADO");
			boolean condicion4 = !o1.getDescripcion().contains("SERA INMOVILIZADO") || !o1.getDescripcion().contains("SERÁ INMOVILIZADO");
			if (condicion1 && condicion2 )
					{
				comparacion = 1;
					}
			else if (condicion3 && condicion4)
			{
				comparacion = -1;
			}
			else {
				boolean condicion5 = o1.getDescripcion().contains("SERA INMOVILIZADO") ;
				boolean condicion6 = !o2.getDescripcion().contains("SERÁ INMOVILIZADO")  ;
				boolean condicion7 = o2.getDescripcion().contains("SERÁ INMOVILIZADO")  ;
				boolean condicion8 = !o1.getDescripcion().contains("SERA INMOVILIZADO") ;
				
				if (condicion5 && condicion6)
				{
					comparacion = 1;
				}
				else if (condicion7 && condicion8)
				{
					comparacion = -1;
				}
				else 
				{
					comparacion = 0;
				}
			}
		}
		
		
		
		return comparacion;
	}

}
