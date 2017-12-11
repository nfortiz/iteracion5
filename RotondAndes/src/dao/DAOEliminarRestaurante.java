package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DAOEliminarRestaurante {
	

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
	public DAOEliminarRestaurante() {
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

	public void eliminarRestaurante(String nombreRestaurante) throws SQLException {
		

		String sql1 ="DELETE * FROM RESTAURANTE WHERE NOMBRE ='"+nombreRestaurante+"'";
		String sql2 ="DELETE * FROM OFRECER_PRODUCTO WHERE RESTAURANTE_NOMBRE ='"+nombreRestaurante+"'";
		String sql3 ="DELETE * FROM MENU WHERE RESTAURANTE_NOMBRE ='"+nombreRestaurante+"'";
		String sql4 ="DELETE * FROM EQUIVALENCIAS_PRODUCTOS WHERE RESTAURANTE_NOMBRE ='"+nombreRestaurante+"'";
		String sql5 ="DELETE * FROM EQUIVALENCIAS_INGREDIENTES WHERE RESTAURANTE_NOMBRE ='"+nombreRestaurante+"'";
		String sql6 ="SELECT * FROM PEDIDO INNER JOIN PEDIDO_PRODUCTO ON PEDIDO.ID = PEDIDO_PRODUCTO WHERE OFRECER_R_RESTAURANTE='"+nombreRestaurante+"'";
				
		PreparedStatement prepStmt = conn.prepareStatement(sql6);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			throw new SQLException("No se puede eliminar el restaurante porque aun tiene pedidos activos");
		}
		PreparedStatement prepStmt4 = conn.prepareStatement(sql4);
		recursos.add(prepStmt4);
		prepStmt4.executeQuery();

		PreparedStatement prepStmt5 = conn.prepareStatement(sql5);
		recursos.add(prepStmt5);
		prepStmt5.executeQuery();

		PreparedStatement prepStmt3 = conn.prepareStatement(sql3);
		recursos.add(prepStmt3);
		prepStmt3.executeQuery();

		PreparedStatement prepStmt2 = conn.prepareStatement(sql2);
		recursos.add(prepStmt2);
	    prepStmt2.executeQuery();

		PreparedStatement prepStmt1 = conn.prepareStatement(sql1);
		recursos.add(prepStmt1);
		prepStmt1.executeQuery();

		
	}

}

