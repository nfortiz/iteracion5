package vos;

import org.codehaus.jackson.annotate.*;

public class Producto {
	@JsonProperty(value = "nombre")
	private String nombre;

	private String clasificacion;

	@JsonProperty(value = "descripcion")
	private String descripcion;

	@JsonProperty(value = "traduccion")
	private String traduccion;

	private Double costoProducto;

	@JsonProperty(value = "tiempoPreparacion")
	private String tiempoPreparacion;
	
	public Producto(@JsonProperty(value = "nombre") String nombre,			 
			@JsonProperty(value = "descripcion") String descripcionEspaniol,
			@JsonProperty(value = "traduccion") String descripcionIngles,			
			@JsonProperty(value = "tiempoPreparacion") String tiempoPreparacion) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcionEspaniol;
		this.traduccion = descripcionIngles;
		
		this.tiempoPreparacion = tiempoPreparacion;
		
	}
	

	public Producto(@JsonProperty(value = "nombre") String nombre,
			 String clasificacion,
			@JsonProperty(value = "descripcion") String descripcionEspaniol,
			@JsonProperty(value = "traduccion") String descripcionIngles,
			 Double costoProducto,
			@JsonProperty(value = "tiempoPreparacion") String tiempoPreparacion) {
		super();
		this.nombre = nombre;
		this.clasificacion = clasificacion;
		this.descripcion = descripcionEspaniol;
		this.traduccion = descripcionIngles;
		this.costoProducto = costoProducto;
		
		this.tiempoPreparacion = tiempoPreparacion;
		
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}





	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getTraduccion() {
		return traduccion;
	}



	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}



	public String getTiempoPreparacion() {
		return tiempoPreparacion;
	}



	public void setTiempoPreparacion(String tiempoPreparacion) {
		this.tiempoPreparacion = tiempoPreparacion;
	}

	



}
