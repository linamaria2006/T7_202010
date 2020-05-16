package model.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import model.data_structures.AVLTreeST;
import model.data_structures.IHashTable;
import model.data_structures.IMaxColaCP;
import model.data_structures.LinearProbingHashST;
import model.data_structures.MaxPQ;
import model.data_structures.Queue;
import model.data_structures.SeparateChainingHashST;
import model.data_structures.noExisteObjetoException;

/**
 * Definicion del modelo del mundo
 *
 */
public class Model
{
	//Constantes
	
	
	/**
	 * Atributos del modelo del mundo
	 */
	private Queue<Multa> datos; 
	private IMaxColaCP<Multa> heapMayorID;
	private IHashTable<LlaveMesDIa, Queue<Multa>> hash2A;
	private IMaxColaCP<Multa> heap1A;
	private AVLTreeST<Date, Multa> arbol3A; 
	private IMaxColaCP<Multa> heap1B;
	private IHashTable<Llave2B, Queue<Multa>> hash2B;
	private IHashTable<Double, Multa> arbol3B;
	private AVLTreeST<Date, ArrayList<Multa>> arbol1c;
	private IMaxColaCP<Multa> heap3c;





	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Model()
	{
		datos = new Queue<Multa>();

		heapMayorID = new MaxPQ<Multa>(31);

		ComparadorGravedad c = new ComparadorGravedad();
		heap1A= new MaxPQ<Multa>(31, c);

		hash2A = new SeparateChainingHashST<LlaveMesDIa, Queue<Multa>>(31);

		arbol3A = new AVLTreeST<Date, Multa>();

		ComparadorCercanoALaEstacion cc = new ComparadorCercanoALaEstacion();
		heap1B = new MaxPQ<Multa>(31, cc);

		hash2B = new LinearProbingHashST<Llave2B, Queue<Multa>>(31);

		arbol3B = new AVLTreeST<Double, Multa>();
		
		arbol1c = new AVLTreeST<Date , ArrayList<Multa>>();
		
		Comparador1c cd = new Comparador1c();
		heap3c = new MaxPQ<Multa>(2500, cd);
		
	}


	private void cargarDatos() throws noExisteObjetoException, FileNotFoundException 
	{
		String path = "./data/comparendos.geojson";
		JsonReader lector;
		InputStream inputstream = new FileInputStream(path);


		try {
			
//			lector = new JsonReader(new FileReader(path));
			lector = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
			JsonElement elem = JsonParser.parseReader(lector);
			JsonObject ja = elem.getAsJsonObject();

			JsonArray features = ja.getAsJsonArray("features");

			for(JsonElement e : features)
			{
				JsonObject propiedades = (JsonObject) e.getAsJsonObject().get("properties");

				long id = propiedades.get("OBJECTID").getAsLong();

				String cadenaFecha = propiedades.get("FECHA_HORA").getAsString();
				String anio = cadenaFecha.substring(0,4);
				String mes = cadenaFecha.substring(5,7);
				String dia = cadenaFecha.substring(8,10);
				String hora = cadenaFecha.substring(11,13);
				String min = cadenaFecha.substring(14,16);
				String seg = cadenaFecha.substring(17,19);
				String fechaConcatenadita = anio + "-" + mes +"-"+ dia + " " + hora + ":" + min + ":" + seg; 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date fecha = sdf.parse(fechaConcatenadita);
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				String fechaconcatenada2 = anio + "-" + mes + "-" +dia;
				Date fecha2 =sdf2.parse(fechaconcatenada2);
				


				String medioDete = propiedades.getAsJsonObject().get("MEDIO_DETECCION").getAsString();

				String claseVehiculo = propiedades.getAsJsonObject().get("CLASE_VEHICULO").getAsString();

				String tipoServicio = propiedades.getAsJsonObject().get("TIPO_SERVICIO").getAsString();

				String infraccion = propiedades.getAsJsonObject().get("INFRACCION").getAsString();

				String descripcion = propiedades.getAsJsonObject().get("DES_INFRACCION").getAsString();

				String localidad = propiedades.getAsJsonObject().get("LOCALIDAD").getAsString();

				String municipio = propiedades.getAsJsonObject().get("MUNICIPIO").getAsString();

				JsonObject geometry = (JsonObject) e.getAsJsonObject().get("geometry");

				String tipo = geometry.get("type").getAsString();


				double[] listaCoords = new double[3];
				JsonArray coordsJson = geometry.getAsJsonArray("coordinates");
				for(int i = 0; i < coordsJson.size(); i ++)
				{
					listaCoords[i] = coordsJson.get(i).getAsDouble();
				}

				Geo geometria = new Geo(tipo, listaCoords);

				Multa multa = new Multa(id, fecha, medioDete, claseVehiculo, tipoServicio, infraccion, descripcion, localidad, municipio, geometria);

				
				heapMayorID.insert(multa);
				datos.enqueue(multa);
				heap1A.insert(multa);
				heap1B.insert(multa);
				

			
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(fecha);
				int diaSemana = gc.get(Calendar.DAY_OF_WEEK);
				int mesNum = gc.get(Calendar.MONTH) + 1;

				LlaveMesDIa llaveMesDia = new LlaveMesDIa(mesNum, diaSemana);

				Queue<Multa> queue1 = hash2A.get(llaveMesDia);
				
				if( queue1 != null)
				{
					queue1.enqueue(multa);
				}
				else
				{
					Queue<Multa> q1 = new Queue<Multa>();
					q1.enqueue(multa);
					hash2A.put(llaveMesDia, q1);
				}
				
				arbol3A.put(fecha, multa);
				
				Llave2B l2b = new Llave2B(medioDete, claseVehiculo, tipoServicio, localidad);
				Queue<Multa> v2b = hash2B.get(l2b);
				if(v2b != null)
				{
					v2b.enqueue(multa);
				}
				else
				{
					Queue<Multa> val2B = new Queue<Multa>();
					val2B.enqueue(multa);
					hash2B.put(l2b, val2B);
				}
				
				double lati = geometria.getLatitud();
				arbol3B.put(lati, multa);
			     
				ArrayList<Multa> v1c = arbol1c.get(fecha2);
				if (v1c != null)
				{
					v1c.add(multa);
				}
				else 
				{
					ArrayList<Multa> val1c = new ArrayList<Multa>();
					val1c.add(multa);
					arbol1c.put(fecha2, val1c);
				}
				
			} //llave for grande
			

		}//llave try
		catch (IOException e) 
		{
			System.out.println("No se encontro el archivo de carga");;
		} catch (ParseException e1) 
		
		{
			// TODO Auto-generated catch block
			System.out.println("La fecha no esta en el formato correcto");;
		}
	}


