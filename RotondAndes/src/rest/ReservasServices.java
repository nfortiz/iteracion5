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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.RotondAndesTM;
import vos.Reserva;

public class ReservasServices {

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
	 * Metodo que expone servicio REST usando GET que da todos las reservas de
	 * la base de datos. <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/(...)
	 * 
	 * @return Json con todos las reservas de la base de datos o json con el
	 *         error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getReservas() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<Reserva> reservas;
		try {
			reservas = tm.darReservas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(reservas).build();
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega la reserva que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RontondAndes/rest/(...)
	 * 
	 * @param reserva
	 *            - reserva a agregar
	 * @return Json con el reserva que agrego o Json con el error que se produjo
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addReserva(Reserva reserva) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addReserva(reserva);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(reserva).build();
	}

	/**
	 * Metodo que expone servicio REST usando POST que agrega las reservas que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/reservas/varios
	 * 
	 * @param reservas
	 *            - reservas a agregar.
	 * @return Json con las reservas que agrego o Json con el error que se
	 *         produjo
	 */
	@POST
	@Path("/varios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addReserva(List<Reserva> reservas) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addReservas(reservas);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(reservas).build();
	}

	/**
	 * Metodo que expone servicio REST usando PUT que actualiza la reserva que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/reservas
	 * 
	 * @param reserva
	 *            - reserva a actualizar.
	 * @return Json con la reserva que actualizo o Json con el error que se
	 *         produjo
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateReserva(Reserva reserva) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.updateReserva(reserva);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(reserva).build();
	}

	/**
	 * Metodo que expone servicio REST usando DELETE que elimina la reserva que
	 * recibe en Json <b>URL: </b> http://"ip o nombre de
	 * host":8080/RotondAndes/rest/reservas
	 * 
	 * @param reserva
	 *            - reserva a eliminar.
	 * @return Json con la reserva que elimino o Json con el error que se
	 *         produjo
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteReserva(Reserva reserva) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteReserva(reserva);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(reserva).build();
	}
}
