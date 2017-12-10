package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Zona {

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "capacidadPublico")
	private Integer capacidadPublico;

	@JsonProperty(value = "aptoNecesidadesEspeciales")
	private boolean aptoNecesidadesEspeciales;

	@JsonProperty(value = "condicionTecnica")
	private String condicionTecnica;

	@JsonProperty(value = "abierto")
	private boolean abierto;

	// Restaurantes
	private List<Restaurante> restaurantes;

	public Zona(@JsonProperty(value = "id") Long id, @JsonProperty(value = "capacidadPublico") Integer capacidadPublico,
			@JsonProperty(value = "aptoNecesidadesEspeciales") boolean aptoNecesidadesEspeciales,
			@JsonProperty(value = "condicionTecnica") String condicionTecnica,
			@JsonProperty(value = "abierto") boolean abierto) {

		this.id = id;
		this.capacidadPublico = capacidadPublico;
		this.aptoNecesidadesEspeciales = aptoNecesidadesEspeciales;
		this.condicionTecnica = condicionTecnica;
		this.abierto = abierto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCapacidadPublico() {
		return capacidadPublico;
	}

	public void setCapacidadPublico(Integer capacidadPublico) {
		this.capacidadPublico = capacidadPublico;
	}

	public boolean getAptoNecesidadesEspeciales() {
		return aptoNecesidadesEspeciales;
	}

	public void setAptoNecesidadesEspeciales(boolean aptoNecesidadesEspeciales) {
		this.aptoNecesidadesEspeciales = aptoNecesidadesEspeciales;
	}

	public String getCondicionTecnica() {
		return condicionTecnica;
	}

	public void setCondicionTecnica(String condicionTecnica) {
		this.condicionTecnica = condicionTecnica;
	}

	public boolean getAbierto() {
		return abierto;
	}

	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}

	public List<Restaurante> getRestaurantes() {
		return restaurantes;
	}

	public void setRestaurantes(List<Restaurante> restaurantes) {
		this.restaurantes = restaurantes;
	}

}