	public String darInfoCargaDatos() throws noExisteObjetoException, FileNotFoundException
	{
		String msj = "";

		long inicio = System.currentTimeMillis();
		cargarDatos();
		long fin = System.currentTimeMillis();
		msj += "El tiempo total de carga en milis es de " + (fin - inicio) + "ms \n";
		msj += "La cantidad de comparendos es de: " + datos.size() +"\n";
		msj += "El comparendo con el ID mas alto es: " + heapMayorID.max().toString() + "\n";

		return msj;
	}


	public String mayorGravedad(int m)
	{
		String msj = "";

		for(int i = 1; i <= m; i++)
		{
			msj += i + "." + heap1A.delMax().toString()+ "\n"; 
		}

		return msj;
	}

	public String mesDia(int pMes, String dia)
	{
		String msj = "";
		LlaveMesDIa llave = new LlaveMesDIa(pMes, dia);
		Queue<Multa> v = hash2A.get(llave);

		if(v == null ) 
		{
			return "No hay comparendos con los datos indicados";
		}

		else
		{
			int tam = v.size();
			for(int i = 1; i <= tam; i++)
			{
				Multa multa = v.dequeue();
				msj += i +". " + multa.toString() + "\n" ; 
			}
		}
		return msj;

	}
	public String mesDia(int pMes, int pDia)
	{
		String dia;
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

		String msj = "";
		LlaveMesDIa llave = new LlaveMesDIa(pMes, pDia);
		Queue<Multa> v = hash2A.get(llave);

		if(v == null ) return "No hay comparendos con los datos indicados";
		else
		{
			int tam = v.size();
			for(int i = 1; i <= tam; i++)
			{
				Multa m = v.dequeue();
				msj += i +". " + m.toString() + "\n" ; 
			}
		}
		return msj;

	}

	public String fechaHoraLoc(String min, String max, String localidad) throws ParseException
	{
		String msj = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
		Date fMin = sdf.parse(min);
		Date fMax = sdf.parse(max);
	
		
		Queue<Date> q = (Queue) arbol3A.keys(fMin, fMax);
		int tam = q.size();
		if(tam == 0) return "no hay comparendos";
		for(int i = 1; i <= tam; i++ )
		{
			Multa m = arbol3A.get(q.dequeue());
			if(m.getLocalidad().compareToIgnoreCase(localidad) == 0 )
			{
				msj += m.toString() + "\n";
			}
		}
		return msj;
	}

