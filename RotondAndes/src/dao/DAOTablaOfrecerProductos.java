package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.OfrecerProductos;
import vos.Registrado;

public class DAOTablaOfrecerProductos {

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
	public DAOTablaOfrecerProductos() {
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

	public ArrayList<OfrecerProductos> darOfrecerProductos() throws SQLException {
		ArrayList<OfrecerProductos> productos = new ArrayList<OfrecerProductos>();

		String sql = "SELECT * FROM OFRECER_PRODUCTO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String restaurante_nombre = rs.getString("restaurante_nombre");
			String producto_nombre = rs.getString("producto_nombre");
			int precioVenta = rs.getInt("precioVenta");
			int disponibilidad = rs.getInt("disponibilidad");
			int numMax = rs.getInt("numMax");
			productos.add(new OfrecerProductos(restaurante_nombre, producto_nombre, precioVenta, disponibilidad, numMax));
		}
		return productos;
	}

	public ArrayList<OfrecerProductos> buscarProductosOfecidosPorIdRestaurante(String idRestaurante) throws SQLException {
		ArrayList<OfrecerProductos> productos = new ArrayList<OfrecerProductos>();
		String sql = "SELECT * FROM OFRECER_PRODUCTO WHERE RESTAURANTE_NOMBRE ='" + idRestaurante+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreR = rs.getString("restaurante_nombre");
			String nombreP = rs.getString("producto_nombre");
			int precioV = rs.getInt("precioventa");
			int disponibilidad = rs.getInt("disponibilidad");
			int numMax = rs.getInt("nummax");
			productos.add(new OfrecerProductos(nombreR, nombreP, precioV, disponibilidad, numMax));
		}
		return productos;
	}

	public void addOfrecerProducto(OfrecerProductos ofrecerProducto) throws SQLException {
		String sql = "INSERT INTO OFRECER_PRODUCTOS VALUES (";
		sql += ofrecerProducto.getRestaurante_nombre() + ",";
		sql += ofrecerProducto.getProducto_nombre() + ",";
		sql += ofrecerProducto.getPrecioVenta() + ",";
		sql += ofrecerProducto.getDisponibilidad() + ",";
		sql += ofrecerProducto.getNumMax() + ")";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	public void deleteOfrecerProductos(OfrecerProductos ofrecerProductos) throws SQLException {
		String sql = "DELETE FROM OFRECER_PRODUCTOS";
		sql += " WHERE restaurante_nombre = '" + ofrecerProductos.getRestaurante_nombre()+"' AND "
			+ "producto_nombre ='"+ofrecerProductos.getProducto_nombre()+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void updateOfrecerProductos(OfrecerProductos ofrecerProductos) throws SQLException {

		String sql = "UPDATE OFRECER_PRODUCTOS SET ";
		sql += "restaurante_nombre='" + ofrecerProductos.getRestaurante_nombre() + "',";
		sql += "producto_nombre'" + ofrecerProductos.getProducto_nombre() + "',";
		sql += "precioventa='" + ofrecerProductos.getPrecioVenta() + "',";
		sql += "disponibilidad=" + ofrecerProductos.getDisponibilidad()+ "',";
		sql += " nummax=" + ofrecerProductos.getNumMax();
		sql += "WHERE nombre_restaurante='" + ofrecerProductos.getRestaurante_nombre()+
				"' AND nombre_producto='" + ofrecerProductos.getProducto_nombre()+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public void surtirRestaurante(List<OfrecerProductos> productos) throws SQLException {
	for(OfrecerProductos producto:productos)
	{
		String sql = "UPDATE OFRECER_PRODUCTO SET DISPONIBILIDAD="+producto.getNumMax()+
				" WHERE restaurante_nombre='" + producto.getRestaurante_nombre()+
				"' AND producto_nombre='" + producto.getProducto_nombre()+"'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	productos = buscarProductosOfecidosPorIdRestaurante(productos.get(0).getRestaurante_nombre());
		
	}

}
