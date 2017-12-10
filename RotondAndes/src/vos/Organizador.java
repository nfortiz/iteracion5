package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Organizador {

	@JsonProperty(value = "id")
	private int id;
	@JsonProperty(value = "ingresoVentasPlato")
	private double ingresoVentasPlato;
	@JsonProperty(value = "costoAsociadosPreparacion")
	private double costoAsociadosPreparacion;

	public Organizador(@JsonProperty(value = "id") int id,
			@JsonProperty(value = "ingresoVentasPlato") double ingresoVentasPlato,
			@JsonProperty(value = "costoAsociadosPreparacion") double costoAsociadosPreparacion) {
		this.id = id;
		this.ingresoVentasPlato = ingresoVentasPlato;
		this.costoAsociadosPreparacion = costoAsociadosPreparacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getIngresoVentasPlato() {
		return ingresoVentasPlato;
	}

	public void setIngresoVentasPlato(double ingresoVentasPlato) {
		this.ingresoVentasPlato = ingresoVentasPlato;
	}

	public double getCostoAsociadosPreparacion() {
		return costoAsociadosPreparacion;
	}

	public void setCostoAsociadosPreparacion(double costoAsociadosPreparacion) {
		this.costoAsociadosPreparacion = costoAsociadosPreparacion;
	}

}
