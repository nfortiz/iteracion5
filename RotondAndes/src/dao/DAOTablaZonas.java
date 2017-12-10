package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los
 * requerimientos de la aplicación
 */
public class DAOTablaZonas {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOVideo <b>post: </b> Crea la instancia del
	 * DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaZonas() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos las zonas
	 * de la base de datos <b>SQL Statement:</b> SELECT * FROM ZONAS;
	 * 
	 * @return Arraylist con las zonas de la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Zona> darZonas() throws SQLException, Exception {
		ArrayList<Zona> zonas = new ArrayList<>();

		String sql = "SELECT * FROM ZONA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("id");
			Integer capacidadPublico = rs.getInt("capacidadPublico");
			boolean aptoNecesidadesEspeciales = rs.getBoolean("aptoNecesidadesEspeciales");
			String condicionTecnica = rs.getString("condicionTecnica");
			boolean abierto = rs.getBoolean("abierto");
			zonas.add(new Zona(id, capacidadPublico, aptoNecesidadesEspeciales, condicionTecnica, abierto));
		}
		return zonas;
	}

	/**
	 * Metodo que busca la zona con el id que entra como parametro.
	 * 
	 * @param id
	 *            - Id de la zona a buscar
	 * @return Zona encontrada
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public Zona buscarZonaPorId(Long id) throws SQLException, Exception {
		Zona zona = null;

		String sql = "SELECT * FROM ZONAS WHERE id =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			Integer capacidadPublico = rs.getInt("capacidaPublico");
			boolean aptoNecesidadesEspeciales = rs.getBoolean("aptoNecesidadesEspeciales");
			String condicionTecnica = rs.getString("condicionTecnica");
			boolean abierto = rs.getBoolean("abierto");
			zona = new Zona(id, capacidadPublico, aptoNecesidadesEspeciales, condicionTecnica, abierto);
		}

		return zona;
	}

	/**
	 * Metodo que agrega la zona que entra como parametro a la base de datos.
	 * 
	 * @param zona
	 *            - la zona a agregar. zona != null <b> post: </b> se ha
	 *            agregado la zona a la base de datos en la transaction actual.
	 *            pendiente que el zona master haga commit para que la zona baje
	 *            a la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje. No pudo
	 *             agregar la zona a la base de datos
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public void addZona(Zona zona) throws SQLException, Exception {

		String sql = "INSERT INTO ZONAS VALUES (";
		sql += zona.getId() + ",'";
		sql += zona.getCapacidadPublico() + "',";
		sql += zona.getAptoNecesidadesEspeciales() + "',";
		sql += zona.getCondicionTecnica() + "',";
		sql += zona.getAbierto() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la zona que entra como parametro en la base de
	 * datos.
	 * 
	 * @param zona
	 *            - la zona a actualizar. zona != null <b> post: </b> se ha
	 *            actualizado la zona en la base de datos en la transaction
	 *            actual. pendiente que el zona master haga commit para que los
	 *            cambios bajen a la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje. No pudo
	 *             actualizar la zona.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public void updateZona(Zona zona) throws SQLException, Exception {

		String sql = "UPDATE ZONAS SET ";
		sql += "capacidadPublico='" + zona.getCapacidadPublico() + "',";
		sql += "aptoNecesidadesEspeciales='" + zona.getAptoNecesidadesEspeciales() + "',";
		sql += "condicionTecnica='" + zona.getCondicionTecnica();
		sql += " WHERE ID = " + zona.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que elimina la zona que entra como parametro en la base de datos.
	 * 
	 * @param zona
	 *            - la zona a borrar. zona != null <b> post: </b> se ha borrado
	 *            la zona en la base de datos en la transaction actual.
	 *            pendiente que el zona master haga commit para que los cambios
	 *            bajen a la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje. No pudo
	 *             actualizar la zona.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteZona(Zona zona) throws SQLException, Exception {

		String sql = "DELETE FROM ZONAS";
		sql += " WHERE id = " + zona.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
