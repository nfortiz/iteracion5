package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductoMenosVendido {
	
	
	@JsonProperty(value = "nombre")
	private String nombre;
	
	@JsonProperty(value = "dia")
	private String dia;
	
	@JsonProperty(value = "minimo")
	private int minimo;
	
	public ProductoMenosVendido(@JsonProperty(value = "nombre") String nombre,
			@JsonProperty(value = "dia") String dia,@JsonProperty(value = "minimo") int minimo) {
		
		this.nombre = nombre;
		this.dia = dia;
		this.minimo = minimo;

}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public int getminimo() {
		return minimo;
	}

	public void setminimo(int minimo) {
		this.minimo = minimo;
	}

}
