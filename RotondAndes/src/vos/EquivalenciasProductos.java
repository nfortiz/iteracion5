package vos;

import org.codehaus.jackson.annotate.*;

public class EquivalenciasProductos {
	
	@JsonProperty(value = "producto1")
	private String producto1;

	@JsonProperty(value = "producto2")
	private String producto2;
	
	@JsonProperty(value = "nombreRestaurante")
	private String nombreRestaurante;


	public EquivalenciasProductos(@JsonProperty(value = "producto1") String producto1,
			@JsonProperty(value = "producto2") String producto2, @JsonProperty(value = "nombreRestaurante") String nombreRestaurante) {
		
		this.producto1 = producto1;
		this.producto2 = producto2;
		this.nombreRestaurante= nombreRestaurante;
	}


	public String getNombreRestaurante() {
		return nombreRestaurante;
	}


	public void setNombreRestaurante(String nomRestaurante) {
		this.nombreRestaurante = nomRestaurante;
	}


	public String getProducto1() {
		return producto1;
	}


	public void setProducto1(String producto1) {
		this.producto1 = producto1;
	}


	public String getProducto2() {
		return producto2;
	}


	public void setProducto2(String producto2) {
		this.producto2 = producto2;
	}



	
}
