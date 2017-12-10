package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class OfrecerProductos {

	@JsonProperty(value = "restaurante_nombre")
	private String restaurante_nombre;

	@JsonProperty(value = "producto_nombre")
	private String producto_nombre;

	

	@JsonProperty(value = "precioVenta")
	private int precioVenta;

	@JsonProperty(value = "disponibilidad")
	private int disponibilidad;

	@JsonProperty(value = "numMax")
	private int numMax;

	public OfrecerProductos(@JsonProperty(value = "restaurante_nombre") String restaurante_nombre,
			@JsonProperty(value = "producto_nombre") String producto_nombre, @JsonProperty(value = "precioVenta") int precioVenta,
			@JsonProperty(value = "disponibilidad") int disponibilidad,
			@JsonProperty(value = "nummax") int numMax) {
		super();
		this.restaurante_nombre = restaurante_nombre;
		this.producto_nombre = producto_nombre;
		this.precioVenta = precioVenta;
		this.disponibilidad = disponibilidad;
		this.numMax = numMax;
	}
	
	public String getRestaurante_nombre() {
		return restaurante_nombre;
	}

	public void setRestaurante_nombre(String restaurante_nombre) {
		this.restaurante_nombre = restaurante_nombre;
	}

	public String getProducto_nombre() {
		return producto_nombre;
	}

	public void setProducto_nombre(String producto_nombre) {
		this.producto_nombre = producto_nombre;
	}

	public int getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(int precioVenta) {
		this.precioVenta = precioVenta;
	}

	public int getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(int disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public int getNumMax() {
		return numMax;
	}

	public void setNummax(int nummax) {
		this.numMax = nummax;
	}
	
}