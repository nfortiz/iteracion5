package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.ProductoMasVendido;
import vos.ProductoMenosVendido;
import vos.RestauranteMnF;

public class DAOProductoMenosVendido {

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
	public DAOProductoMenosVendido() {
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

	public ArrayList<ProductoMenosVendido> darProductosMenosVendidos() throws SQLException {
		ArrayList<ProductoMenosVendido> productos= new ArrayList<ProductoMenosVendido>();

		String sql ="select sed.PRODUCTO_NOMBRE, sed.DIA, sed1.minimo from (SELECT PRODUCTO_NOMBRE, to_char(FECHA, 'DAY') AS DIA, (count(producto_nombre)) as nProducto FROM ( SELECT  RESTAURANTE_NOMBRE, PRODUCTO_NOMBRE, FECHA FROM PEDIDO_PRODUCTO PED INNER JOIN OFRECER_PRODUCTO OFR ON PED.OFRECER_PRODUCTO_NOMBRE=OFR.PRODUCTO_NOMBRE AND PED.OFRECER_R_RESTAURANTE  = OFR.RESTAURANTE_NOMBRE INNER JOIN PEDIDO ON pedido_id = PEDIDO.ID WHERE PEDIDO.SERVIDO=1) " + 
				"group by to_char (FECHA, 'DAY'),producto_nombre order by  to_char (FECHA, 'DAY'), nproducto desc) sed inner join(select DIA,Min(nProducto)as minimo from (SELECT PRODUCTO_NOMBRE, to_char(FECHA, 'DAY') AS DIA, (count(producto_nombre)) as nProducto FROM (        SELECT  RESTAURANTE_NOMBRE, PRODUCTO_NOMBRE, FECHA FROM PEDIDO_PRODUCTO PED INNER JOIN OFRECER_PRODUCTO OFR ON PED.OFRECER_PRODUCTO_NOMBRE=OFR.PRODUCTO_NOMBRE AND PED.OFRECER_R_RESTAURANTE  = OFR.RESTAURANTE_NOMBRE INNER JOIN PEDIDO ON pedido_id = PEDIDO.ID WHERE PEDIDO.SERVIDO=1) group by to_char (FECHA, 'DAY'),producto_nombre order by  to_char (FECHA, 'DAY'), nproducto desc) group by DIA) sed1 on sed.DIA=sed1.DIA and sed.nproducto=sed1.minimo "; 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String producto_nombre = rs.getString("producto_nombre");
			String dia = rs.getString("dia");
			int minimo= rs.getInt("minimo");
			productos.add(new ProductoMenosVendido(producto_nombre, dia, minimo));
		}
		return productos;
	}
	

}
