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

public class OrganizadorServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los organizadores
	 * de la base de datos. <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/(...)
	 * 
	 * @return Json con todos los organizadores de la base de datos o json con
	 *         el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOrganizadores() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Organizador> organizadores;
		try {
			organizadores = tm.darOrganizadores();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(organizadores).build();
	}

	@GET
	@Path("{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOrganizadorPorId(@QueryParam("id") int id) {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Organizador organizador = tm.darOrganizadorPorId(id);
			return Response.status(200).entity(organizador).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega el organizador que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RontondAndes/rest/(...)
	 * 
	 * @param organizador
	 *            - organizador a agregar
	 * @return Json con el organizador que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrganizador(Organizador organizador) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addOrganizador(organizador);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(organizador).build();
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega los organizadores
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/organizadores/varios
	 * 
	 * @param organizadors
	 *            - organizadors a agregar.
	 * @return Json con el organizador que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Path("/varios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrganizadores(List<Organizador> organizadores) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			// TODO tm.addOrganizadores(organizadores);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(organizadores).build();
	}

	/**
	 * Metodo que expone servicio REST usando PUT que actualiza el organizador
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/organizadores
	 * 
	 * @param organizador
	 *            - organizador a actualizar.
	 * @return Json con el organizador que actualizo o Json con el error que se
	 *         produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOrganizador(Organizador organizador) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateOrganizador(organizador);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(organizador).build();
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina el organizador
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/organizadores
	 * 
	 * @param organizador
	 *            - organizador a eliminar.
	 * @return Json con el organizador que elimino o Json con el error que se
	 *         produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganizador(Organizador organizador) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteOrganizador(organizador);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(organizador).build();
	}

}
