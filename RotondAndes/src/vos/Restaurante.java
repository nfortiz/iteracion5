package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Restaurante {

	@JsonProperty(value = "nombre")
	private String nombre;

	@JsonProperty(value = "tipoComida")
	private String tipoComida;

	@JsonProperty(value = "paginaWeb")
	private String paginaWeb;

	@JsonProperty(value = "representante")
	private String representante;

	@JsonProperty(value = "zona_id")
	private Long zona_id;

	private List<Producto> productos;

	public Restaurante(@JsonProperty(value = "nombre") String nombre,
			@JsonProperty(value = "tipoComida") String tipoComida, @JsonProperty(value = "paginaWeb") String paginaWeb,
			@JsonProperty(value = "representante") String representante,
			@JsonProperty(value = "zona_id") Long zona_id) {
		super();
		this.nombre = nombre;
		this.tipoComida = tipoComida;
		this.paginaWeb = paginaWeb;
		this.representante = representante;
		this.zona_id = zona_id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoComida() {
		return tipoComida;
	}

	public void setTipoComida(String tipoComida) {
		this.tipoComida = tipoComida;
	}

	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public String getRepresentante() {
		return representante;
	}

	public void setRepresentante(String representante) {
		this.representante = representante;
	}

	public Long getZona_id() {
		return zona_id;
	}

	public void setZona_id(Long zona_id) {
		this.zona_id = zona_id;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

}
