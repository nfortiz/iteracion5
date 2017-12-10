package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Reserva {

	@JsonProperty(value = "id")
	private int id;
	@JsonProperty(value = "fecha")
	private Date fecha;
	@JsonProperty(value = "asistentes")
	private int asistentes;

	public Reserva(@JsonProperty(value = "id") int id, @JsonProperty(value = "fecha") Date fecha,
			@JsonProperty(value = "asistentes") int asistentes) {
		this.id = id;
		this.fecha = fecha;
		this.asistentes = asistentes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getAsistentes() {
		return asistentes;
	}

	public void setAsistentes(int asistentes) {
		this.asistentes = asistentes;
	}

}
