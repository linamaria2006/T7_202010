package model.logic;

import model.data_structures.Queue;

public class Llave2B 
{
	
	
	private String deteccion;
	
	private String vehiculo;
	
	private String servicio;
	
	private String localidad;

	public Llave2B(String deteccion, String vehiculo, String servicio, String localidad)
	{
		
		this.deteccion = deteccion;
		this.vehiculo = vehiculo;
		this.servicio = servicio;
		this.localidad = localidad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deteccion == null) ? 0 : deteccion.hashCode());
		result = prime * result + ((localidad == null) ? 0 : localidad.hashCode());
		result = prime * result + ((servicio == null) ? 0 : servicio.hashCode());
		result = prime * result + ((vehiculo == null) ? 0 : vehiculo.hashCode());
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
		Llave2B other = (Llave2B) obj;
		if (deteccion == null) {
			if (other.deteccion != null)
				return false;
		} else if (!deteccion.equals(other.deteccion))
			return false;
		if (localidad == null) {
			if (other.localidad != null)
				return false;
		} else if (!localidad.equals(other.localidad))
			return false;
		if (servicio == null) {
			if (other.servicio != null)
				return false;
		} else if (!servicio.equals(other.servicio))
			return false;
		if (vehiculo == null) {
			if (other.vehiculo != null)
				return false;
		} else if (!vehiculo.equals(other.vehiculo))
			return false;
		return true;
	}
	
	

}
