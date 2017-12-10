	package rest;
	import java.util.ArrayList;
	import java.util.List;
	import javax.servlet.ServletContext;
	import javax.ws.rs.Consumes;
	import javax.ws.rs.DELETE;
	import javax.ws.rs.GET;
	import javax.ws.rs.POST;
	import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
	import javax.ws.rs.QueryParam;
	import javax.ws.rs.core.Context;
	import javax.ws.rs.core.MediaType;
	import javax.ws.rs.core.Response;
	import tm.RotondAndesTM;
    import vos.EquivalenciasIngredientes;



	@Path("/equivalenciasingredientes")
	public class EquivalenciasIngredientesServices {
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
		public Response getEquivalenciasIngredientes() {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			List<EquivalenciasIngredientes> equivalenciasIngredientes;
			try {
				equivalenciasIngredientes = tm.darEquivalenciasIngredientes();
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(equivalenciasIngredientes).build();
		}

		@GET
		@Path("{nombreRestaurante}")
		@Produces({ MediaType.APPLICATION_JSON })
		public Response getEquivalenciasIngredientePorIdRestaurante(@PathParam("nombreRestaurante") String idRestaurante) {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			ArrayList<EquivalenciasIngredientes> equivalencias;
			try {
				if (idRestaurante == null || idRestaurante.length() == 0)
					throw new Exception("Id del ingrediente no valido");
				else if(idRestaurante.contains("-"))
				{
					String nuevoId= idRestaurante.replace("-", " ");
					equivalencias = tm.buscarEquivalenciasIngredientesPorIdRestaurante(nuevoId);
					
				}
				else {
				equivalencias = tm.buscarEquivalenciasIngredientesPorIdRestaurante(idRestaurante);}
				
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(equivalencias).build();
		}

		@POST
		@Path("{nombreRestaurante}")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response addEquivalenciaIngrediente(@PathParam("nombreRestaurante") String idRestaurante,EquivalenciasIngredientes eingrediente) {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			try {
				if (idRestaurante == null || idRestaurante.length() == 0)
					throw new Exception("Id del ingrediente no valido");
				if(idRestaurante.contains("-"))
				{
					idRestaurante= idRestaurante.replace("-", " ");
				}
				if(!idRestaurante.equals(eingrediente.getNombreRestaurante()))
				{
					throw new Exception("No puede agregar una equivalencia a otro restaurante");
				}
				
				tm.addEquivalenciasIngredientes(eingrediente);
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(eingrediente).build();
		}

	

		@DELETE
		@Path("{nombreRestaurante}")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response deleteEquivalenciaIngredientePorEquivalencia(@PathParam("nombreRestaurante") String idRestaurante,EquivalenciasIngredientes eingrediente) {
			RotondAndesTM tm = new RotondAndesTM(getPath());
			try {
				if (idRestaurante == null || idRestaurante.length() == 0)
					throw new Exception("Id del ingrediente no valido");
				if(idRestaurante.contains("-"))
				{
					idRestaurante= idRestaurante.replace("-", " ");
				}
				if(!idRestaurante.equals(eingrediente.getNombreRestaurante()))
				{
					throw new Exception("No puede eliminar una equivalencia a otro restaurante");
				}
				
			
			
			tm.deleteEquivalenciasIngredientePorEquivalencia(eingrediente);
			}
			 catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(eingrediente).build();
		}

}


