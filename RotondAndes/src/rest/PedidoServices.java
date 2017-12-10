package rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Pedido;
import vos.Registrado;
import vos.Restaurante;

@Path("pedidos")
public class PedidoServices {

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la
	 * conexion actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el
	 * deploy actual dentro del servidor.
	 * 
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e) {
		return "{ \"ERROR\": \"" + e.getMessage() + "\"}";
	}


	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPedidos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Pedido> pedidos;
		try {
			pedidos = tm.darPedidos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidos).build();
	}

	@GET
	@Path("{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response gePedidoPorId(@QueryParam("id") Long id) {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Pedido pedido = tm.darPedidoPorId(id);
			return Response.status(200).entity(pedido).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPedidoTotal(Pedido pedido) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addPedidoTotal(pedido);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return null;
	}
	
	@POST
	@Path("/varios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPedidosVarios(List<Pedido> pedidos) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addPedidosVarios(pedidos);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedidos).build();
	}
	
	
	
	@PUT
	@Path("{servido}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void updatePedidoServido(@QueryParam("pedido")Long idPedido) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updatePedidoServido(idPedido);
		} catch (Exception e) {
			System.out.println("ERROR: Confirmar llegada pedido.");
			e.printStackTrace();
		}
	}
	
	@PUT
	@Path("actServidos/{listaServidos}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void updatePedidosServidos(@PathParam("listaServidos")String listaS) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			List<Long> idsPedidos = new ArrayList<>();
			String[] numerosDivididos = listaS.split("-");
			for(int i=0; i<numerosDivididos.length;i++)
			{
				idsPedidos.add(Long.valueOf(numerosDivididos[i]));
			}
			
			tm.updatePedidosServidos(idsPedidos);
		} catch (Exception e) {
			System.out.println("ERROR: Confirmar llegada pedidos.");
			e.printStackTrace();
		}
	}

	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePedido(Pedido pedido) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updatePedido(pedido);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedido).build();
	}
	
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePedido(Pedido pedido) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deletePedido(pedido);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(pedido).build();
	}
}