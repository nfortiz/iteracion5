package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.ListaProductos;
import vos.Producto;
import vos.Restaurante;

@Path("/productos")
public class ProductosServices {
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

	/**
	 * Metodo que expone servicio REST usando GET que da todos los productos de
	 * un restaurante de la base de datos. <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/restaurantes/{name}/productos
	 * 
	 * @return Json con todos los productos de la base de datos o json con el
	 *         error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		ListaProductos productos;
		try {
			productos = tm.darProductos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}

	@GET
	@Path("{nombre: [a-zA-Z][a-zA-Z]*}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestaurantesPorNombre(@QueryParam("idRestaurante") String idRestaurante,
			@QueryParam("nombre") String nombre) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Restaurante restaurantes;
		try {
			if (nombre == null || nombre.length() == 0)
				throw new Exception("Nombre del restaurante no valido");
			restaurantes = tm.buscarRestaurantesPorNombre(nombre);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurantes).build();
	}

//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response addProducto(Producto producto) {
//		RotondAndesTM tm = new RotondAndesTM(getPath());
//		try {
//			tm.addProducto(producto);
//		} catch (Exception e) {
//			return Response.status(500).entity(doErrorMessage(e)).build();
//		}
//		return Response.status(200).entity(producto).build();
//	}
//
//	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response updateProducto(Producto producto) {
//		RotondAndesTM tm = new RotondAndesTM(getPath());
//		try {
//			tm.updateProducto(producto);
//		} catch (Exception e) {
//			return Response.status(500).entity(doErrorMessage(e)).build();
//		}
//		return Response.status(200).entity(producto).build();
//	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteVideo(Producto producto) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteProducto(producto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(producto).build();
	}

}
