package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.EquivalenciasProductos;
import vos.Producto;
import vos.Restaurante;

public class DAOTablaEquivalenciasProductos {
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
	public DAOTablaEquivalenciasProductos() {
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


	public ArrayList<EquivalenciasProductos> darEquivalenciasProductos() throws SQLException, Exception {
		ArrayList<EquivalenciasProductos> equivalenciasproductos = new ArrayList<>();

		String sql = "SELECT * FROM  EQUIVALENCIAS_PRODUCTOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String producto1 = rs.getString("PRODUCTO1");
			String producto2 = rs.getString("PRODUCTO2");
			String nombreRestaurante = rs.getString("NOMBRERESTAURANTE");
			equivalenciasproductos.add(new EquivalenciasProductos(producto1, producto2, nombreRestaurante));
		}
		return equivalenciasproductos;
	}


	
//	public ArrayList<EquivalenciasProductos> darEquivalenciaProductoPorIdProducto(String idProducto) throws SQLException, Exception {
//		ArrayList<EquivalenciasProductos> equivalenciasproductos = new ArrayList<>();
//
//		String sql = "SELECT * FROM EQUIVALENCIAS_PRODUCTOS WHERE ID1='"+idProducto+"' or ID2='"+idProducto+"'";
//
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
//
//		while (rs.next()) {
//			String id1 = rs.getString("ID1");
//			String id2 = rs.getString("ID2");
//			String nomRestaurante= rs.getString("NOM_RESTAURANTE");
//
//			equivalenciasproductos.add(new EquivalenciasProductos(id1,id2,nomRestaurante ));
//		}
//		return equivalenciasproductos;
//	}

	
	public void addEquivalenciasProductos(EquivalenciasProductos equivalenciaproducto) throws SQLException, Exception {
		
		
		String categoria1="";
		String categoria2="";
		
		String sql="SELECT clasificacion from PRODUCTO WHERE nombre='"+equivalenciaproducto.getProducto1()+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			categoria1 = rs.getString("clasificacion");
		}
		String sql2="SELECT clasificacion from PRODUCTO WHERE nombre='"+equivalenciaproducto.getProducto2()+"'";
		PreparedStatement prepStmt2 = conn.prepareStatement(sql2);
		recursos.add(prepStmt2);
		ResultSet rs2 = prepStmt2.executeQuery();

		while (rs2.next()) {
			categoria2 = rs2.getString("clasificacion");
		}
		
		if(!categoria1.equals(categoria2))
		{
			throw new SQLException("No se puede agregar la equivalencia porque los productos son de diferente categoria");
		}
		else
		{
		String sql3 = "INSERT INTO EQUIVALENCIAS_PRODUCTOS VALUES ('";
		sql3 += equivalenciaproducto.getProducto1() + "','";
		sql3 += equivalenciaproducto.getProducto2() + "','";
		sql3 += equivalenciaproducto.getNombreRestaurante()+"')"; 

		PreparedStatement prepStmt3 = conn.prepareStatement(sql3);
		recursos.add(prepStmt3);
		prepStmt3.executeQuery();
		}

	}


	public void deleteEquivalenciaPorEquivalencia(EquivalenciasProductos equivalencia) throws SQLException, Exception {

		String sql = "DELETE FROM EQUIVALENCIAS_PRODUCTOS";
		sql += " WHERE PRODUCTO1 = '"+equivalencia.getProducto1()+"' AND PRODUCTO2='"+equivalencia.getProducto2()+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public ArrayList<EquivalenciasProductos> darEquivalenciaProductoPorIdRestaurante(String idRestaurante) throws SQLException{
	String sql = "SELECT * FROM EQUIVALENCIAS_PRODUCTOS WHERE NOMBRERESTAURANTE='"+idRestaurante+"'";

	PreparedStatement prepStmt = conn.prepareStatement(sql);
	recursos.add(prepStmt);
	ResultSet rs = prepStmt.executeQuery();
	ArrayList<EquivalenciasProductos> equivalenciasproductos = new ArrayList<>();
	while (rs.next()) {
		String producto1 = rs.getString("PRODUCTO1");
		String producto2 = rs.getString("PRODUCTO2");
		String nombreRestaurante= rs.getString("NOMBRERESTAURANTE");

		equivalenciasproductos.add(new EquivalenciasProductos(producto1,producto2,nombreRestaurante ));
	}
	return equivalenciasproductos;
}
}