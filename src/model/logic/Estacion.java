package model.logic;

public class Estacion 
{

	private double lat;
	private double lon;
	private int id;
	private String nombre;
	private String telefono;
	private String dir;
	
	public Estacion(double lat, double lon, int id, String nombre, String telefono, String dir) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.dir = dir;
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDir()
	{
		return dir;
	}
	public void setDir(String pDir)
	{
		dir = pDir;
	}
}
