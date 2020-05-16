package model.logic;

import java.awt.BorderLayout;
import java.io.*;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.MapViewOptions;

import edu.princeton.cs.algs4.Queue;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.noExisteObjetoException;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {

	private final static double LATITUD_MIN = 4.597714;
	private final static double LATITUD_MAX = 4.621360;
	private final static double LONGITUD_MIN = -74.094723; 
	private final static double LONGITUD_MAX = -74.062707;

	private GrafoNoDirigido<Integer, String> grafo;
	private GrafoNoDirigido<Integer, String> grafoDeApi;
	private Haversine haversine;
	private Queue<Vertice> qVertice;
	private Queue<Edge> qEdge;
	private Queue<Estacion> qEstacion;


	public Modelo()
	{
		grafo = new GrafoNoDirigido<Integer,String>(228046);
		haversine = new Haversine();
		qVertice = new Queue<Vertice>();
		qEdge = new Queue<Edge>();
		qEstacion = new Queue<Estacion>();

	}

	public void cargarDatos() throws noExisteObjetoException, IOException
	{
		String pathVertex = "./data/vertices_small";
		String pathEdge = "./data/arcos_small";



		BufferedReader brVertex = new BufferedReader(new FileReader(pathVertex));
		BufferedReader brEdge = new BufferedReader(new FileReader(pathEdge));
		String cadenaVertex;
		while((cadenaVertex = brVertex.readLine()) != null )
		{

			String[] partesVertice = cadenaVertex.split(",");
			int idVertex = Integer.parseInt(partesVertice[0]);
			String infoVertex = partesVertice[1] +"/" + partesVertice[2];
			grafo.addVertex(idVertex, infoVertex);

			Vertice v = new Vertice<Integer, String>(idVertex, infoVertex);
			qVertice.enqueue(v);

		} 
		brVertex.close();

		String cadenaEdge;
		while((cadenaEdge = brEdge.readLine()) != null)
		{
			String[] partesArco = cadenaEdge.split(" ");
			if (!partesArco[0].contains("#") && partesArco.length >= 2)
			{
				if(grafo.getInfoVertex(Integer.parseInt(partesArco[0])) != null)
				{
					for (int i = 1; i < partesArco.length ; i++)
					{

						if( grafo.getInfoVertex(Integer.parseInt(partesArco[i])) != null)
						{
							String inicio = (String) grafo.getInfoVertex(Integer.parseInt(partesArco[0]));
							String fin = (String) grafo.getInfoVertex(Integer.parseInt(partesArco[i]));

							String[] partesInicio = inicio.split("/");
							String[] partesFinal = fin.split("/");
							double startLong = Double.parseDouble(partesInicio[0]);
							double startLat = Double.parseDouble(partesInicio[1]);
							double endLong = Double.parseDouble(partesFinal[0]);
							double endLat = Double.parseDouble(partesFinal[1]);
							double peso = Haversine.distance(startLat, startLong, endLat, endLong);
							grafo.addEdge(Integer.parseInt(partesArco[0]), Integer.parseInt(partesArco[i]), peso);

							Edge e = new Edge<Integer>(peso, Integer.parseInt(partesArco[0]), Integer.parseInt(partesArco[i]));
							qEdge.enqueue(e);
						}
					}
				}
			}
		}

		brEdge.close();





		System.out.println("La cantidad de vertices es: " + grafo.cantidadVertices());
		System.out.println("La cantidad de vertices de la lista son "+qVertice.size());
		System.out.println("La cantidad arcos es: " + grafo.cantidadArcos());
		System.out.println("La cantidad de arcos en la lista es " + qEdge.size());
		System.out.println("\n\n\n\n ================================================================");
	}

	public void generarJSon()
	{
		int tamVert = qVertice.size();
		int tamEdge = qEdge.size();

		String pathArcos = "./data/Json_Arcos";
		String pathVertex = "./data/Json_vertices";

		FileWriter wv;
		FileWriter we;

		Vertice[] listaVerts = new Vertice[tamVert];
		Edge[] listaEdges = new Edge[tamEdge];
		try {
			wv = new FileWriter(new File(pathVertex));
			we = new FileWriter(new File(pathArcos));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			for(int i = 0; i < tamVert; i++)
			{
				Vertice v = qVertice.dequeue();
				qVertice.enqueue(v);
				listaVerts[i] = v;
			}
			String verts = gson.toJson(listaVerts);
			wv.write(verts);
			wv.close();

			for(int i = 0; i < tamEdge; i++)
			{
				Edge e = qEdge.dequeue();
				qEdge.enqueue(e);
				listaEdges[i] = e;
			}
			String edges = gson.toJson(listaEdges);
			we.write(edges);
			we.close();

		} 
		catch (FileNotFoundException e) {
			System.out.println("No se encuentra el archivo para pasar el grafo a json");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Problemas escribiendo el archivo");;
		}
	}

	public void leerJson()
	{
		grafoDeApi = new GrafoNoDirigido<>(228046);

		String pathArcos = "./data/Json_Arcos";
		JsonReader lectorArcos;

		String pathVertex = "./data/Json_Vertices"; 
		JsonReader lectorVertices;
		try
		{	
			lectorVertices = new JsonReader(new FileReader(pathVertex));
			JsonElement elementoV =  JsonParser.parseReader(lectorVertices);
			JsonArray listaVertices = elementoV.getAsJsonArray();
			for(JsonElement e : listaVertices)
			{
				JsonObject o = e.getAsJsonObject();
				int key = o.get("key").getAsInt();
				String val = o.get("val").getAsString();
				grafoDeApi.addVertex(key, val);
			}

			lectorArcos = new JsonReader(new FileReader(pathArcos));
			JsonElement elementoE =  JsonParser.parseReader(lectorArcos);
			JsonArray listaEdges = elementoE.getAsJsonArray();
			for(JsonElement e : listaEdges)
			{
				JsonObject o = e.getAsJsonObject();
				String pesoS = o.get("peso").getAsString();

				double peso = Double.parseDouble(pesoS);

				int from = o.get("from").getAsInt();

				int to = o.get("to").getAsInt();


				grafoDeApi.addEdge(from, to, peso);
			}

			System.out.println("Arcos: " + grafoDeApi.cantidadArcos());
			System.out.println("Vertices" + grafoDeApi.cantidadVertices());
		}
		catch(Exception e)
		{
			e.printStackTrace();

		}

	}

	public void cargarEstaciones()
	{
		String path = "./data/estacionpolicia.geojson";
		JsonReader lector;

		try 
		{
			lector = new JsonReader(new FileReader(path));
			JsonElement element = JsonParser.parseReader(lector);
			JsonObject o = element.getAsJsonObject();
			JsonArray arreglo = o.get("features").getAsJsonArray();

			for(JsonElement e : arreglo)
			{
				JsonObject objeto = (JsonObject) e.getAsJsonObject().get("properties");
				double lat = objeto.get("EPOLATITUD").getAsDouble();
				double lon = objeto.get("EPOLONGITU").getAsDouble();
				int id = objeto.get("OBJECTID").getAsInt();
				String telefono = objeto.get("EPOTELEFON").getAsString();
				String nombre = objeto.get("EPONOMBRE").getAsString();
				String dir = objeto.get("EPODIR_SITIO").getAsString();

				Estacion estacion = new Estacion(lat, lon, id, nombre, telefono, dir);
				qEstacion.enqueue(estacion);

			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}


	}

	public void graficar()
	{

		final Mapa mapa = new Mapa("test");

		LatLng vert1 = new LatLng(LATITUD_MIN, LONGITUD_MIN);
		LatLng vert2 = new LatLng(LATITUD_MAX, LONGITUD_MIN);
		LatLng vert3 = new LatLng(LATITUD_MAX, LONGITUD_MAX);
		LatLng vert4 = new LatLng(LATITUD_MIN, LONGITUD_MAX);

		mapa.GenerateLine(false, vert1, vert2, vert3, vert4);
		for(Estacion estacion : qEstacion)
		{
			double lat = estacion.getLat();
			double lon = estacion.getLon();
			mapa.generateMarker(new LatLng(lat, lon));
		}

		for(Vertice v : qVertice)
		{
			double lat = v.getLat();
			double lon = v.getLong();
			if(estaDentro(LATITUD_MIN, LONGITUD_MIN, LATITUD_MAX, LATITUD_MAX, lat, lon))
			{
				mapa.generateArea(new LatLng(lat, lon), 10.0);
			}

		}
		for (Edge e : qEdge)
		{
			int from = (int) e.getFrom();
			int to = (int ) e.getTo();

			String fromS = grafoDeApi.getInfoVertex(from);
			String toS = grafoDeApi.getInfoVertex(to);

			String[] partesFrom = fromS.split("/");
			String[] partesTo = toS.split("/");

			double latIni = Double.parseDouble(partesFrom[1]);
			double lonIni = Double.parseDouble(partesFrom[0]);
			double latFin = Double.parseDouble(partesTo[1]);
			double lonFin = Double.parseDouble(partesTo[0]);

			if(estaDentro(LATITUD_MIN, LATITUD_MIN, LATITUD_MAX, LONGITUD_MAX, latIni, lonIni) && estaDentro(LATITUD_MIN, LATITUD_MIN, LATITUD_MAX, LONGITUD_MAX, latFin, lonFin) )
			{
				LatLng start = new LatLng(latIni, lonIni);
				LatLng end = new LatLng(latFin, lonFin);
				mapa.generateSimplePath(start, end, false);
			}
		}


		System.out.println("Mapa completo");
	}

	private boolean estaDentro(double latMin, double lonMin, double latMax, double lonMax, double latActual, double lonActual)
	{
		return (latActual <= latMax && latActual >= latMin) && (lonActual <= lonMax && lonActual >= lonMin);
	}





}
