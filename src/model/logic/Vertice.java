package model.logic;

public class Vertice<K, V>
{
	private K key;
	private V val;
	private double lat;
	private double longi;
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getVal() {
		return val;
	}
	public void setVal(V val) {
		this.val = val;
	}
	public Vertice(K key, V val) {
		super();
		this.key = key;
		this.val = val;
		
		String[] partes = val.toString().split("/");
		longi = Double.parseDouble(partes[0]);
		lat = Double.parseDouble(partes[1]);
	}
	public double getLat()
	{
		return lat;
	}
	public void setLat(double pLat)
	{
		lat = pLat;
	}
	public double getLong()
	{
		return longi;
	}
	public void setLong(double pLong)
	{
		longi = pLong;
	}

}
