package model.logic;

import java.util.Date;

public class Multa implements Comparable<Multa>
{

	//CONSTANTES
	
	
	//Atributos
	private long id;

	private Date fecha;

	private String medioDete;

	private String vehiculo;

	private String servicio;

	private String infraccion;

	private String descripcion;

	private String localidad;

	private String municipio;

	private Geo geo;
	
	private int numeroDiasEspera;




	public Multa(long id, Date fecha, String medioDete, String vehiculo, String servicio, String infraccion,
			String descripcion, String localidad, String municipio, Geo geo) 
	{
		super();
		this.id = id;
		this.fecha = fecha;
		this.medioDete = medioDete;
		this.vehiculo = vehiculo;
		this.servicio = servicio;
		this.infraccion = infraccion;
		this.descripcion = descripcion;
		this.localidad = localidad;
		this.municipio = municipio;
		this.geo = geo;
		this.numeroDiasEspera = 0;
		
		
		
	}

	public String toString()
	{

		String msj = "El id es "+ id +" la fecha del comparendo "+ fecha 
				+" el medio de detencion es "+ medioDete +
				" el vehiculo es "+ vehiculo +" el tipo de servicio es " + servicio + 
				" la infraccion es " + infraccion + 
				" la descripcion de lo sucedido es " + descripcion + " la localidad es "+
				localidad + " el municipio es " + municipio + " la geografira es "+ geo.toString();

		return msj;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMedioDete() {
		return medioDete;
	}

	public void setMedioDete(String medioDete) {
		this.medioDete = medioDete;
	}

	public String getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getInfraccion() {
		return infraccion;
	}

	public void setInfraccion(String infraccion) {
		this.infraccion = infraccion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	public String getMunicipio()
	{
		return municipio;
	}

	public int getNumeroDiasEspera()
	{
		return numeroDiasEspera;
	}
	
	public void setNumeroDiasEspera(int numeroDiasEspera)
	{
		this.numeroDiasEspera = numeroDiasEspera;
	}
	@Override
	public int compareTo(Multa o)
	{
		int comparacion = 0;
		
		if(id > o.getId())
		{
			comparacion = 1;
		}
		else if( id < o.getId())
		{
			comparacion = -1;
		}
		
		return comparacion;
	}

}
