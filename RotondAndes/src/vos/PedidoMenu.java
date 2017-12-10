package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class PedidoMenu {
	@JsonProperty(value = "pedido_id")
	private Long pedido_id;

	@JsonProperty(value = "menu_id")
	private Long menu_id;

	public PedidoMenu(@JsonProperty(value = "pedido_id")Long pedido_id,@JsonProperty(value = "menu_id") Long menu_id) {
		super();
		this.pedido_id = pedido_id;
		this.menu_id = menu_id;
	}

	public Long getPedido_id() {
		return pedido_id;
	}

	public void setPedido_id(Long pedido_id) {
		this.pedido_id = pedido_id;
	}

	public Long getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Long menu_id) {
		this.menu_id = menu_id;
	}
	
}
