package vos;

import org.codehaus.jackson.annotate.*;

public class Menu {
	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "precioVenta")
	private Long precioVenta;


	public Menu(@JsonProperty(value = "id") Long id, @JsonProperty(value = "precioVenta") Long precioVenta) {
		super();
		this.id = id;
		this.precioVenta = precioVenta;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Long precioVenta) {
		this.precioVenta = precioVenta;
	}



}
