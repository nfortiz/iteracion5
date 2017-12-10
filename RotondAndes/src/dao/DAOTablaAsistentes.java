package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Asistente;

public class DAOTablaAsistentes {

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
	public DAOTablaAsistentes() {
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

	public ArrayList<Asistente> darAsistentes() throws SQLException {
		ArrayList<Asistente> asistentes = new ArrayList<Asistente>();

		String sql = "SELECT * FROM ASISTENTE";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			asistentes.add(new Asistente(id));
		}
		return asistentes;
	}

	public Asistente buscarAsistentePorId(int id) throws SQLException {
		Asistente asistente = null;
		String sql = "SELECT * FROM ASISTENTE WHERE id =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {

			asistente = new Asistente(id);
		}

		return asistente;
	}

	public void addAsistente(Asistente asistente) throws SQLException {
		String sql = "INSERT INTO ASISTENTE VALUES (";
		sql += asistente.getId() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void updateAsistente(Asistente asistente) {
		/////
	}

	public void deleteAsistente(Asistente asistente) throws SQLException {
		String sql = "DELETE FROM ASISTENTE";
		sql += " WHERE id = " + asistente.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	public ArrayList<Asistente> darBuenosClientes() throws SQLException {
		ArrayList<Asistente> asistentes = new ArrayList<Asistente>();

		String sql = "SELECT * FROM (SELECT ASISTENTE_ID FROM PEDIDO WHERE PEDIDO.SERVIDO = 1 MINUS(SELECT ASISTENTE_ID FROM PEDIDO_MENU INNER JOIN PEDIDO ON PEDIDO_MENU.PEDIDO_ID = PEDIDO.ID AND PEDIDO.SERVIDO=1) MINUS(SELECT  ASISTENTE_ID FROM (PEDIDO_PRODUCTO INNER JOIN PRODUCTO ON OFRECER_PRODUCTO_NOMBRE= PRODUCTO.NOMBRE and PRODUCTO.CLASIFICACION <> 'Platos_Fuertes' INNER JOIN PEDIDO ON PEDIDO_PRODUCTO.PEDIDO_ID = PEDIDO.ID and PEDIDO.SERVIDO=1  ))MINUS ( SELECT ASISTENTE_ID FROM PEDIDO_EQUIVALENCIASPRODUCTO INNER JOIN PEDIDO ON pedido_id = PEDIDO.ID WHERE PEDIDO.SERVIDO=1))";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("asistente_id");
			asistentes.add(new Asistente(id));
		}
		return asistentes;
	}


}
