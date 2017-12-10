package vos;

import org.codehaus.jackson.annotate.*;

public class EquivalenciasIngredientes {
	
	@JsonProperty(value = "ingrediente1")
	private String ingrediente1;

	public String getIngrediente1() {
		return ingrediente1;
	}


	public void setIngrediente1(String ingrediente1) {
		this.ingrediente1 = ingrediente1;
	}


	@JsonProperty(value = "ingrediente2")
	private String ingrediente2;


	public String getIngrediente2() {
		return ingrediente2;
	}


	public void setIngrediente2(String ingrediente2) {
		this.ingrediente2 = ingrediente2;
	}

	@JsonProperty(value = "nombreRestaurante")
	private String nombreRestaurante;
	
	
	public EquivalenciasIngredientes(@JsonProperty(value = "ingrediente1") String ingrediente1,
			@JsonProperty(value = "ingrediente2") String ingrediente2, @JsonProperty(value = "nombreRestaurante") String nombreRestaurante) {
		
		this.ingrediente1 = ingrediente1;
		this.ingrediente2 = ingrediente2;
		this.nombreRestaurante= nombreRestaurante;
		
	}


	public String getNombreRestaurante() {
		return nombreRestaurante;
	}
	
	public void setNombreRestaurante(String nombreRestaurante) {
		this.nombreRestaurante = nombreRestaurante;
	}


	
	
}
