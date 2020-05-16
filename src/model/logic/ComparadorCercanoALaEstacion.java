package model.logic;

import java.util.Comparator;

public class ComparadorCercanoALaEstacion implements Comparator<Multa> 
{
	private final static double LATITUD_ESTACION = 4.647586;
	private final static double LONGITUD_ESTCAION = -74.078122;

	private static final int EARTH_RADIUS = 6371;
	
	
	private double haversin(double val)
	{
		return Math.pow(Math.sin(val / 2), 2);
	}

	private double distancia (double latitud1, double longitud1)
	{

		double dLat  = Math.toRadians((LATITUD_ESTACION - latitud1));
		double dLong = Math.toRadians((LONGITUD_ESTCAION - longitud1));

		latitud1 = Math.toRadians(latitud1);
		double latitudEstacion   = Math.toRadians(LATITUD_ESTACION);

		double a = haversin(dLat) + Math.cos(latitud1) * Math.cos(latitudEstacion) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c; // <-- d
	}



	@Override
	public int compare(Multa m1, Multa m2)
	{
		double latitudM1 = m1.getGeo().getLatitud();
		double longitudM1 = m1.getGeo().getLongitud();
		double distanciaM1 = distancia(latitudM1, longitudM1);

		double latitudM2 = m2.getGeo().getLatitud();
		double longitudM2 = m2.getGeo().getLongitud();
		double distanciaM2 = distancia(latitudM2, longitudM2);

		int num = 0;

		if(distanciaM1 < distanciaM2) num = 1;
		else if(distanciaM1 > distanciaM2) num = -1; 

		return num;
	}


}
