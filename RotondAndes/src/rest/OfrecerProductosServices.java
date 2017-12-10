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
import vos.OfrecerProductos;
import vos.Registrado;
import vos.Zona;
@Path("ofrecerProductos")
public class OfrecerProductosServices {

	@Context
	private ServletContext context;

	
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e) {
		return "{ \"ERROR\": \"" + e.getMessage() + "\"}";
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOfrecerProductos() {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<OfrecerProductos> productos;
		try {
			productos = tm.darOfrecerProductos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}
	
	@GET
	@Path("{nombreRestaurante}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOfrecerProductosPorIdRestaurante(@PathParam("nombreRestaurante") String idRestaurante) {

		RotondAndesTM tm = new RotondAndesTM(getPath());
		List<OfrecerProductos> productos;
		try {
			if (idRestaurante == null || idRestaurante.length() == 0)
				throw new Exception("Id del restaurante no valido");
			else if(idRestaurante.contains("-"))
			{
				String nuevoId= idRestaurante.replace("-", " ");
				productos = tm.darOfrecerProductosPorIdRestaurante(nuevoId);
			}
			else
			{
			productos = tm.darOfrecerProductosPorIdRestaurante(idRestaurante);
			}
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(productos).build();
	}


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOfrecerProductos(OfrecerProductos oproducto) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.addOfrecerProductos(oproducto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(oproducto).build();
	}


	@PUT
	@Path("{nombreRestaurante}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOfrecerProductos(@PathParam("nombreRestaurante") String idRestaurante, OfrecerProductos oproducto) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			if (idRestaurante == null || idRestaurante.length() == 0)
				throw new Exception("Id del ingrediente no valido");
			if(idRestaurante.contains("-"))
			{
				idRestaurante= idRestaurante.replace("-", " ");
			}
			if(!idRestaurante.equals(oproducto.getRestaurante_nombre()))
			{
				throw new Exception("No puede actualizar los productos ofrecidos a otro restaurante");
			}
			tm.updateOfrecerProductos(oproducto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(oproducto).build();
	}
	
	@PUT
	@Path("surtirRestaurante/{nombreRestaurante}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response  surtirRestaurate(@PathParam("nombreRestaurante") String idRestaurante) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			if (idRestaurante == null || idRestaurante.length() == 0)
				throw new Exception("Id del restaurante no valido");
			if(idRestaurante.contains("-"))
			{
				idRestaurante= idRestaurante.replace("-", " ");
			}
			List<OfrecerProductos> productos = tm.darOfrecerProductosPorIdRestaurante(idRestaurante);
			for(OfrecerProductos pro:productos)
			{
				int numeroM= pro.getNumMax();
				pro.setDisponibilidad(numeroM);
			}
			tm.surtirRestaurante(productos);
			//productos = tm.darOfrecerProductosPorIdRestaurante(idRestaurante);
			return Response.status(200).entity(productos).build();
		} catch (Exception e) 
		{
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
	}


	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteZona(OfrecerProductos oproducto) {
		RotondAndesTM tm = new RotondAndesTM(getPath());
		try {
			tm.deleteOfrecerProductos(oproducto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(oproducto).build();
	}

}
