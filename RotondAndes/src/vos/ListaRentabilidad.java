package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ListaRentabilidad {
	@JsonProperty(value="rentabilidades")
	private List<Rentabilidad> rentabilidades;
	

	public ListaRentabilidad( @JsonProperty(value="rentabilidades")List<Rentabilidad> rentabilidades){
		this.rentabilidades = rentabilidades;
	}


	public List<Rentabilidad> getRentabilidades() {
		return rentabilidades;
	}


	public void setRentabilidades(List<Rentabilidad> rentabilidades) {
		this.rentabilidades = rentabilidades;
	}


}
