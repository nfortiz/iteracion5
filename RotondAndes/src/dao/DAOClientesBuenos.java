package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.RestauranteMnF;

public class DAOClientesBuenos {
	

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
	public DAOClientesBuenos() {
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

	public ArrayList<RestauranteMnF> darRestaurantesMenosFrecuentados() throws SQLException {
		ArrayList<RestauranteMnF> restaurantes = new ArrayList<RestauranteMnF>();

		String sql ="select sed.RESTAURANTE_NOMBRE, sed.DIA, sed1.minimo from (SELECT RESTAURANTE_NOMBRE, to_char(FECHA, 'DAY') AS DIA, (count(restaurante_nombre)) as nRestaurante FROM ( SELECT  RESTAURANTE_NOMBRE, PRODUCTO_NOMBRE, FECHA FROM PEDIDO_PRODUCTO PED INNER JOIN OFRECER_PRODUCTO OFR ON PED.OFRECER_PRODUCTO_NOMBRE=OFR.PRODUCTO_NOMBRE AND PED.OFRECER_R_RESTAURANTE  = OFR.RESTAURANTE_NOMBRE INNER JOIN PEDIDO ON pedido_id = PEDIDO.ID WHERE PEDIDO.SERVIDO=1) group by to_char (FECHA, 'DAY'),restaurante_nombre order by  to_char (FECHA, 'DAY'), nrestaurante) sed inner join (select DIA,MIN(nRestaurante)as minimo from (SELECT RESTAURANTE_NOMBRE, to_char(FECHA, 'DAY') AS DIA, (count(restaurante_nombre)) as nRestaurante FROM (SELECT  RESTAURANTE_NOMBRE, PRODUCTO_NOMBRE, FECHA FROM PEDIDO_PRODUCTO PED INNER JOIN OFRECER_PRODUCTO OFR ON PED.OFRECER_PRODUCTO_NOMBRE=OFR.PRODUCTO_NOMBRE AND PED.OFRECER_R_RESTAURANTE  = OFR.RESTAURANTE_NOMBRE INNER JOIN PEDIDO ON pedido_id = PEDIDO.ID WHERE PEDIDO.SERVIDO=1) " 
		+ "group by to_char (FECHA, 'DAY'),restaurante_nombre order by  to_char (FECHA, 'DAY'), nrestaurante) group by DIA) sed1 on sed.DIA=sed1.DIA and sed.nRestaurante=sed1.minimo";
				
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String restaurante_nombre = rs.getString("restaurante_nombre");
			String dia = rs.getString("dia");
			int minimo= rs.getInt("minimo");
			restaurantes.add(new RestauranteMnF(restaurante_nombre, dia, minimo));
		}
		return restaurantes;
	}

}
