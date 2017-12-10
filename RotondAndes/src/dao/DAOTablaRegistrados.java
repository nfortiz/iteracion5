package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Asistente;
import vos.Registrado;

public class DAOTablaRegistrados {

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
	public DAOTablaRegistrados() {
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

	public ArrayList<Registrado> darRegistrados() throws SQLException {
		ArrayList<Registrado> registrados = new ArrayList<Registrado>();

		String sql = "SELECT * FROM REGISTRADO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("asistente_id");
			String nombre = rs.getString("nombre");
			String identificacion = rs.getString("identificacion");
			String email = rs.getString("email");
			String rol = rs.getString("rol");
			String preferencia = rs.getString("preferencia");
			registrados.add(new Registrado(id, nombre, identificacion, email, rol, preferencia));
		}
		return registrados;
	}

	public Registrado buscarRegistradoPorId(int id) throws SQLException {
		Registrado registrado = null;
		String sql = "SELECT * FROM REGISTRADO WHERE id =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {

			String nombre = rs.getString("nombre");
			String identificacion = rs.getString("identificacion");
			String email = rs.getString("email");
			String rol = rs.getString("rol");
			String preferencia = rs.getString("preferencia");
			registrado = new Registrado(id, nombre, identificacion, email, rol, preferencia);
		}

		return registrado;
	}

	public void addRegistrado(Registrado registrado) throws SQLException {
		String sql = "INSERT INTO REGISTRADO VALUES (";
		sql += registrado.getId() + ",";
		sql += registrado.getNombre() + ",";
		sql += registrado.getIdentificacion() + ",";
		sql += registrado.getEmail() + ",";
		sql += registrado.getRol() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void deleteRegistrado(Registrado registrado) throws SQLException {
		String sql = "DELETE FROM REGISTRADO";
		sql += " WHERE asistente_id = " + registrado.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void updateRegistrado(Registrado registrado) throws SQLException {

		String sql = "UPDATE REGISTRADO SET ";
		sql += "nombre='" + registrado.getNombre() + "',";
		sql += "identificacion='" + registrado.getIdentificacion() + "',";
		sql += "rol='" + registrado.getRol() + "'";
		sql += "preferencia='" + registrado.getPreferencia();
		sql += " WHERE ID = " + registrado.getId();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
