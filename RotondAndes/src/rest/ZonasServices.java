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
import vos.Restaurante;
import vos.Zona;

@Path("zonas")
public class ZonasServices {

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
	 * Metodo que expone servicio REST usando GET que da todos las zonas de la
	 * base de datos. <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/(...)
	 * 
	 * @return Json con todos las zonas de la base de datos o json con el error
	 *         que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getZonas() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Zona> zonas;
		try {
			zonas = tm.darZonas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}

	@GET
	@Path("{restaurantes}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRestaurantePorZona(@QueryParam("zona")Long idZona){
		
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Restaurante> restaurantes;
		try {
			restaurantes = tm.buscarRestaurantePorZonaId(idZona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(restaurantes).build();
	}
	
	/**
	 * Metodo que expone servicio REST usando POST que agrega la zona que recibe
	 * en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RontondAndes/rest/(...)
	 * 
	 * @param zona
	 *            - zona a agregar
	 * @return Json con la zona que agrego o Json con el error que se produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addZona(Zona zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega las zonas que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/zonas/varios
	 * 
	 * @param zonas
	 *            - zonas a agregar.
	 * @return Json con las zona que agrego o Json con el error que se produjo
	 */
	@POST
	@Path("/varios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addZona(List<Zona> zonas) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addZonas(zonas);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zonas).build();
	}

	/**
	 * Metodo que expone servicio REST usando PUT que actualiza la zona que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/zonas
	 * 
	 * @param zona
	 *            - zona a actualizar.
	 * @return Json con la zona que actualizo o Json con el error que se produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateZona(Zona zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina la zona que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/zonas
	 * 
	 * @param zona
	 *            - zona a eliminar.
	 * @return Json con la zona que elimino o Json con el error que se produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteZona(Zona zona) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteZona(zona);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(zona).build();
	}

}
