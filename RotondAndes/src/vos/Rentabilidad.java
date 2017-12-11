package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Rentabilidad {
	/**
	 * Posibles valores: Zona,Producto,Categoria
	 */
	@JsonProperty(value = "tipo")
	private String tipo;
	
	@JsonProperty(value = "productosVendidos")
	private int productosVendidos;
	
	@JsonProperty(value = "valorTotalFacturado")
	private int  valorTotalFacturado;

	public Rentabilidad(@JsonProperty(value = "tipo")String tipo,@JsonProperty(value = "productosVendidos") int productosVendidos, @JsonProperty(value = "valorTotalFacturado")int valorTotalFacturado) {
		this.tipo = tipo;
		this.productosVendidos = productosVendidos;
		this.valorTotalFacturado = valorTotalFacturado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getProductosVendidos() {
		return productosVendidos;
	}

	public void setProductosVendidos(int productosVendidos) {
		this.productosVendidos = productosVendidos;
	}

	public int getValorTotalFacturado() {
		return valorTotalFacturado;
	}

	public void setValorTotalFacturado(int valorTotalFacturado) {
		this.valorTotalFacturado = valorTotalFacturado;
	}
	
	
	
}
