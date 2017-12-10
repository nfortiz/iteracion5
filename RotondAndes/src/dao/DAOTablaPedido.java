package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.Menu;
import vos.Pedido;
import vos.Pedido.ItemPedido;
import vos.Pedido.TipoPedido;
import vos.Producto;
import vos.Restaurante;

public class DAOTablaPedido {
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
	public DAOTablaPedido() {
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
	public ArrayList<Pedido> darPedidos() throws SQLException, Exception {
		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();

		String sql = "SELECT * FROM  PEDIDO ";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Long id = rs.getLong("id");
			String fecha = rs.getString("fecha");
			pedidos.add(new Pedido(id,fecha,null,false,null));
		}
		return pedidos;
	}

	/**
	 * Metodo que, usando la conexión a la base de datos, saca todos los
	 * restaurantes de la base de datos <b>SQL Statement:</b> SELECT * FROM
	 * PEDIDO;
	 * 
	 * @return Arraylist con los restaurantes de la base de datos.
	 * @throws SQLException
	 *             - Cualquier error que la base de datos arroje.
	 * @throws Exception
	 *             - Cualquier error que no corresponda a la base de datos
	 */
	public Pedido darPedidosPorId(Long idPedido) throws SQLException, Exception {
		Pedido pedido = null;

		String sql = "SELECT * FROM  PEDIDO WHERE ID = " + idPedido;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			Long id = rs.getLong("id");
			String fecha = rs.getDate("fecha").toString();
			pedido = new Pedido(id, fecha,null,false,null);
		}
		return pedido;
	}

	public void addPedido(Pedido pedido) throws SQLException, Exception {

		String sql = "INSERT INTO PEDIDO VALUES (";
		sql += pedido.getId() + ",'";
		sql += pedido.getFecha() + "')";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	
	public void addPedidoTotal(Pedido pedido) throws SQLException, Exception {

		String sql = "INSERT INTO PEDIDO VALUES (";
		sql +=  pedido.getId() + ",";
		sql += pedido.getIdCliente() + ",'";
		sql +=   pedido.getFecha() + "',0)";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();


		
		for(ItemPedido  item : pedido.getItems()){
			if( item.getTipoPedido().equals(TipoPedido.PEDIDO_PRODUCTO.name())) {
				sql = "SELECT DISPONIBILIDAD FROM  OFRECER_PRODUCTO  ";
				sql += "  WHERE RESTAURANTE_NOMBRE = '";
				sql += item.getNombreRestaurante() + "'";
				sql += " AND PRODUCTO_NOMBRE = '"+ item.getIdItem() +"'";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				
				if (!rs.next())throw new Exception ("No hay producto ofrecido por el restaurante.");
				
					long id = rs.getLong("DISPONIBILIDAD");
					
					if(id<1)throw new Exception ("No hay disponibilidad de productos.");					
					
					sql =" INSERT INTO PEDIDO_PRODUCTO VALUES(";
					sql +=  pedido.getId() + ",'";
					sql +=  item.getIdItem() + "','";
					sql +=  item.getNombreRestaurante() + "')";
					System.out.println(sql);
					prepStmt = conn.prepareStatement(sql);
					recursos.add(prepStmt);
					prepStmt.executeQuery();				
				
			}
			else if( item.getTipoPedido().equals(TipoPedido.PEDIDO_MENU.name())) {
				//Primero verificar que el menu sea ofrecido por el resturante
				sql= "SELECT * FROM MENU WHERE RESTAURANTE_NOMBRE = '" + item.getNombreRestaurante() +"' AND ID =" + item.getIdItem();
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				if (!rs.next())throw new Exception ("No hay menú ofrecido por el restaurante.");
				
				sql="SELECT DISPONIBILIDAD,PRODUCTO_NOMBRE "
					+ "FROM (COMBINACION_PRODUCTO INNER JOIN PEDIDO_MENU  ON  PEDIDO_MENU.MENU_ID=COMBINACION_PRODUCTO.MENU_ID)"
					+ "INNER JOIN OFRECER_PRODUCTO ON COMBINACION_PRODUCTO.OFRECER_PRODUCTO_NOMBRE = OFRECER_PRODUCTO.PRODUCTO_NOMBRE "
					+ "WHERE PEDIDO_MENU.PEDIDO_ID=" + item.getIdItem();
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
				
				//Se verifica que haya disponibilidad de todos los productos
				while (rs.next()) {
					Long disponibilidad = rs.getLong("DISPONIBILIDAD");
					if(disponibilidad.compareTo(1L)<0)throw new Exception ("No hay disponibilidad para el producto." + rs.getString("PRODUCTO_NOMBRE"));					
				}
				
				//Si el menu es ofrecido por el restaurante y hay disponibilidad, entonces se procesa el pedido
				sql =" INSERT INTO PEDIDO_MENU VALUES(";
				sql +=  pedido.getId() + ",";
				sql +=  item.getIdItem() + ")";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
				
			}
			else if ( item.getTipoPedido().equals(TipoPedido.PEDIDO_EQUIVALENCIAPRODUCTO.name())) {
				String[]productos = item.getIdItem().split(",");
				//Se verifica que exista dicha equivalencia
				sql = "select * from EQUIVALENCIAS_PRODUCTOS"
					+ " WHERE NOMBRERESTAURANTE = '"+item.getNombreRestaurante() +"' "
					+ "AND PRODUCTO1 = '"+ productos[0] +"'AND PRODUCTO2 ='"+ productos[1]+"'";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				ResultSet rs = prepStmt.executeQuery();
				if (!rs.next())throw new Exception ("No hay equivalencia ofrecido por el restaurante.");
				
				
				//Se verifica siponibilidad del producto
				sql= "SELECT OFRECER_PRODUCTO.DISPONIBILIDAD FROM EQUIVALENCIAS_PRODUCTOS INNER JOIN OFRECER_PRODUCTO ON "
				+ " EQUIVALENCIAS_PRODUCTOS.NOMBRERESTAURANTE=OFRECER_PRODUCTO.RESTAURANTE_NOMBRE"
				+ " AND OFRECER_PRODUCTO.PRODUCTO_NOMBRE=EQUIVALENCIAS_PRODUCTOS.PRODUCTO2 "
				+ "WHERE NOMBRERESTAURANTE = 'Heladeria' AND PRODUCTO1 = 'Te sabor manzana'AND PRODUCTO2 ='Te helado'";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				rs = prepStmt.executeQuery();
				long id = rs.getLong("OFRECER_PRODUCTO.DISPONIBILIDAD");
				
				if(id<1)throw new Exception ("No hay disponibilidad de productos.");	
				
				//Si la equivalencia es ofrecida por el restaurante y hay disponibilidad, entonces se procesa el pedido
				sql =" INSERT INTO PEDIDO_EQUIVALENCIASPRODUCTO VALUES(";
				sql +=  pedido.getId() + ",'";
				sql +=  productos[0] + "','";
				sql +=  productos[1] + "')";
				prepStmt = conn.prepareStatement(sql);
				recursos.add(prepStmt);
				prepStmt.executeQuery();
			}
			
		}

	}
	

	public void updatePedido(Pedido pedido) throws SQLException, Exception {

		String sql = "UPDATE PEDIDO SET ";
		sql += "NUMDISPONIBLES = '" + pedido.getFecha() + "',";
		sql += "WHERE ID=" + pedido.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void updatePedidoServido(Long idPedido) throws SQLException, Exception {
			//Se revisda que el pedido no ha sido servido
			String sql ="SELECT servido FROM PEDIDO WHERE ID= " +idPedido;
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs=prepStmt.executeQuery();
			
			if(!rs.next())throw new Exception("No existe el pedido");
			long servido = rs.getLong("servido");			
			if(servido==1)throw new Exception ("El pedido ya fue servido.");	
			
			
			//Se observa si se realizo algun pedido de un menú
				 sql="SELECT DISPONIBILIDAD,PRODUCTO_NOMBRE,RESTAURANTE_NOMBRE "
				+ "FROM (COMBINACION_PRODUCTO INNER JOIN PEDIDO_MENU  ON  PEDIDO_MENU.MENU_ID=COMBINACION_PRODUCTO.MENU_ID) "
				+ "INNER JOIN OFRECER_PRODUCTO ON COMBINACION_PRODUCTO.OFRECER_PRODUCTO_NOMBRE = OFRECER_PRODUCTO.PRODUCTO_NOMBRE "
				+ "WHERE PEDIDO_MENU.PEDIDO_ID=" + idPedido;
			 prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			 rs=prepStmt.executeQuery();
			
			//Se actualiza la disponibilidad de todos los productos
			while (rs.next()) {
				
				 sql ="UPDATE OFRECER_PRODUCTO SET DISPONIBILIDAD = DISPONIBILIDAD-1   WHERE RESTAURANTE_NOMBRE = '" + rs.getString("RESTAURANTE_NOMBRE") + "' AND PRODUCTO_NOMBRE = '"+ rs.getString("PRODUCTO_NOMBRE")+"'";	
				 prepStmt = conn.prepareStatement(sql);
				 recursos.add(prepStmt);
				 prepStmt.executeQuery();
			}
			
			//Se observa si se realizo algun pedido de un producto
			sql ="SELECT  RESTAURANTE_NOMBRE, PRODUCTO_NOMBRE,PEDIDO_ID FROM PEDIDO_PRODUCTO PED INNER JOIN OFRECER_PRODUCTO OFR ON PED.OFRECER_PRODUCTO_NOMBRE=OFR.PRODUCTO_NOMBRE AND PED.OFRECER_R_RESTAURANTE  = OFR.RESTAURANTE_NOMBRE WHERE PEDIDO_ID=";
			sql +=idPedido;
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs=prepStmt.executeQuery();
			
			//Se actualiza la disponibilidad de las equivalencias
			while (rs.next()) {
				
				 String sql1 ="UPDATE OFRECER_PRODUCTO SET DISPONIBILIDAD=DISPONIBILIDAD-1 WHERE RESTAURANTE_NOMBRE='"; 
				 sql1+= rs.getString("RESTAURANTE_NOMBRE") + "' AND PRODUCTO_NOMBRE='";
				 sql1+= rs.getString("PRODUCTO_NOMBRE") +"'";	
				 prepStmt = conn.prepareStatement(sql1);
				 recursos.add(prepStmt);
				 prepStmt.executeQuery();
			}
			
			//Se observa si se realizo el pedido de una equivalencia
			sql="select * from  PEDIDO_EQUIVALENCIASPRODUCTO   where pedido_id =5";
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs=prepStmt.executeQuery();
			
			//Se actualiza la disponibilidad de las equivalencias
			while (rs.next()) {
				
				 String sql1 ="UPDATE OFRECER_PRODUCTO SET DISPONIBILIDAD=DISPONIBILIDAD-1 WHERE RESTAURANTE_NOMBRE='"; 
				 sql1+= rs.getString("EQUI_NOMBRERESTAURANTE") + "' AND PRODUCTO_NOMBRE='";
				 sql1+= rs.getString("EQUI_PRODUCTO2") +"'";	
				 prepStmt = conn.prepareStatement(sql1);
				 recursos.add(prepStmt);
				 prepStmt.executeQuery();
			}
			
			
			//Se actualiza el pedido como servido
			sql="UPDATE PEDIDO SET SERVIDO=1 WHERE ID = "+ idPedido;
			prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			rs=prepStmt.executeQuery();
	}
	
	public void updatePedidosServidos(List<Long> idsPedidos) throws SQLException, Exception {
		
		for(Long idPedido: idsPedidos)
		{
		//Se observa si se realizo algun pedido de un menú
			String sql="SELECT DISPONIBILIDAD,PRODUCTO_NOMBRE,RESTAURANTE_NOMBRE "
			+ "FROM (COMBINACION_PRODUCTO INNER JOIN PEDIDO_MENU  ON  PEDIDO_MENU.MENU_ID=COMBINACION_PRODUCTO.MENU_ID) "
			+ "INNER JOIN OFRECER_PRODUCTO ON COMBINACION_PRODUCTO.OFRECER_PRODUCTO_NOMBRE = OFRECER_PRODUCTO.PRODUCTO_NOMBRE "
			+ "WHERE PEDIDO_MENU.PEDIDO_ID=" + idPedido;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		
		//Se actualiza la disponibilidad de todos los productos
		while (rs.next()) {
			
			 sql ="UPDATE OFRECER_PRODUCTO SET DISPONIBILIDAD = DISPONIBILIDAD-1   WHERE RESTAURANTE_NOMBRE = '" + rs.getString("RESTAURANTE_NOMBRE") + "' AND PRODUCTO_NOMBRE = '"+ rs.getString("PRODUCTO_NOMBRE")+"'";	
			 prepStmt = conn.prepareStatement(sql);
			 recursos.add(prepStmt);
			 prepStmt.executeQuery();
		}
		
		//Se observa si se realizo algun pedido de un producto
		sql ="SELECT  RESTAURANTE_NOMBRE, PRODUCTO_NOMBRE,PEDIDO_ID FROM PEDIDO_PRODUCTO PED INNER JOIN OFRECER_PRODUCTO OFR ON PED.OFRECER_PRODUCTO_NOMBRE=OFR.PRODUCTO_NOMBRE AND PED.OFRECER_R_RESTAURANTE  = OFR.RESTAURANTE_NOMBRE WHERE PEDIDO_ID=";
		sql +=idPedido;
		prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		rs=prepStmt.executeQuery();
		while (rs.next()) {
			
			 String sql1 ="UPDATE OFRECER_PRODUCTO SET DISPONIBILIDAD=DISPONIBILIDAD-1 WHERE RESTAURANTE_NOMBRE='"; 
			 sql1+= rs.getString("RESTAURANTE_NOMBRE") + "' AND PRODUCTO_NOMBRE='";
			 sql1+= rs.getString("PRODUCTO_NOMBRE") +"'";	
			 prepStmt = conn.prepareStatement(sql1);
			 recursos.add(prepStmt);
			 prepStmt.executeQuery();
		}
		}
		
}


	public void deletePedido(Pedido pedido) throws SQLException, Exception {

		String sql = "DELETE FROM PEDIDO";
		sql += " WHERE ID = " + pedido.getId();

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public List<Producto> rq11( ) throws SQLException
	{
		
		String sql = "SELECT ID, FECHA FROM PEDIDO WHERE SERVIDO=1";
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs=prepStmt.executeQuery();
		
		while (rs.next()) {
		//Peido menu
		String sql2="SELECT PRODUCTO_NOMBRE,RESTAURANTE_NOMBRE "
		+ "FROM (COMBINACION_PRODUCTO INNER JOIN PEDIDO_MENU  ON  PEDIDO_MENU.MENU_ID=COMBINACION_PRODUCTO.MENU_ID) "
		+ "INNER JOIN OFRECER_PRODUCTO ON COMBINACION_PRODUCTO.OFRECER_PRODUCTO_NOMBRE = OFRECER_PRODUCTO.PRODUCTO_NOMBRE "
		+ "WHERE PEDIDO_MENU.PEDIDO_ID=" + rs.getLong("ID")
		
		//Union con pedido producto
		+"UNION SELECT  RESTAURANTE_NOMBRE, PRODUCTO_NOMBRE FROM PEDIDO_PRODUCTO PED INNER JOIN OFRECER_PRODUCTO OFR ON PED.OFRECER_PRODUCTO_NOMBRE=OFR.PRODUCTO_NOMBRE"
		+ " AND PED.OFRECER_R_RESTAURANTE  = OFR.RESTAURANTE_NOMBRE WHERE PEDIDO_ID="+ rs.getLong("ID")
		
		//Union con equivalencia 
		+"UNION SELECT EQUI_PRODUCTO2 AS PRODUCTO_NOMBRE, EQUI_NOMBRERESTAURANTE AS RESTAURANTE_NOMBRE FROM  PEDIDO_EQUIVALENCIASPRODUCTO WHERE pedido_id ="+rs.getLong("ID");
		PreparedStatement prepStmt2 = conn.prepareStatement(sql2);
		recursos.add(prepStmt2);
		ResultSet rs2=prepStmt2.executeQuery();	
	
		
		
		}	
		
		
		return null;
		
	}

}
