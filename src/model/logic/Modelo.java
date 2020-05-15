package model.logic;

import java.io.*;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import edu.princeton.cs.algs4.Queue;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.noExisteObjetoException;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {

	private GrafoNoDirigido<Integer, String> grafo;
	private Haversine haversine;
	private Queue<Vertice> qVertice;
	private Queue<Edge> qEdge;


	public Modelo()
	{
		grafo = new GrafoNoDirigido<Integer,String>(228046);
		haversine = new Haversine();
		qVertice = new Queue<Vertice>();
		qEdge = new Queue<Edge>();

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
				listaVerts[i] = v;
			}
			String verts = gson.toJson(listaVerts);
			wv.write(verts);
			wv.close();
			
			for(int i = 0; i < tamEdge; i++)
			{
				Edge e = qEdge.dequeue();
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
		String pathArcos = "./data/Json_Arcos";
		String pathVertex = "./data/Json_Vertices";
		JsonReader lectorArcos; 
		JsonReader lectorVertices;
		try
		{
			lectorArcos = new JsonReader(new FileReader(pathArcos));
			
			
			
			
			
			lectorVertices = new JsonReader(new FileReader(pathVertex));
			JsonElement elemento =  JsonParser.parseReader(lectorVertices);
			JsonArray lista = elemento.getAsJsonArray();
			for(JsonElement e : lista)
			{
				JsonObject o = e.getAsJsonObject();
				int key = o.get("key").getAsInt();
				String val = o.get("val").getAsString();
			}
			
		}
		catch(Exception e)
		{
			System.out.println("No puedo leer el json :(");
			
		}

	}
	

}
