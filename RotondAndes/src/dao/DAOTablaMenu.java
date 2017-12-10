package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;

public class DAOTablaMenu {
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
	public DAOTablaMenu() {
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

	public ArrayList<Menu> darMenusPorRestaurante(String idRestaurante) throws SQLException, Exception {
		ArrayList<Menu> menus = new ArrayList<Menu>();
		String sql = "SELECT * FROM MENU WHERE Restaurante_nombre='" + idRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			Long id = rs.getLong("ID");
			Long precio = rs.getLong("PRECIOVENTA");
			menus.add(new Menu(id, precio));
		}
		return menus;
	}

	public Menu buscarMenuPorIdPorRestaurante(String idRestaurante, Long id) throws SQLException, Exception {
		Menu menus = null;

		String sql = "SELECT * FROM MENU WHERE ID ='" + id + "' AND Restaurante_nombre='" + idRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			Long precio = rs.getLong("PRECIOVENTA");

			menus = new Menu(id, precio);
		}

		return menus;
	}

	public Menu buscarMenuPorId(Long id) throws SQLException, Exception {
		Menu menus = null;
		String sql = "SELECT * FROM MENU WHERE ID=" + id + "";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			Long precio = rs.getLong("PRECIOVENTA");
			menus = new Menu(id, precio);
		}
		return menus;
	}

	public void addMenu(Menu menu) throws SQLException, Exception {

		String sql = "INSERT INTO MENU VALUES (";
		sql += menu.getId() + ",";
		sql += menu.getPrecioVenta() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateMenu(Menu menu) throws SQLException, Exception {

		String sql = "UPDATE MENU SET ";
		sql += "PRECIO=" + menu.getPrecioVenta() ;
		sql += " WHERE ID = " + menu.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void deleteMenu(Menu menu) throws SQLException, Exception {

		String sql = "DELETE FROM MENU";
		sql += " WHERE ID = " + menu.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
