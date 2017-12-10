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
import vos.Organizador;
import vos.Registrado;

@Path("registrados")
public class RegistradosServices {

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
	 * Metodo que expone servicio REST usando GET que da todos los registrados
	 * de la base de datos. <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/
	 * 
	 * @return Json con todos los registrados de la base de datos o json con el
	 *         error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRegistrados() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Registrado> registrados;
		try {
			registrados = tm.darRegistrados();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(registrados).build();
	}

	@GET
	@Path("{id: \\d+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRegistradoPorId(@QueryParam("id") int id) {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			Registrado registrado = tm.darRegistradoPorId(id);
			return Response.status(200).entity(registrado).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega el registrado que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RontondAndes/rest/(...)
	 * 
	 * @param registrado
	 *            - registrado a agregar
	 * @return Json con el registrado que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRegistrado(Registrado registrado) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addRegistrado(registrado);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(registrado).build();
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega los registrados
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/registrados/varios
	 * 
	 * @param registrados
	 *            - registrados a agregar.
	 * @return Json con el registrado que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Path("/varios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRegistrado(List<Registrado> registrados) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addRegistrados(registrados);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(registrados).build();
	}

	/**
	 * Metodo que expone servicio REST usando PUT que actualiza el registrado
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/registrados
	 * 
	 * @param registrado
	 *            - registrado a actualizar.
	 * @return Json con el registrado que actualizo o Json con el error que se
	 *         produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRegistrado(Registrado registrado) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateRegistrado(registrado);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(registrado).build();
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina el registrado
	 * que recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/registrados
	 * 
	 * @param registrado
	 *            - registrado a eliminar.
	 * @return Json con el registrado que elimino o Json con el error que se
	 *         produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRegistrado(Registrado registrado) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteRegistrado(registrado);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(registrado).build();
	}
}
