package vos;

import org.codehaus.jackson.annotate.*;

public class Ingrediente {
	/**
	 * Nombre unico
	 */
	@JsonProperty(value = "nombre")
	private String nombre;

	@JsonProperty(value = "descripcionEspaniol")
	private String descripcionEspaniol;

	@JsonProperty(value = "descripcionIngles")
	private String descripcionIngles;

	public Ingrediente(@JsonProperty(value = "nombre") String nombre,
			@JsonProperty(value = "descripcionEspaniol") String descripcionEspaniol,
			@JsonProperty(value = "descripcionIngles") String descripcionIngles) {
		super();
		this.nombre = nombre;
		this.descripcionEspaniol = descripcionEspaniol;
		this.descripcionIngles = descripcionIngles;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcionEspaniol() {
		return descripcionEspaniol;
	}

	public void setDescripcionEspaniol(String descripcionEspaniol) {
		this.descripcionEspaniol = descripcionEspaniol;
	}

	public String getDescripcionIngles() {
		return descripcionIngles;
	}

	public void setDescripcionIngles(String descripcionIngles) {
		this.descripcionIngles = descripcionIngles;
	}

}
