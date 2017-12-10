package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ListaProductos {

	@JsonProperty(value="productos")
	private List<Producto> productos;
	

	public ListaProductos( @JsonProperty(value="productos")List<Producto> productos){
		this.productos = productos;
	}


	public List<Producto> getProductos() {
		return productos;
	}


	public void setProducto(List<Producto> productos) {
		this.productos = productos;
	}
}
