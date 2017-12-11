package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los
 * requerimientos de la aplicación
 * 
 * @author Monitores 2017-20
 */
public class DAORotondAndes {

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
	public DAORotondAndes() {
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

	// ________________________________________________________________
	
	public ArrayList<Rentabilidad> darRentabilidadRestaurante(String fecha1, String fecha2, String restaurante) throws SQLException, Exception {
		ArrayList<Rentabilidad> productos = new ArrayList<Rentabilidad>();

		String sql = "select sum(ofp.precioventa) as valortotal,count(ofp.precioventa) as productosvendidos, ofp.PRODUCTO_NOMBRE"+
					" from (pedido ped inner join PEDIDO_PRODUCTO pedp on ped.ID=pedp.PEDIDO_ID)inner join OFRECER_PRODUCTO ofp "+
					"on pedp.OFRECER_PRODUCTO_NOMBRE = ofp.PRODUCTO_NOMBRE where fecha between"+
					" '"+fecha1+"' and '"+fecha2+"'  and ofp.RESTAURANTE_NOMBRE='"+restaurante+"' group by(ofp.PRODUCTO_NOMBRE)";


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

			productos.add(new Rentabilidad("dss",2,3));
			n++;
		}
		return productos;
	}

}
