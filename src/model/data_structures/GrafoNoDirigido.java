 package model.data_structures;

import java.util.ArrayList;

import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.DepthFirstSearch;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class GrafoNoDirigido <K,V>
{
	private EdgeWeightedGraph grafo;
	private SeparateChainingHashST<K, Integer> llaveAEntero;
	private SeparateChainingHashST<Integer, K> enteroALlave;
	private SeparateChainingHashST<K, V> llaveAInfoVertex;
	private boolean[] marked;
	private int[] id;
	private int count;
	private int V;
	
	public GrafoNoDirigido(int tamaNo) {
		grafo = new EdgeWeightedGraph(tamaNo);
		llaveAEntero = new SeparateChainingHashST<K, Integer>();
		llaveAInfoVertex = new SeparateChainingHashST<K, V>();
		enteroALlave = new SeparateChainingHashST<Integer, K>();
		id = new int[tamaNo];
		count = 0;
		V = 0;
	
	
	}
	
	public int cantidadVertices()
	{
		return V;
		
	}
	
	public int cantidadArcos()
	{
		return grafo.E();
	}
	
	public void addEdge(K from, K to, double peso) {
		
		if(!llaveAEntero.contains(from)) {
			llaveAEntero.put(from, V);
			enteroALlave.put(V, from);
			V++;
		}
		int fromEntero = llaveAEntero.get(from);
		
		if(!llaveAEntero.contains(to)) {
			llaveAEntero.put(to, V);
			enteroALlave.put(V, to);
			V++;
		}
		int toEntero = llaveAEntero.get(to);

		grafo.addEdge(new Edge(fromEntero, toEntero, peso));
	}
	
	public V getInfoVertex(K key) {
		return llaveAInfoVertex.get(key);
	}
	
	public void addVertex(K key, V info) {
		llaveAInfoVertex.put(key, info);
		if(!llaveAEntero.contains(key)) {
			llaveAEntero.put(key, V);
			enteroALlave.put(V, key);
			V++;
		}
	}
	public double getCostArc(K idVertexIni, K idVertexFin) throws Exception
	{
		double costo = 0;
		boolean si = false;
		int llaveIni = llaveAEntero.get(idVertexIni);
		int llaveFin = llaveAEntero.get(idVertexIni);
		for(Edge myEdge: grafo.edges())
		{
			if (myEdge.other(llaveIni) == llaveFin)
			{
				costo = myEdge.weight();
				si = true;
				break;
			}
		}
		
		if (!si)
		{
			costo = -1;
		}
		return costo;
	}
	
	public void setCostArc (K idVertexIni, K idVertexFin, double cost)
	{
		int llaveIni = llaveAEntero.get(idVertexIni);
		int llaveFin = llaveAEntero.get(idVertexIni);
		
		for(Edge myEdge: grafo.edges())
		{
			if (myEdge.other(llaveIni) == llaveFin)
			{
				myEdge.setWeight(cost);
			}
		}
	}
	public void uncheck()
	{
	    marked = new boolean[grafo.V()];
	}
	public void dfs(K s)
	{
		
		int llave = llaveAEntero.get(s);
		marked[llave] = true;
		
		for (K w: adj(s))
		{
			int llave2 = llaveAEntero.get(w);
			if (!marked[llave2])
			{
				id[llave2] = count;
				dfs(w);
			}
		}
				
	}
	public int cc ()
	{
		
		for (int s  = 0; s < grafo.V(); s++)
		{
			if(!marked[s])
			{
			dfs(enteroALlave.get(s));
			count++;
			}
		}
		return count;
		
	}
	
	public Iterable<K> getCC(K key) {
		CC cc = new CC (grafo);
		ArrayList<K> lista = new ArrayList<K>();
		
		for(int v = 0; v < grafo.V(); v++) {
			if(cc.id(v) == cc.id(llaveAEntero.get(key))) {
				lista.add(enteroALlave.get(v));
			}
		}
		
		return lista;
	}
	
	
	public Iterable<K> adj(K key) {
		Iterable<Edge> a = grafo.adj(llaveAEntero.get(key));
		
		ArrayList<K> resultado = new ArrayList<K>();
		for(Edge e: a) {
			int v = e.other(llaveAEntero.get(key));
			resultado.add(enteroALlave.get(v));
		}
		return resultado;
	}
	
	public void setInfoVertex(K key, V info)
	{
		llaveAInfoVertex.put(key, info);
	}
	
	
	
}
