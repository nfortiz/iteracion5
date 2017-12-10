package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class IngredienteEquivalente {
	/**
	 * Id de la Equivalencia
	 */
	@JsonProperty(value = "id")
	private Long id;

	/**
	 * Nombre de la Equivalencia del Ingrediente
	 */
	@JsonProperty(value = "name")
	private String name;

	public IngredienteEquivalente(@JsonProperty(value = "id") Long id, @JsonProperty(value = "name") String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
