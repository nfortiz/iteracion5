package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.Organizador;

public class DAOTablaOrganizadores {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Metodo constructor que crea DAOTablaOrganizadores <b>post: </b> Crea la
	 * instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaOrganizadores() {
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

	public ArrayList<Organizador> darOrganizadores() throws SQLException {
		ArrayList<Organizador> organizadores = new ArrayList<Organizador>();

		String sql = "SELECT * FROM ORGANIZADORES";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			double ingresoVentasPlato = rs.getDouble("ingresoVentasPlato");
			double costosAsociadosPreparacion = rs.getDouble("costosAsociadosPreparacion");
			organizadores.add(new Organizador(id, ingresoVentasPlato, costosAsociadosPreparacion));
		}
		return organizadores;
	}

	public Organizador buscarOrganizadorPorId(int id) throws SQLException {
		Organizador organizador = null;
		String sql = "SELECT * FROM ORGANIZADORES WHERE id =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			double ingresoVentasPlato = rs.getDouble("ingresoVentasPlato");
			double costosAsociadosPreparacion = rs.getDouble("costosAsociadosPreparacion");
			organizador = new Organizador(id, ingresoVentasPlato, costosAsociadosPreparacion);
		}

		return organizador;
	}

	public void addOrganizador(Organizador organizador) throws SQLException {
		String sql = "INSERT INTO ORGANIZADORES VALUES (";
		sql += organizador.getId() + ",";
		sql += organizador.getIngresoVentasPlato() + ",";
		sql += organizador.getCostoAsociadosPreparacion() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void deleteOrganizador(Organizador organizador) throws SQLException {
		String sql = "DELETE FROM ORGANIZADORES";
		sql += " WHERE id = " + organizador.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void updateOrganizador(Organizador organizador) throws SQLException {

		String sql = "UPDATE ORGANIZADORES SET ";
		sql += "ingresoVentasPlato='" + organizador.getIngresoVentasPlato() + "',";
		sql += "costosAsociadosPreparacion='" + organizador.getCostoAsociadosPreparacion();
		sql += " WHERE ID = " + organizador.getId();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

}
