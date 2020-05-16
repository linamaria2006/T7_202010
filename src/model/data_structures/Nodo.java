package model.data_structures;

import java.util.Iterator;

public class Nodo<K> implements Iterator<Nodo<K>>
{

	private K generico;
	
	private Nodo<K> siguiente;
	
	private Nodo<K> anterior;
	
	public Nodo(K pGenerico)
	
	{
		siguiente = null;
		
		anterior = null;
		
		generico = pGenerico;
				
	}
	
	
	public Nodo<K> darSiguiente()
	{
		return  siguiente;
	}
	
	
	public Nodo<K> darAnterior()
	{
		return  anterior;
	}
	
	public void cambiarAnterior(Nodo<K> pAnterior)
	{
		anterior = pAnterior;
	}
	
	public void cambiarSiguiente(Nodo<K> pSiguiente )
	{
		siguiente = pSiguiente;
	}
	
	public void desconectarSiguiente()
	{
		siguiente = null;
	}
	
	public void desconectarAnterior()
	{
		anterior = null;
	}
	
	public K darGenerico()
	{
		return generico;
	}


	public boolean hasNext() {
		// TODO Auto-generated method stub
		return this.siguiente != null;
	}


	public Nodo<K> next() {
		// TODO Auto-generated method stub
		return this.siguiente;
	}
	

}
