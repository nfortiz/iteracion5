package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Asistente;
import vos.Pedido;
import vos.Registrado;
import vos.Restaurante;

public class DAOTablaRestaurantes {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAORotondAndes <b>post: </b> Crea la
	 * instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaRestaurantes() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Metodo que cierra todos los recursos que estan enel arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for (Object ob : recursos) {
			if (ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que inicializa la connection del DAO a la base de datos con la
	 * conexión que entra como parametro.
	 * 
	 * @param con
	 *            - connection a la base de datos
	 */
	public void setConn(Connection con) {
		this.conn = con;
	}

	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los
	 * restaurantes de la base de datos <b>SQL Statement:</b> SELECT * FROM
	 * RESTAURANTES;
	 * 
	 * @return Arraylist con los restaurantes de la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Restaurante> darRestaurantes() throws SQLException, Exception {
		ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();

		String sql = "SELECT * FROM RESTAURANTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			String tipoComida = rs.getString("TIPOCOMIDA");
			String paginaWeb = rs.getString("PAGINAWEB");
			String representante = rs.getString("REPRESENTANTE");
			Long Zona_id = rs.getLong("ZONA_ID");
			restaurantes.add(new Restaurante(nombre, tipoComida, paginaWeb, representante, Zona_id));
		}
		return restaurantes;
	}

	/**
	 * Metodo que busca el restaurante con el nombre que entra como parametro.
	 * 
	 * @param name
	 *            - Nombre del restaurante a buscar
	 * @return ArrayList con los restaurantes encontrados
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public Restaurante buscarRestaurantePorNombre(String nombre) throws SQLException, Exception {
		Restaurante restaurante = null;

		String sql = "SELECT * FROM RESTAURANTE WHERE nombre ='" + nombre + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		if (rs == null) {
			restaurante = null;
		} else if (rs.next()) {

			String tipoComida = rs.getString("tipoComida");
			String paginaWeb = rs.getString("paginaWeb");
			String representante = rs.getString("representante");
			Long Zona_id = rs.getLong("Zona_id");
			restaurante = (new Restaurante(nombre, tipoComida, paginaWeb, representante, Zona_id));
		}

		return restaurante;
	}

	/**
	 * Metodo que busca los restaurante con el id de zona que entra como
	 * parametro.
	 * 
	 * @param name
	 *            - Id de la zona de los restaurantes a buscar
	 * @return Restaurantes encontrados
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList buscarRestaurantesPorIdZona(Long idZona) throws SQLException, Exception {
		ArrayList<Restaurante> restaurantes = new ArrayList<>();

		String sql = "SELECT * FROM RESTAURANTE WHERE Zona_id =" + idZona;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre2 = rs.getString("nombre");
			String tipoComida = rs.getString("tipoComida");
			String paginaWeb = rs.getString("paginaWeb");
			String representante = rs.getString("representante");
			Long Zona_id = rs.getLong("Zona_id");
			restaurantes.add(new Restaurante(nombre2, tipoComida, paginaWeb, representante, Zona_id));
		}

		return restaurantes;
	}

	/**
	 * Retrona la información de los clientes que han consumido un producto de un restaurante <br>
	 * en un determinado rango de fechas.
	 * La información puede ser entrgada en un orden.
	 * @param nombre
	 * @param fecha1
	 * @param fecha2
	 * @param ordenar criterios de ordenamiento
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList buscarAsistentesRestaurantesPorRango(String nombre, String fecha1, String fecha2, String ordenar) throws SQLException, Exception {
		ArrayList<Registrado> asistentes = new ArrayList<>();

		String sql = "SELECT * "+
				"FROM (PEDIDO PED INNER JOIN PEDIDO_PRODUCTO PEP ON PED.ID = PEP.PEDIDO_ID) left outer join REGISTRADO usuario ON PED.ASISTENTE_ID= usuario.ASISTENTE_ID "+
				"WHERE PEP.OFRECER_R_RESTAURANTE='"+nombre+"' AND PED.FECHA BETWEEN '"+fecha1+"' AND '"+fecha2+"'";
		if(ordenar!=null && ordenar.length()!=0)sql+=" ORDER BY (" +ordenar+")";
		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {			
			Integer id = rs.getInt("ASISTENTE_ID");
			String nombreUser= rs.getString("NOMBRE");
			String identificacion = rs.getString("IDENTIFICACION");
			String email = rs.getString("EMAIL");
			String rol = rs.getString("ROL");
			String preferencia = rs.getString("PREFERENCIA");
			asistentes.add(new Registrado(id, nombreUser, identificacion, email, rol, preferencia));
		}

		return asistentes;
	}


	public ArrayList buscarAsistentesRestaurantesPorRango2(String nombre, String fecha1, String fecha2, String ordenar) throws SQLException, Exception {
		ArrayList<Pedido> pedidos = new ArrayList<>();
		ArrayList<Asistente> asistentes = new ArrayList<>();

		String sql = "SELECT * FROM PEDIDO";		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {			
			Integer id = rs.getInt("ID");
			Integer asistente_id= rs.getInt("ASISTENTE_ID");
			String fecha = rs.getString("FECHA");
			Integer ser = rs.getInt("SERVIDO");
			boolean servido = false;
			if(ser.compareTo(1)==0)servido=true;			
			pedidos.add(new Pedido(Long.valueOf(id), fecha, Long.valueOf(asistente_id), servido, null));
		}
		
		sql = "SELECT * FROM PEDIDO";		
		prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		rs = prepStmt.executeQuery();

		while (rs.next()) {			
			Integer id = rs.getInt("ID");
			Integer asistente_id= rs.getInt("ASISTENTE_ID");
			String fecha = rs.getString("FECHA");
			Integer ser = rs.getInt("SERVIDO");
			boolean servido = false;
			if(ser.compareTo(1)==0)servido=true;			
			pedidos.add(new Pedido(Long.valueOf(id), fecha, Long.valueOf(asistente_id), servido, null));
		}


		return pedidos;
	}
	/**
	 * 
	 * @param nombre
	 * @param fecha1
	 * @param fecha2
	 * @param ordenar
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList buscarNoAsistentesRestaurantesPorRango(String nombre, String fecha1, String fecha2, String ordenar) throws SQLException, Exception {
		ArrayList<Registrado> asistentes = new ArrayList<>();

		String sql = "SELECT * "+
				" FROM ((PEDIDO PED INNER JOIN PEDIDO_PRODUCTO PEP ON PED.ID = PEP.PEDIDO_ID and PED.FECHA BETWEEN '"+fecha1+"' AND '"+fecha2+"' and "
				+ "PEP.OFRECER_R_RESTAURANTE='"+nombre+"'  ) "+
				" right join ASISTENTE usuario ON PED.ASISTENTE_ID = usuario.ID)  left join REGISTRADO reg ON usuario.id=reg.ASISTENTE_ID "+
				" WHERE PED.ASISTENTE_ID IS NULL";
		if(ordenar!=null && ordenar.length()!=0)sql+=" ORDER BY (" +ordenar+")";
		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {			
			Integer id = rs.getInt("ASISTENTE_ID");
			String nombreUser= rs.getString("NOMBRE");
			String identificacion = rs.getString("IDENTIFICACION");
			String email = rs.getString("EMAIL");
			String rol = rs.getString("ROL");
			String preferencia = rs.getString("PREFERENCIA");
			asistentes.add(new Registrado(id, nombreUser, identificacion, email, rol, preferencia));
		}

		return asistentes;
	}
	/**
	 * Metodo que agrega el restaurante que entra como parametro a la base de
	 * datos.
	 * 
	 * @param Restaurantte
	 *            - el restaurante a agregar. restaurante != null <b> post: </b>
	 *            se ha agregado el restaurante a la base de datos en la
	 *            transaction actual. pendiente que el restaurante master haga
	 *            commit para que el restaurante baje a la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje. No pudo
	 *             agregar el restaurante a la base de datos
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public void addRestaurante(Restaurante restaurante) throws SQLException, Exception {



		String sql = "INSERT INTO RESTAURANTE VALUES ('";

		sql += restaurante.getNombre() + "','";
		sql += restaurante.getTipoComida() + "','";
		sql += restaurante.getPaginaWeb() + "','";
		sql += restaurante.getRepresentante() + "',";
		sql += restaurante.getZona_id() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza el restaurante que entra como parametro en la base
	 * de datos.
	 * 
	 * @param restaurante
	 *            - el restaurante a actualizar. restaurante != null <b> post:
	 *            </b> se ha actualizado el restaurante en la base de datos en
	 *            la transaction actual. pendiente que el restaurante master
	 *            haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje. No pudo
	 *             actualizar el restaurante.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public void updateRestaurante(Restaurante restaurante) throws SQLException, Exception {

		String sql = "UPDATE RESTAURANTES SET ";
		sql += "nombre='" + restaurante.getNombre() + "','";
		sql += "tipoComida='" + restaurante.getTipoComida() + "',";
		sql += "paginaWeb='" + restaurante.getPaginaWeb() + "',";
		sql += "representante='" + restaurante.getRepresentante() + "'";
		// sql += "Zona_id=" + restaurante.getZonaId();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina el restaurante que entra como parametro en la base de
	 * datos.
	 * 
	 * @param restaurante
	 *            - el restaurante a borrar. restaurante != null <b> post: </b>
	 *            se ha borrado el restaurante en la base de datos en la
	 *            transaction actual. pendiente que el restaurante master haga
	 *            commit para que los cambios bajen a la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje. No pudo
	 *             eliminar el restaurante.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteRestaurante(Restaurante restaurante) throws SQLException, Exception {

		String sql = "DELETE FROM RESTAURANTES";
		sql += " WHERE nombre = " + restaurante.getNombre();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