	public String comparendosMasCercanos(int m)
	{
		String msj = "";

		for(int i = 1; i <= m; i++)
		{
			msj += i + "." + heap1B.delMax().toString() + "\n";
		}

		return msj;
	}

	public String reque2B(String medioDete, String claseVehi, String tipoServi, String loc)
	{
		String msj = "";
		
		Llave2B llave = new Llave2B(medioDete.toUpperCase(), claseVehi.toUpperCase(), tipoServi, loc.toUpperCase());
		Queue<Multa> val = hash2B.get(llave);
		if(val == null) return "no se encontraron comparendos con esos valores";
		int tam = val.size();
		for(int i = 1; i <= tam; i++)
		{
			msj += val.dequeue().toString() + "\n";
		}
		
		return msj;
	}

	public String darMultasLatitudMinMax(double min, double max, String vehiculo)
	{
		String msj = "";
		
		Queue<Double> q = (Queue) arbol3B.keys(min, max);
		int tam = q.size();
		for(int i = 1; i <= tam; i++)
		{
			Multa m = arbol3B.get(q.dequeue());
			if(m.getVehiculo().compareToIgnoreCase(vehiculo) == 0)
			{
				msj +=  m.toString() + "\n";
			}
		}
		return msj;
	}

	public Date sumarRestarDiasFecha(Date fecha, int dias){
		
		      Calendar calendar = Calendar.getInstance();
			
		      calendar.setTime(fecha); // Configuramos la fecha que se recibe
			
		      calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de dÃ­as a aÃ±adir, o restar en caso de dÃ­as<0
			
		 
		
		      return calendar.getTime(); // Devuelve el objeto Date con los nuevos dÃ­as aÃ±adidos
			
		 
		
		 }
	public void reque1C(int intervalo)
	{
		
		boolean stop = false;
		AVLTreeST<Date, Integer> arbol = new AVLTreeST<Date, Integer>();
		double cantidadAsteriscosMax = 40;
		double cantidadAsteriscosActual = 1000;
		
		Date min = arbol1c.min();
		
		while(!stop)
		{
			
			
			
			Date max = sumarRestarDiasFecha(min, intervalo-1);
			
			int tamano1 = 0;
			
			for (Date hola: arbol1c.keys(min, max))
			{
			  ArrayList<Multa> alo = arbol1c.get(hola);
			  int tamano = alo.size();
			  tamano1 = tamano1 + tamano;
			}
			
			if (tamano1 > cantidadAsteriscosActual)
		    {
		    	cantidadAsteriscosActual= cantidadAsteriscosActual*1.5;
		   }
			
			arbol.put(min, tamano1);
			
		    
			min = sumarRestarDiasFecha(max, 1);
			
			if (!arbol1c.contains(min))
			{
				stop = true;
			}
		    
		}
		
	  
		
		for (Date f: arbol.keysInOrder())
		{
			
		    double numero =  arbol.get(f);
		    
		    double asteriscos = (numero/cantidadAsteriscosActual)*cantidadAsteriscosMax;
		  int aloAsteriscos = (int) asteriscos;
		    
		    
		    Date fMax = sumarRestarDiasFecha(f, intervalo -1);
		    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		    String fechas = dt1.format(f) + "-" + dt1.format(fMax);
		    String losAsteriscos = "";
		    
		    for (int k = 0; k < aloAsteriscos; k++)
		    {
		    	losAsteriscos = losAsteriscos + "*";
				
		    }
		   
		    System.out.println(fechas + "|" + losAsteriscos );
		}
	}

