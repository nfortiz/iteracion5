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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Asistente;
import vos.EquivalenciasProductos;
import vos.Producto;
import vos.Restaurante;

@Path("restaurantes")
public class RestaurantesServices {

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
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestaurantes() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Restaurante> restaurantes;
		try {
			restaurantes = tm.darRestaurantes();
		} catch (Exception e) {
			System.out.println("Se fue por aqui");
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurantes).build();
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
	@Path("nombre")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRestaurantesPorNombre(@QueryParam("nombre") String nombre) {
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
	/**
	 * Entrega la cantidad de comensales 
	 * @param nombre Nombre del restaurante
	 * @param fecha1 Rango de fecha 1
	 * @param fecha2 Rango de fecha 2
	 * @return
	 */
	@GET
	@Path("comensales")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getComensalesRestaurante(@QueryParam("nombre") String nombre, @QueryParam("fecha1") String fecha1, @QueryParam("fecha2") String fecha2, @QueryParam("ordenar") String ordenar) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Asistente> asistentes;
		//System.out.println(nombre+":"+fecha1+":"+fecha2+":"+ordenar);
		try {
			if (nombre == null || nombre.length() == 0||fecha1==null||fecha1.length()==0||fecha2==null||fecha2.length()==0)
				throw new Exception("Parametros no validos");
			asistentes = tm.buscarClientesRangoFecha(nombre, fecha1, fecha2, ordenar);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		return Response.status(200).entity(asistentes).build();
	}
	/**
	 * Entrega la cantidad de comensales 
	 * @param nombre Nombre del restaurante
	 * @param fecha1 Rango de fecha 1
	 * @param fecha2 Rango de fecha 2
	 * @return
	 */
	@GET
	@Path("comensales2")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getComensalesRestaurante2(@QueryParam("nombre") String nombre, @QueryParam("fecha1") String fecha1, @QueryParam("fecha2") String fecha2, @QueryParam("ordenar") String ordenar) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Asistente> asistentes;
		try {
			if (nombre == null || nombre.length() == 0||fecha1==null||fecha1.length()==0||fecha2==null||fecha2.length()==0)
				throw new Exception("Parametros no validos");
			asistentes = tm.buscarClientesRangoFecha2(nombre, fecha1, fecha2, ordenar);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		return Response.status(200).entity(asistentes).build();
	}

	@GET
	@Path("nocomensales")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getNoComensalesRestaurante(@QueryParam("nombre") String nombre, @QueryParam("fecha1") String fecha1, @QueryParam("fecha2") String fecha2, @QueryParam("ordenar") String ordenar) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Asistente> asistentes;
		//System.out.println(nombre+":"+fecha1+":"+fecha2+":"+ordenar);
		try {
			if (nombre == null || nombre.length() == 0||fecha1==null||fecha1.length()==0||fecha2==null||fecha2.length()==0)
				throw new Exception("Parametros no validos");
			asistentes = tm.buscarNoClientesRangoFecha(nombre, fecha1, fecha2, ordenar);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		return Response.status(200).entity(asistentes).build();
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
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addRestaurante(restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurante).build();
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
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addRestaurantes(restaurantes);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurantes).build();
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
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateRestaurante(restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurante).build();
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
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteRestaurante(restaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurante).build();
	}

	////////////////////////////////////
	// ----------Productos-------------//
	////////////////////////////////////
	@GET
	@Path("{idRestaurante}/productos")
	public Response getProductos(@QueryParam("idRestaurante") String idRestaurante) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		Restaurante restaurantes;
		List<Producto> productos = new ArrayList<>();
		try {
			if (idRestaurante == null || idRestaurante.length() == 0)
				throw new Exception("Nombre del restaurante no valido");
			restaurantes = tm.buscarRestaurantesPorNombre(idRestaurante);

			productos = tm.darProductosPorRestaurante(idRestaurante);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}

	////////////////////////////////////
	// ----------Menu-------------//
	////////////////////////////////////
	// @Path("{idRestaurante}/menu")
	// public Response getMenusService(@QueryParam("idRestaurante") String
	// idRestaurante ){
	// RotondAndesTM tm = new RotondAndesTM(getPath());
	// Restaurante restaurantes;
	// List<Producto>productos= new ArrayList<>();
	// try {
	// if (idRestaurante == null || idRestaurante.length() == 0)
	// throw new Exception("Nombre del restaurante no valido");
	// restaurantes = tm.buscarRestaurantesPorNombre(idRestaurante);
	//
	// productos=tm.darProductosPorRestaurante(idRestaurante);
	// } catch (Exception e) {
	// return Response.status(500).entity(doErrorMessage(e)).build();
	// }
	// return Response.status(200).entity(MenuServices).build();
	// }
	
	

	
	
	
}
