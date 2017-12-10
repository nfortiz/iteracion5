package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Producto;
import vos.Restaurante;

public class DAOTablaProducto {
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
	public DAOTablaProducto() {
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
	 * Metodo que, usando la conexión a la base de datos, saca todos los
	 * restaurantes de la base de datos <b>SQL Statement:</b> SELECT * FROM
	 * RESTAURANTES;
	 * 
	 * @return Arraylist con los restaurantes de la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Producto> darProductos() throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT * FROM  PRODUCTO ";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		int n =0;
		while (rs.next()&&n<100) {
			String nombre = rs.getString("nombre");
			String clasificacion = rs.getString("clasificacion");
			String descripcionEspaniol = rs.getString("descripcionEspaniol");
			String descripcionIngles = rs.getString("descripcionIngles");
			Double costoProduccion = Double.parseDouble(rs.getString("costoProducto"));

			String tiempoPreparacion = rs.getString("tiempoPreparacion");

			productos.add(new Producto(nombre, clasificacion, descripcionEspaniol, descripcionIngles, costoProduccion
					, tiempoPreparacion));
			n++;
		}
		return productos;
	}

	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los
	 * restaurantes de la base de datos <b>SQL Statement:</b> SELECT * FROM
	 * RESTAURANTES;
	 * 
	 * @return Arraylist con los restaurantes de la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Producto> darProductosPorRestaurantes(String idRestaurante) throws SQLException, Exception {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		String sql = "SELECT nombre,clasificacion,descripcionEspaniol,descripcionIngles,costoProduccion,precioVenta,tiempoPreparacion,tipo FROM OFRECER_PRODUCTO INNER JOIN PRODUCTO ON Restaurante_nombre = '"
				+ idRestaurante + "'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("nombre");
			String clasificacion = rs.getString("clasificacion");

			String descripcionEspaniol = rs.getString("descripcionEspaniol");
			String descripcionIngles = rs.getString("descripcionIngles");
			Double costoProduccion = Double.parseDouble(rs.getString("costoProducto"));
			String tiempoPreparacion = rs.getString("tiempoPreparacion");
			
			productos.add(new Producto(nombre, clasificacion, descripcionEspaniol, descripcionIngles, costoProduccion,
					 tiempoPreparacion));
		}
		return productos;
	}

	public Producto buscarProductoPorRestaurantePorNombre(String idRestaurante, String idProducto)
			throws SQLException, Exception {
		Producto productos = null;

		String sql = "SELECT nombre,clasificacion,descripcionEspaniol,descripcionIngles,costoProducto,tiempoPreparacion,tipo FROM OFRECER_PRODUCTO INNER JOIN PRODUCTO ON Restaurante_nombre = '"
				+ idRestaurante + "' WHERE nombre='" + idProducto + "'";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			String nombre = rs.getString("nombre");
			String clasificacion = rs.getString("clasificacion");
			String descripcionEspaniol = rs.getString("descripcionEspaniol");
			String descripcionIngles = rs.getString("descripcionIngles");
			Double costoProduccion = Double.parseDouble(rs.getString("costoProductp"));
			String tiempoPreparacion = rs.getString("tiempoPreparacion");
			productos = new Producto(nombre, clasificacion, descripcionEspaniol, descripcionIngles, costoProduccion,
					 tiempoPreparacion);
		}
		return productos;
	}

//	public void addProducto(Producto producto) throws SQLException, Exception {
//
//		String sql = "INSERT INTO PRODUCTO VALUES ('";
//		sql += producto.getNombre() + "','";
//		sql += producto.getClasificacion() + "','";
//		sql += producto.getDescripcionEspaniol() + "','";
//		sql += producto.getDescripcionIngles() + "',";
//		sql += producto.getCostoProducto() + ",";
//		sql += producto.getTiempoPreparacion() + ")";
//		
//
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
//
//	}
//
//	public void updateProducto(Producto producto) throws SQLException, Exception {
//
//		String sql = "UPDATE PRODUCTO SET ";
//		sql += "CLASIFICACION ='" + producto.getClasificacion() + "',";
//		sql += "DESCRIPCIONESPANIOL = '" + producto.getDescripcionEspaniol() + "',";
//		sql += "DESCRIPCIONINGLES = '" + producto.getDescripcionIngles() + "',";
//		sql += "COSTOPRODUCTO = " + producto.getCostoProducto() + ",";
//		sql += "TIEMPOPREPARACION = '" + producto.getTiempoPreparacion() + "'";
//		sql += "WHERE NAME='" + producto.getNombre() + "'";
//
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
//	}

	public void deleteProducto(Producto producto) throws SQLException, Exception {

		String sql = "DELETE FROM PRODUCTO ";
		sql += " WHERE NOMBRE = " + producto.getNombre();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

}
