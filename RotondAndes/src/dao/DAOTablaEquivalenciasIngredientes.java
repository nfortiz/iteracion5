package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.EquivalenciasIngredientes;

public class DAOTablaEquivalenciasIngredientes {
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
	public DAOTablaEquivalenciasIngredientes() {
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


	public ArrayList<EquivalenciasIngredientes> darEquivalenciasIngredientes() throws SQLException, Exception {
		ArrayList<EquivalenciasIngredientes> equivalenciasingredientes = new ArrayList<>();

		String sql = "SELECT * FROM  EQUIVALENCIAS_INGREDIENTES ";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String id = rs.getString("INGREDIENTE1");
			String id2 = rs.getString("INGREDIENTE2");
			String nomRestaurante = rs.getString("NOMBRERESTAURANTE");
			equivalenciasingredientes.add(new EquivalenciasIngredientes(id, id2, nomRestaurante));
		}
		return equivalenciasingredientes;
	}


	
	public ArrayList<EquivalenciasIngredientes> darEquivalenciaIngredientePorIdIngrediente(String idIngrediente) throws SQLException, Exception {
		ArrayList<EquivalenciasIngredientes> equivalenciasingredientes = new ArrayList<>();

		String sql = "SELECT * FROM EQUIVALENCIAS_INGREDIENTES WHERE INGREDIENTE1='"+idIngrediente+"' or INGREDIENTE2='"+idIngrediente+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String id = rs.getString("INGREDIENTE1");
			String id2 = rs.getString("INGREDIENTE2");
			String nomRestaurante = rs.getString("NOMBRERESTAURANTE");
			equivalenciasingredientes.add(new EquivalenciasIngredientes(id, id2, nomRestaurante));
		}
		return equivalenciasingredientes;
	}

	
	public void addEquivalenciasIngredientes(EquivalenciasIngredientes equivalenciaingrediente) throws SQLException, Exception {
		
		
		
		String sql3 = "INSERT INTO EQUIVALENCIAS_INGREDIENTES VALUES ('";
		sql3 += equivalenciaingrediente.getIngrediente1() + "','";
		sql3 += equivalenciaingrediente.getIngrediente2()+"','";
		sql3 +=equivalenciaingrediente.getNombreRestaurante()+"')";

		PreparedStatement prepStmt3 = conn.prepareStatement(sql3);
		recursos.add(prepStmt3);
		prepStmt3.executeQuery();
		

	}


	public void deleteEquivalenciaPorEquivalencia(EquivalenciasIngredientes equivalencia) throws SQLException, Exception {

		String sql = "DELETE FROM EQUIVALENCIAS_INGREDIENTES";
		sql += " WHERE INGREDIENTE1 = '"+equivalencia.getIngrediente1()+"' AND INGREDIENTE2='"+equivalencia.getIngrediente2()+"'"
			+"AND NOMBRERESTAURANTE='"+equivalencia.getNombreRestaurante()+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public ArrayList<EquivalenciasIngredientes> darEquivalenciaIngredientePorIdRestaurante(String idRestaurante)throws SQLException, Exception {
		ArrayList<EquivalenciasIngredientes> equivalenciasingredientes = new ArrayList<>();

		String sql = "SELECT * FROM EQUIVALENCIAS_INGREDIENTES WHERE NOMBRERESTAURANTE='"+idRestaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			String id = rs.getString("INGREDIENTE1");
			String id2 = rs.getString("INGREDIENTE2");
			String nomRestaurante = rs.getString("NOMBRERESTAURANTE");
			equivalenciasingredientes.add(new EquivalenciasIngredientes(id, id2, nomRestaurante));
		}
		return equivalenciasingredientes;
	}

}
