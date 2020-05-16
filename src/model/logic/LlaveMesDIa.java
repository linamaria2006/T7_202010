package model.logic;

public class LlaveMesDIa 
{
	private int mes;
	private String dia;

	public LlaveMesDIa(int pMes, int pDia)
	{
		mes = pMes;
		
		switch (pDia)
		{
		case 1:
			dia = "D";
			break;
		case 2: 
			dia  = "L";
			break;
		case 3 :
			dia = "M";
			break;
		case 4 : 
			dia = "I";
			break;
		case 5 : 
			dia = "J";
			break;
		case 6 : 
			dia = "V";
			break;
		case 7 : 
			dia = "S";
			break;
		}

	}
	public LlaveMesDIa(int pMes, String pDia)
	{
		mes = pMes;
		
		dia = pDia;

	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + mes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LlaveMesDIa other = (LlaveMesDIa) obj;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		if (mes != other.mes)
			return false;
		return true;
	}

	
}
