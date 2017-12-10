package rest;

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
import tm.RotondAndesTM;
import vos.Asistente;
import vos.Organizador;
import vos.Producto;
import vos.ProductoMasVendido;
import vos.ProductoMenosVendido;
import vos.Restaurante;
import vos.RestauranteMF;
import vos.RestauranteMnF;
import vos.Restaurante;

@Path("RotondAndes")
public class RotondAndesServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los restaurantes
	 * de la base de datos. <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/restaurantes
	 * 
	 * @return Json con todos los restaurantes de la base de datos o json con el
	 *         error que se produjo
	 */
	@GET
	@Path("{restaurantes}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestaurantes() {
		// TODO: Terminar metod
		return null;
	}

	/**
	 * Metodo que expone servicio REST usando GET que busca el restaurante con
	 * el nombre que entra como parametro <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/restaurantes/nombre/nombre?nombre=<<nombre>>"
	 * para la busqueda"
	 * 
	 * @param nombre
	 *            - Nombre del restaurante a buscar que entra en la URL como
	 *            parametro
	 * @return Json con el restaurante encontrados con el nombre que entra como
	 *         parametro o json con el error que se produjo
	 */
	@GET
	@Path("/restaurantes/{nombre}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestaurantesPorNombre(@QueryParam("nombre") String nombre) {
		// TODO: Terminar metod
		return null;

	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega el resturante que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RontondAndes/rest/restaurantes/restaurantes
	 * 
	 * @param restaurante
	 *            - restaurante a agregar
	 * @return Json con el restaurante que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurante(Restaurante restaurante) {
		// TODO: Terminar metod
		return null;

	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega los restaurantes
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/restaurantes/varios
	 * 
	 * @param restaurantes
	 *            - restaurantes a agregar.
	 * @return Json con el restaurante que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Path("/varios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurante(List<Restaurante> restaurantes) {
		// TODO: Terminar metod
		return null;

	}

	/**
	 * Metodo que expone servicio REST usando PUT que actualiza el restaurante
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/restaurantes
	 * 
	 * @param restaurante
	 *            - restaurante a actualizar.
	 * @return Json con el restaurante que actualizo o Json con el error que se
	 *         produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRestaurante(Restaurante restaurante) {
		// TODO: Terminar metod
		return null;
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina el restaurante
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/restaurantes
	 * 
	 * @param restaurante
	 *            - restaurante a eliminar.
	 * @return Json con el restaurante que elimino o Json con el error que se
	 *         produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRestaurante(Restaurante restaurante) {
		// TODO: Terminar metod
		return null;
	}
	
	////////////////////////////////////////////////////////
	
	@GET
	@Path("/restauranteMasFrecuentado")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestauranteMasFrecuentado() {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<RestauranteMF> restaurantes;
		try {
			restaurantes = tm.darRestauranteMasFrecuentados();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurantes).build();
	}
	
	@GET
	@Path("/restauranteMenosFrecuentado")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestauranteMenosFrecuentado() {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<RestauranteMnF> restaurantes;
		try {
			restaurantes = tm.darRestauranteMenosFrecuentados();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurantes).build();
	}
	
	@GET
	@Path("/productoMasVendido")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductoMasVendido() {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ProductoMasVendido> productos;
		try {
			productos = tm.darProductoMasConsumido();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}

	@GET
	@Path("/productoMenosVendido")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProductoMenosVendido() {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<ProductoMenosVendido> productos;
		try {
			productos = tm.darProductoMenosConsumido();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}
	
	@GET
	@Path("/buenosClientes")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getBuenosClientes() {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Asistente> bClientes;
		try {
			bClientes = tm.darBuenosClientes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(bClientes).build();
	}
	@DELETE
	@Path("/eliminarRestaurante/{nombreRestaurante}")
	@Produces({ MediaType.APPLICATION_JSON })
	public void eliminarRestaurante(@PathParam("nombreRestaurante") String nombreRestaurante) {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		
		try {
			tm.eliminarRestaurante(nombreRestaurante);
		} catch (Exception e) {
			e.getMessage();
		}
		
	}



}
