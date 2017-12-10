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
import vos.Asistente;
import vos.Organizador;

@Path("asistentes")
public class AsistentesServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los asistentes de
	 * la base de datos. <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/(...)
	 * 
	 * @return Json con todos los asistentes de la base de datos o json con el
	 *         error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAsistentes() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Asistente> asistentes;
		try {
			asistentes = tm.darAsistentes();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(asistentes).build();
	}

	@GET
	@Path("{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAsistentePorId(@QueryParam("id") int id) {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Asistente asistente = tm.darAsistentePorId(id);
			return Response.status(200).entity(asistente).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega el asistente que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RontondAndes/rest/(...)
	 * 
	 * @param asistente
	 *            - asistente a agregar
	 * @return Json con el asistente que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAsistente(Asistente asistente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addAsistente(asistente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(asistente).build();
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega los asistentes que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/asistentes/varios
	 * 
	 * @param asistentes
	 *            - asistentes a agregar.
	 * @return Json con el asistente que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Path("/varios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAsistente(List<Asistente> asistentes) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addAsistentes(asistentes);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(asistentes).build();
	}

	/**
	 * Metodo que expone servicio REST usando PUT que actualiza el asistente que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/asistentes
	 * 
	 * @param asistente
	 *            - asistente a actualizar.
	 * @return Json con el asistente que actualizo o Json con el error que se
	 *         produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAsistente(Asistente asistente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateAsistente(asistente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(asistente).build();
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina el asistente
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/asistentes
	 * 
	 * @param asistente
	 *            - asistente a eliminar.
	 * @return Json con el asistente que elimino o Json con el error que se
	 *         produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAsistente(Asistente asistente) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteAsistente(asistente);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(asistente).build();
	}

}