	public void reque2C()
	{
		
		
		int totalPenalizaciones = 0;
		int diasTardeComparendo = 0;
		int totalComparendos = 0;
		int cantidadAsteriscosMax = 40;
		
		Queue<Multa> lasMultas = new Queue<Multa>();
		
		for (Date f: arbol1c.keysInOrder())
		{
			int cantidadComparendosProcesados = 0;
			int cantidadComparendosEspera = 0;
			double cantidadAsteriscos = 0;
			double cantidadHashtags = 0;
			double techo = 2000; 
			
			String asteriscos = "";
			String hashtags = "";
			
			ArrayList <Multa> multas = arbol1c.get(f);
			totalComparendos = totalComparendos + multas.size();
			
			for (Multa lalaMulta : multas)
			{
				lasMultas.enqueue(lalaMulta);
				
				
			}
			
			
			if(lasMultas.size() <=1500)
			{
				cantidadComparendosProcesados = lasMultas.size();
			}
			
			else 
			{
				cantidadComparendosProcesados = 1500;
				cantidadComparendosEspera = lasMultas.size() - 1500;
			}
			
			
			cantidadAsteriscos = (cantidadComparendosProcesados/techo)*40;
			cantidadHashtags = (cantidadComparendosEspera/techo)*40;
			
			
			int finalAsteriscos = (int) cantidadAsteriscos;
			int finalHashtags = (int) cantidadHashtags;
			
			for (int i = 0; i < finalAsteriscos; i++)
			{
				asteriscos = asteriscos + "*";
			}
			
			for (int i = 0; i < finalHashtags; i++)
			{
				hashtags = hashtags + "#";
			}
			
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println(dt1.format(f) + "|" + asteriscos);
			System.out.println("          " + "|" + hashtags);
			
			for (int i = 0; i < 1500 && !lasMultas.isEmpty(); i++)
			{
				lasMultas.dequeue();
			}
			
			
			
			if (lasMultas.isEmpty() == false)
			{
				
			for (Multa lalalaMulta: lasMultas)
				
			{
				
				
				diasTardeComparendo ++;
				if (lalalaMulta.getDescripcion().contains("SERA INMOVILIZADO") || lalalaMulta.getDescripcion().contains("SERÃ� INMOVILIZADO") )
				{
					
					totalPenalizaciones = totalPenalizaciones +400;
					
				}
				
				else if (lalalaMulta.getDescripcion().contains("LICENCIA DE CONDUCCIÃ“N"))
				{
					totalPenalizaciones = totalPenalizaciones +40;
					
				}
				
				else 
					{totalPenalizaciones = totalPenalizaciones+ 4;
					
				
					}
				
			}
			
			}
			
		   
			
		}
		
		int promedioDias= totalComparendos/diasTardeComparendo;
		System.out.println("");
		System.out.println("El costo total de las penalizaciones es:" + totalPenalizaciones);
		System.out.println("El promedio de dias que debe esperar un comparendo es:" + promedioDias);
		
	}
	
	public void reque3C()
	{

		int totalPenalizaciones = 0;
		int totalComparendos =0;
		int diasTardeComparendos = 0;
		int techo = 2000;
		
		for (Date f: arbol1c.keysInOrder())
		{
			
			double comparendosProcesados = 0;
			double comparendosEnEspera = 0;
			
			ArrayList<Multa> lasMultas = arbol1c.get(f);
			totalComparendos = totalComparendos + lasMultas.size();
			
			for (Multa lalaMulta: lasMultas)
			{
				heap3c.insert(lalaMulta);
				
			}
			
			if (heap3c.size()< 1500)
			{
				comparendosProcesados = heap3c.size();
				
			}
			
			else 
			{
				comparendosProcesados = 1500;
				comparendosEnEspera = heap3c.size() - 1500;
			}
			
			for (int i = 0; i <1500 && !heap3c.isEmpty(); i++)
			{
	               heap3c.delMax();
	               
			}
			
			
			
			double cantidadAsteriscos = (comparendosProcesados/techo)*40;
			double cantidadHashtags = (comparendosEnEspera/techo)*40;
			
			int asteriscos = (int)cantidadAsteriscos;
			int hashtags = (int)cantidadHashtags;
			
			String iAsteriscos = "";
			String iHashtags = "";
			
			for(int i = 0; i < asteriscos; i++)
			{
				iAsteriscos = iAsteriscos + "*";
			}
			
			for (int i = 0; i < hashtags; i++)
			{
				iHashtags = iHashtags + "#";
			}
			
			            
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
	        
			System.out.println(dt1.format(f) + "|" + iAsteriscos);
			System.out.println("          " + "|" + iHashtags);
			
			if (!heap3c.isEmpty())
			{
				java.util.Iterator<Multa> multicas = heap3c.iterator();
				
				while(multicas.hasNext())
				{
					diasTardeComparendos++;
					Multa multica = multicas.next();
					int espera = multica.getNumeroDiasEspera();
					multica.setNumeroDiasEspera(espera++);
					
					if(multica.getDescripcion().contains("SERA INMOVILIZADO") || multica.getDescripcion().contains("SERÃ� INMOVILIZADO"))
					{
						totalPenalizaciones = totalPenalizaciones +400;
					}
					else if (multica.getDescripcion().contains("LICENCIA DE CONDUCCIÃ“N"))
							{
						totalPenalizaciones = totalPenalizaciones +40;
							}
					else {
						totalPenalizaciones = totalPenalizaciones +4;
					}
				}
			}
					
		}
		
		System.out.println("El total de las penalizaciones es:" + totalPenalizaciones);
		int promedioDias= totalComparendos/diasTardeComparendos;
		System.out.println("El numero de dias que debe esperar un comparendo en promedio es:" + promedioDias);
	}

}//llave clase
