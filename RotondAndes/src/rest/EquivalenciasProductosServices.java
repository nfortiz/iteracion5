
	
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
import vos.EquivalenciasIngredientes;
import vos.EquivalenciasProductos;
import vos.Producto;
	import vos.Restaurante;

	@Path("/equivalenciasproductos")
	public class EquivalenciasProductosServices {
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
		public Response getEquivalenciasProductos() {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			List<EquivalenciasProductos> equivalenciasProductos;
			try {
				equivalenciasProductos = tm.darEquivalenciasProductos();
			} catch (Exception e) {
				
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(equivalenciasProductos).build();
		}

		@GET
		@Path("{nombreRestaurante}")
		@Produces({ MediaType.APPLICATION_JSON })
		public Response getEquivalenciasProductosPorIdRestaurante(@PathParam("nombreRestaurante") String idRestaurante) {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			ArrayList<EquivalenciasProductos> equivalencias;
			try {
				if (idRestaurante == null || idRestaurante.length() == 0)
					throw new Exception("Id del restaurante no valido");
				else if(idRestaurante.contains("-"))
				{
					String nuevoId= idRestaurante.replace("-", " ");
					equivalencias = tm.buscarEquivalenciasProductosPorIdRestaurante(nuevoId);
					
				}
				else {
				equivalencias = tm.buscarEquivalenciasProductosPorIdRestaurante(idRestaurante);}
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(equivalencias).build();
		}

		@POST
		@Path("{nombreRestaurante}")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response addEquivalenciaProducto(@PathParam("nombreRestaurante") String idRestaurante,EquivalenciasProductos eproducto) {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			try {
				if (idRestaurante == null || idRestaurante.length() == 0)
					throw new Exception("Id del restaurante no valido");
				if(idRestaurante.contains("-"))
				{
					idRestaurante = idRestaurante.replace("-", " ");
				}
				if(!idRestaurante.equals(eproducto.getNombreRestaurante()))
				{
					throw new Exception("No puede agregar una equivalencia a otro restaurante");
				}
				tm.addEquivalenciasProductos(eproducto);
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(eproducto).build();
		}

	

		@DELETE
		@Path("{nombreRestaurante}")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response deleteEquivalenciaProductoPorEquivalencia(@PathParam("nombreRestaurante") String idRestaurante,EquivalenciasProductos eProductos) {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			
			try {
				if (idRestaurante == null || idRestaurante.length() == 0)
					throw new Exception("Id del restaurante no valido");
				if(idRestaurante.contains("-"))
				{
					idRestaurante = idRestaurante.replace("-", " ");
				}
				if(!idRestaurante.equals(eProductos.getNombreRestaurante()))
				{
					throw new Exception("No puede agregar eliminar una equivalencia a otro restaurante");
				}
				tm.deleteEquivalenciasProductoPorEquivalencia(eProductos);
			
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(eProductos).build();
		}

	}



