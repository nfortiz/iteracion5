package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProductoMasVendido {
	
	
	@JsonProperty(value = "nombre")
	private String nombre;
	
	@JsonProperty(value = "dia")
	private String dia;
	
	@JsonProperty(value = "maximo")
	private int maximo;
	
	public ProductoMasVendido(@JsonProperty(value = "nombre") String nombre,
			@JsonProperty(value = "dia") String dia,@JsonProperty(value = "maximo") int maximo) {
		
		this.nombre = nombre;
		this.dia = dia;
		this.maximo = maximo;

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

	public int getMaximo() {
		return maximo;
	}

	public void setMaximo(int maximo) {
		this.maximo = maximo;
	}

}
