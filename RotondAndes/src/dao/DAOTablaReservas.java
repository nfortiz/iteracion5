package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Asistente;
import vos.Registrado;
import vos.Reserva;

public class DAOTablaReservas {

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
	public DAOTablaReservas() {
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

	public ArrayList<Reserva> darReservas() throws SQLException {
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		String sql = "SELECT * FROM RESERVA";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = rs.getInt("id");
			Date fecha = rs.getDate("fecha");
			int asistentes = rs.getInt("asistentes");
			reservas.add(new Reserva(id, fecha, asistentes));
		}
		return reservas;
	}

	public Reserva buscarReservaPorId(int id) throws SQLException {
		Reserva reserva = null;
		String sql = "SELECT * FROM RESERVA WHERE id =" + id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {

			Date fecha = rs.getDate("fecha");
			int asistentes = rs.getInt("asistentes");
			reserva = (new Reserva(id, fecha, asistentes));
		}

		return reserva;
	}

	public void addReserva(Reserva reserva) throws SQLException {
		String sql = "INSERT INTO RESERVA VALUES (";
		sql += reserva.getId() + ",";
		sql += reserva.getFecha() + ",";
		sql += reserva.getAsistentes() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void deleteReserva(Reserva reserva) throws SQLException {
		String sql = "DELETE FROM RESERVA";
		sql += " WHERE id = " + reserva.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void updateReserva(Reserva reserva) throws SQLException {

		String sql = "UPDATE RESERVA SET ";
		sql += "id='" + reserva.getId() + "',";
		sql += "fecha='" + reserva.getFecha() + "',";
		sql += "asistentes='" + reserva.getAsistentes();
		sql += " WHERE ID = " + reserva.getId();
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
