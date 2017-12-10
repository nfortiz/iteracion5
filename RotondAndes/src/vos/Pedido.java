package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Pedido {

	public enum TipoPedido{
		PEDIDO_MENU,  
		PEDIDO_PRODUCTO, 
		PEDIDO_EQUIVALENCIAPRODUCTO
	}
	public static class ItemPedido{
		public TipoPedido tipos;
		
		@JsonProperty(value = "nombreRestaurante")
		private String nombreRestaurante;
		
		@JsonProperty(value = "tipoPedido")
		private String tipoPedido;
		
		@JsonProperty(value = "idItem")
		private String idItem;

		
		public ItemPedido(@JsonProperty(value = "nombreRestaurante") String nombreRestaurante, @JsonProperty(value = "tipoPedido") String tipoPedido, @JsonProperty(value = "idItem") String idItem) {
			super();
			this.nombreRestaurante = nombreRestaurante;
			this.tipoPedido = tipoPedido;
			this.idItem = idItem;
		}
        
		public String getNombreRestaurante() {
			return nombreRestaurante;
		}

		public void setNombreRestaurante(String nombreRestaurante) {
			this.nombreRestaurante = nombreRestaurante;
		}

		public String getTipoPedido() {
			return tipoPedido;
		}

		public void setTipoPedido(String tipoPedido) {
			this.tipoPedido = tipoPedido;
		}

		public String getIdItem() {
			return idItem;
		}

		public void setIdItem(String idItem) {
			this.idItem = idItem;
		}
	}


	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "fecha")
	private String fecha;

	@JsonProperty(value = "idCliente")
	private Long idCliente;
	
	@JsonProperty(value = "servido")
	private boolean servido;
	
	@JsonProperty(value = "items")
	private List<ItemPedido> items;

	
	public Pedido(@JsonProperty(value = "id") Long id, 	@JsonProperty(value = "fecha") String fecha, @JsonProperty(value = "idCliente") Long idCliente, @JsonProperty(value = "servido")boolean servido,@JsonProperty(value = "items")List<ItemPedido> items) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.idCliente = idCliente;
		this.servido = servido;
		this.items = items;
	}



	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public List<ItemPedido> getItems() {
		return items;
	}

	public void setItems(List<ItemPedido> items) {
		this.items = items;
	}



	public boolean isServido() {
		return servido;
	}



	public void setServido(boolean servido) {
		this.servido = servido;
	}


}
