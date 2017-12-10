package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Asistente {

	@JsonProperty(value = "id")
	private Integer id;

	public int getId() {
		return id;
	}

	public void setId(@JsonProperty(value = "id") int id) {
		this.id = id;
	}

	public Asistente(int id) {
		this.id = id;
	}

}
