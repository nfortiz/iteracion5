package tm;

import java.io.File;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import dao.DAOEliminarRestaurante;
import dao.DAOProductoMasVendido;
import dao.DAOProductoMenosVendido;
import dao.DAORestauranteMasFrecuentado;
import dao.DAORestauranteMenosFrecuentado;

import javax.ws.rs.QueryParam;


import dao.DAORotondAndes;

import dao.DAOTablaMenu;
import dao.DAOTablaOfrecerProductos;
import dao.DAOTablaOrganizadores;
import dao.DAOTablaPedido;
import dao.DAOTablaAsistentes;
import dao.DAOTablaEquivalenciasIngredientes;
import dao.DAOTablaEquivalenciasProductos;
import dao.DAOTablaRegistrados;
import dao.DAOTablaReservas;
import dao.DAOTablaZonas;
import dtm.RotondAndesDistributed;
import jms.NonReplyException;
import dao.DAOTablaAsistentes;

import dao.DAOTablaProducto;
import dao.DAOTablaRestaurantes;

import vos.Asistente;
import vos.EquivalenciasIngredientes;
import vos.EquivalenciasProductos;
import vos.ListaProductos;
import vos.Menu;
import vos.OfrecerProductos;
import vos.Organizador;
import vos.Pedido;
import vos.Registrado;
import vos.Producto;
import vos.ProductoMasVendido;
import vos.ProductoMenosVendido;
import vos.Registrado;
import vos.Reserva;
import vos.Restaurante;
import vos.RestauranteMF;
import vos.RestauranteMnF;
import vos.Zona;

public class RotondAndesTM {

	/**
	 * Atributo estatico que contiene el path relativo del archivo que tiene los
	 * datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los
	 * datos de la conexion
	 */
	private String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de
	 * datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String driver;

	/**
	 * conexion a la base de datos
	 */
	private Connection conn;
	private RotondAndesDistributed dtm;
	/**
	 * Metodo constructor de la clase RotondAndesMaster, esta clase modela y
	 * contiene cada una de las transacciones y la logica de negocios que estas
	 * conllevan. <b>post: </b> Se crea el objeto RotondAndesTM, se inicializa
	 * el path absoluto del archivo de conexion y se inicializa los atributos
	 * que se usan par la conexion a la base de datos.
	 * 
	 * @param contextPathP
	 *            - path absoluto en el servidor del contexto del deploy actual
	 */
	public RotondAndesTM(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
		dtm=RotondAndesDistributed.getInstance(this);
	}

	/**
	 * Metodo que inicializa los atributos que se usan para la conexion a la
	 * base de datos. <b>post: </b> Se han inicializado los atributos que se
	 * usan par la conexion a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que retorna la conexion a la base de datos
	 * 
	 * @return Connection - la conexion a la base de datos
	 * @throws SQLException
	 *             - Cualquier error que se genere durante la conexion a la base
	 *             de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	////////////////////////////////////////
	/////// Transacciones////////////////////
	////////////////////////////////////////

	/**
	 * Metodo que modela la transaccion que retorna todos los restaurantes de la
	 * base de datos.
	 * 
	 * @return ListaVideos - objeto que modela un arreglo de restaurantes. este
	 *         arreglo contiene el resultado de la busqueda
	 * @throws Exception
	 *             - cualquier error que se genere durante la transaccion
	 */
	public List<Restaurante> darRestaurantes() throws Exception {
		List<Restaurante> restaurantes;
		DAOTablaRestaurantes daoRestaurantes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRestaurantes.setConn(conn);
			restaurantes = daoRestaurantes.darRestaurantes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}

	/**
	 * Metodo que modela la transaccion que busca el/los restaurantes en la base
	 * de datos con el nombre entra como parametro.
	 * 
	 * @param name
	 *            - Nombre del restaurante a buscar. name != null
	 * @return ListaRestaurantes - objeto que modela un arreglo de restaurantes.
	 *         este arreglo contiene el resultado de la busqueda
	 * @throws Exception
	 *             - cualquier error que se genere durante la transaccion
	 */
	public Restaurante buscarRestaurantesPorNombre(String name) throws Exception {
		Restaurante restaurante;
		DAOTablaRestaurantes daoAsistentes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			restaurante = daoAsistentes.buscarRestaurantePorNombre(name);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurante;
	}

	/**
	 * Metodo que modela la transaccion que busca el video en la base de datos
	 * con el id que entra como parametro.
	 * 
	 * @param name
	 *            - Id del video a buscar. name != null
	 * @return Video - Resultado de la busqueda
	 * @throws Exception
	 *             - cualquier error que se genere durante la transaccion
	 */
	public List<Restaurante> buscarRestaurantePorZonaId(Long id) throws Exception {
		List<Restaurante> restaurantes;
		DAOTablaRestaurantes daoAsistentes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			restaurantes = daoAsistentes.buscarRestaurantesPorIdZona(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurantes;
	}

	/**
	 * Retrona la información de los clientes que han consumido un producto de un restaurante <br>
	 * en un determinado rango de fechas.
	 * La información puede ser entrgada en un orden
	 * @param id
	 * @return
	 * @throws Exception
	 */
	
	public List<Asistente> buscarClientesRangoFecha(String nombre, String fecha1, String fecha2, String ordenar) throws Exception {
		List<Asistente>  asistentes;
		DAOTablaRestaurantes daoAsistentes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			asistentes = daoAsistentes.buscarAsistentesRestaurantesPorRango(nombre, fecha1, fecha2, ordenar);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return asistentes;
	}

	/**
	 * Retrona la información de los clientes que han consumido un producto de un restaurante <br>
	 * en un determinado rango de fechas.
	 * La información puede ser entrgada en un orden
	 * @param id
	 * @return
	 * @throws Exception
	 */
	
	public List<Asistente> buscarClientesRangoFecha2(String nombre, String fecha1, String fecha2, String ordenar) throws Exception {
		List<Asistente>  asistentes;
		DAOTablaRestaurantes daoAsistentes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			asistentes = daoAsistentes.buscarAsistentesRestaurantesPorRango(nombre, fecha1, fecha2, ordenar);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return asistentes;
	}
	/**
	 * 
	 * @param nombre
	 * @param fecha1
	 * @param fecha2
	 * @param ordenar
	 * @return
	 * @throws Exception
	 */
	public List<Asistente> buscarNoClientesRangoFecha(String nombre, String fecha1, String fecha2, String ordenar) throws Exception {
		List<Asistente>  asistentes;
		DAOTablaRestaurantes daoAsistentes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			asistentes = daoAsistentes.buscarNoAsistentesRestaurantesPorRango(nombre, fecha1, fecha2, ordenar);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return asistentes;
	}

	/**
	 * Metodo que modela la transaccion que agrega un solo restaurante a la base
	 * de datos. <b> post: </b> se ha agregado el restaurante que entra como
	 * parametro
	 * 
	 * @param restaurante
	 *            - el restaurante a agregar. restaurante != null
	 * @throws Exception
	 *             - cualquier error que se genere agregando el restaurante
	 */
	public void addRestaurante(Restaurante restaurante) throws Exception {
		DAOTablaRestaurantes daoAsistentes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			daoAsistentes.addRestaurante(restaurante);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que agrega los videos que entran como
	 * parametro a la base de datos. <b> post: </b> se han agregado los videos
	 * que entran como parametro
	 * 
	 * @param videos
	 *            - objeto que modela una lista de videos y se estos se
	 *            pretenden agregar. videos != null
	 * @throws Exception
	 *             - cualquier error que se genera agregando los videos
	 */
	public void addRestaurantes(List<Restaurante> videos) throws Exception {
		DAOTablaRestaurantes daoAsistentes = new DAOTablaRestaurantes();
		try {
			////// transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoAsistentes.setConn(conn);
			Iterator<Restaurante> it = videos.iterator();
			while (it.hasNext()) {
				daoAsistentes.addRestaurante(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza el video que entra como
	 * parametro a la base de datos. <b> post: </b> se ha actualizado el video
	 * que entra como parametro
	 * 
	 * @param video
	 *            - Video a actualizar. video != null
	 * @throws Exception
	 *             - cualquier error que se genera actualizando los videos
	 */
	public void updateRestaurante(Restaurante video) throws Exception {
		DAOTablaRestaurantes daoRestaurantes = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRestaurantes.setConn(conn);
			daoRestaurantes.updateRestaurante(video);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRestaurantes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina el video que entra como
	 * parametro a la base de datos. <b> post: </b> se ha eliminado el video que
	 * entra como parametro
	 * 
	 * @param video
	 *            - Video a eliminar. video != null
	 * @throws Exception
	 *             - cualquier error que se genera actualizando los videos
	 */
	public void deleteRestaurante(Restaurante restaurante) throws Exception {
		DAOTablaRestaurantes daoVideos = new DAOTablaRestaurantes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoVideos.setConn(conn);
			daoVideos.deleteRestaurante(restaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoVideos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	////////////////////////
	////// Productos/////////
	////////////////////////
	/**
	 * Metodo que modela la transaccion que retorna todos los restaurantes de la
	 * base de datos.
	 * 
	 * @return ListaVideos - objeto que modela un arreglo de restaurantes. este
	 *         arreglo contiene el resultado de la busqueda
	 * @throws Exception
	 *             - cualquier error que se genere durante la transaccion
	 */

	public List<Producto> darProductosLocal() throws Exception {
		List<Producto> productos;
		DAOTablaProducto daoProductos = new DAOTablaProducto();

		try {
			////// transaccion
			this.conn = darConexion();
			daoProductos.setConn(conn);
			productos = daoProductos.darProductos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}
	
	public ListaProductos darProductos() throws Exception {
		ListaProductos remL = new ListaProductos(darProductosLocal());
		try
		{
			System.out.println("Entro en dar productos");
			ListaProductos resp = dtm.getRemoteProductos();
			
			System.out.println(resp.getProductos().size());
			remL.getProductos().addAll(resp.getProductos());
		}
		catch(NonReplyException e)
		{
			e.printStackTrace();
		}catch (Exception e) {
			System.out.println("Entro en error");
			e.printStackTrace();
		}
		return remL;
		
	}

	/**
	 * Metodo que modela la transaccion que retorna todos los restaurantes de la
	 * base de datos.
	 * 
	 * @return ListaVideos - objeto que modela un arreglo de restaurantes. este
	 *         arreglo contiene el resultado de la busqueda
	 * @throws Exception
	 *             - cualquier error que se genere durante la transaccion
	 */
	public List<Producto> darProductosPorRestaurante(String idRestaurante) throws Exception {
		List<Producto> productos;
		DAOTablaProducto daoProductos = new DAOTablaProducto();
		try {
			////// transaccion
			this.conn = darConexion();
			daoProductos.setConn(conn);
			productos = daoProductos.darProductosPorRestaurantes(idRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public Producto darProductosPorRestaurantePorNombre(String idRestaurante, String idProducto) throws Exception {
		Producto restaurante;
		DAOTablaProducto daoRotondAndes = new DAOTablaProducto();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRotondAndes.setConn(conn);
			restaurante = daoRotondAndes.buscarProductoPorRestaurantePorNombre(idRestaurante, idProducto);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRotondAndes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return restaurante;
	}

//	public void addProducto(Producto producto) throws Exception {
//		DAOTablaProducto daoRotondAndes = new DAOTablaProducto();
//		try {
//			////// transaccion
//			this.conn = darConexion();
//			daoRotondAndes.setConn(conn);
//			daoRotondAndes.addProducto(producto);
//			conn.commit();
//
//		} catch (SQLException e) {
//			System.err.println("SQLException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} catch (Exception e) {
//			System.err.println("GeneralException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} finally {
//			try {
//				daoRotondAndes.cerrarRecursos();
//				if (this.conn != null)
//					this.conn.close();
//			} catch (SQLException exception) {
//				System.err.println("SQLException closing resources:" + exception.getMessage());
//				exception.printStackTrace();
//				throw exception;
//			}
//		}
//
//	}

	public void addProductoToRestaurante() {
		// TODO
	}

//	public void updateProducto(Producto producto) throws Exception {
//		DAOTablaProducto daoProductos = new DAOTablaProducto();
//		try {
//			////// transaccion
//			this.conn = darConexion();
//			daoProductos.setConn(conn);
//			daoProductos.updateProducto(producto);
//
//		} catch (SQLException e) {
//			System.err.println("SQLException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} catch (Exception e) {
//			System.err.println("GeneralException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} finally {
//			try {
//				daoProductos.cerrarRecursos();
//				if (this.conn != null)
//					this.conn.close();
//			} catch (SQLException exception) {
//				System.err.println("SQLException closing resources:" + exception.getMessage());
//				exception.printStackTrace();
//				throw exception;
//			}
//		}
//	}

	public void deleteProducto(Producto producto) throws Exception {
		DAOTablaProducto daoProductos = new DAOTablaProducto();
		try {
			////// transaccion
			this.conn = darConexion();
			daoProductos.setConn(conn);
			daoProductos.deleteProducto(producto);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	////////////////////////
	////// Asistente/////////
	////////////////////////

	public List<Asistente> darAsistentes() throws Exception {
		List<Asistente> asistentes;
		DAOTablaAsistentes daoAsistentes = new DAOTablaAsistentes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			asistentes = daoAsistentes.darAsistentes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return asistentes;
	}

	public void addAsistente(Asistente asistente) throws Exception {
		DAOTablaAsistentes daoAsistentes = new DAOTablaAsistentes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			daoAsistentes.addAsistente(asistente);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addAsistentes(List<Asistente> asistentes) throws SQLException {

		DAOTablaAsistentes daoAsistentes = new DAOTablaAsistentes();
		try {
			////// transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoAsistentes.setConn(conn);
			Iterator<Asistente> it = asistentes.iterator();
			while (it.hasNext()) {
				daoAsistentes.addAsistente(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void updateAsistente(Asistente asistente) throws Exception {
		DAOTablaAsistentes daoAsistentes = new DAOTablaAsistentes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			daoAsistentes.updateAsistente(asistente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void deleteAsistente(Asistente asistente) throws Exception {
		DAOTablaAsistentes daoAsistentes = new DAOTablaAsistentes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			daoAsistentes.deleteAsistente(asistente);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	////////////////////////
	////// Registrado/////////
	////////////////////////

	public List<Registrado> darRegistrados() throws Exception {
		List<Registrado> registrados;
		DAOTablaRegistrados daoRegistrados = new DAOTablaRegistrados();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRegistrados.setConn(conn);
			registrados = daoRegistrados.darRegistrados();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRegistrados.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return registrados;
	}

	public void updateRegistrado(Registrado registrado) throws Exception {
		DAOTablaRegistrados daoRegistrados = new DAOTablaRegistrados();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRegistrados.setConn(conn);
			daoRegistrados.updateRegistrado(registrado);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRegistrados.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addRegistrados(List<Registrado> registrados) throws Exception {
		DAOTablaRegistrados daoRegistrados = new DAOTablaRegistrados();
		try {
			////// transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoRegistrados.setConn(conn);
			Iterator<Registrado> it = registrados.iterator();
			while (it.hasNext()) {
				daoRegistrados.addRegistrado(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRegistrados.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addRegistrado(Registrado registrado) throws Exception {
		DAOTablaRegistrados daoRegistrados = new DAOTablaRegistrados();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRegistrados.setConn(conn);
			daoRegistrados.addRegistrado(registrado);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRegistrados.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void deleteRegistrado(Registrado registrado) throws Exception {
		DAOTablaRegistrados daoRegistrados = new DAOTablaRegistrados();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRegistrados.setConn(conn);
			daoRegistrados.deleteRegistrado(registrado);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRegistrados.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	////////////////////////
	////// Zonas/////////
	////////////////////////
	public void deleteZona(Zona zona) throws Exception {
		DAOTablaZonas daoZonas = new DAOTablaZonas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoZonas.setConn(conn);
			daoZonas.deleteZona(zona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZonas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void updateZona(Zona zona) throws Exception {
		DAOTablaZonas daoZonas = new DAOTablaZonas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoZonas.setConn(conn);
			daoZonas.updateZona(zona);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZonas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addZonas(List<Zona> zonas) throws Exception {
		DAOTablaZonas daoZonas = new DAOTablaZonas();
		try {
			////// transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoZonas.setConn(conn);
			Iterator<Zona> it = zonas.iterator();
			while (it.hasNext()) {
				daoZonas.addZona(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoZonas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addZona(Zona zona) throws Exception {
		DAOTablaZonas daoZonas = new DAOTablaZonas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoZonas.setConn(conn);
			daoZonas.addZona(zona);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZonas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public List<Zona> darZonas() throws Exception {
		List<Zona> zonas;
		DAOTablaZonas daoZonas = new DAOTablaZonas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoZonas.setConn(conn);
			zonas = daoZonas.darZonas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoZonas.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return zonas;
	}

	////////////////////////
	////// Reservas/////////
	////////////////////////
	public void deleteReserva(Reserva reserva) throws Exception {
		DAOTablaReservas daoOfrecerProductos = new DAOTablaReservas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			daoOfrecerProductos.deleteReserva(reserva);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void updateReserva(Reserva reserva) throws Exception {
		DAOTablaReservas daoOfrecerProductos = new DAOTablaReservas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			daoOfrecerProductos.updateReserva(reserva);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addReservas(List<Reserva> reservas) throws Exception {
		DAOTablaReservas daoOfrecerProductos = new DAOTablaReservas();
		try {
			////// transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoOfrecerProductos.setConn(conn);
			Iterator<Reserva> it = reservas.iterator();
			while (it.hasNext()) {
				daoOfrecerProductos.addReserva(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public List<Reserva> darReservas() throws Exception {
		List<Reserva> reservas;
		DAOTablaReservas daoOfrecerProductos = new DAOTablaReservas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			reservas = daoOfrecerProductos.darReservas();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reservas;
	}

	public void addReserva(Reserva reserva) throws Exception {

		DAOTablaReservas daoOfrecerProductos = new DAOTablaReservas();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			daoOfrecerProductos.addReserva(reserva);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void deleteOrganizador(Organizador organizador) throws Exception {
		DAOTablaOrganizadores daoOrganizadores = new DAOTablaOrganizadores();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOrganizadores.setConn(conn);
			daoOrganizadores.deleteOrganizador(organizador);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrganizadores.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void updateOrganizador(Organizador organizador) throws Exception {
		DAOTablaOrganizadores daoOrganizadores = new DAOTablaOrganizadores();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOrganizadores.setConn(conn);
			daoOrganizadores.updateOrganizador(organizador);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrganizadores.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addRegistradoes(List<Registrado> registradoes) throws Exception {

		DAOTablaRegistrados daoRegistradoes = new DAOTablaRegistrados();
		try {
			////// transaccion - ACID Example
			this.conn = darConexion();
			conn.setAutoCommit(false);
			daoRegistradoes.setConn(conn);
			Iterator<Registrado> it = registradoes.iterator();
			while (it.hasNext()) {
				daoRegistradoes.addRegistrado(it.next());
			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			try {
				daoRegistradoes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void addOrganizador(Organizador organizador) throws Exception {

		DAOTablaOrganizadores daoOrganizadors = new DAOTablaOrganizadores();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOrganizadors.setConn(conn);
			daoOrganizadors.addOrganizador(organizador);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrganizadors.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public List<Organizador> darOrganizadores() throws Exception {
		List<Organizador> organizadores;
		DAOTablaOrganizadores daoOrganizadores = new DAOTablaOrganizadores();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOrganizadores.setConn(conn);
			organizadores = daoOrganizadores.darOrganizadores();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrganizadores.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return organizadores;
	}

	public Registrado darRegistradoPorId(int id) throws Exception {
		Registrado registrado;
		DAOTablaRegistrados daoRegistrados = new DAOTablaRegistrados();
		try {
			////// transaccion
			this.conn = darConexion();
			daoRegistrados.setConn(conn);
			registrado = daoRegistrados.buscarRegistradoPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoRegistrados.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return registrado;
	}

	public Asistente darAsistentePorId(int id) throws Exception {
		Asistente asistente;
		DAOTablaAsistentes daoAsistentes = new DAOTablaAsistentes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsistentes.setConn(conn);
			asistente = daoAsistentes.buscarAsistentePorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoAsistentes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return asistente;
	}

	public Organizador darOrganizadorPorId(int id) throws Exception {
		Organizador organizador;
		DAOTablaOrganizadores daoOrganizadors = new DAOTablaOrganizadores();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOrganizadors.setConn(conn);
			organizador = daoOrganizadors.buscarOrganizadorPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOrganizadors.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return organizador;
	}

	/////////////////////
	// ------Menu-------//
	/////////////////////
	/**
	 * Retorna la lista de menus asociados a un restaurante
	 * 
	 * @param idRestaurante
	 * @return
	 */
	public List<Menu> darMenus(String idRestaurante) throws Exception {
		List<Menu> menus;
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		try {
			////// transaccion
			this.conn = darConexion();
			daoMenu.setConn(conn);
			menus = daoMenu.darMenusPorRestaurante(idRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenu.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menus;
	}	

	public void addMenu(Menu menu) throws Exception {
		DAOTablaMenu daoMenu = new DAOTablaMenu();
		try {
			////// transaccion
			this.conn = darConexion();
			daoMenu.setConn(conn);
			daoMenu.addMenu(menu);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenu.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void updateMenu(Menu menu)throws Exception {
		
		
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try {
			////// transaccion
			this.conn = darConexion();
			daoMenus.setConn(conn);
			daoMenus.updateMenu(menu);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public void deleteMenu(Menu menu)throws Exception {
		
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try {
			////// transaccion
			this.conn = darConexion();
			daoMenus.setConn(conn);
			daoMenus.deleteMenu(menu);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}


	}
	
	public Menu darMenuPorId(Long id) throws Exception {
		Menu menu;
		DAOTablaMenu daoMenus = new DAOTablaMenu();
		try {
			////// transaccion
			this.conn = darConexion();
			daoMenus.setConn(conn);
			menu = daoMenus.buscarMenuPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoMenus.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return menu;
	}

	public List<EquivalenciasProductos> darEquivalenciasProductos() throws Exception {
		List<EquivalenciasProductos> equivalenciasProductos;
		DAOTablaEquivalenciasProductos daoEquivalenciasProductos = new DAOTablaEquivalenciasProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasProductos.setConn(conn);
			equivalenciasProductos = daoEquivalenciasProductos.darEquivalenciasProductos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalenciasProductos;
	}

//	public ArrayList<EquivalenciasProductos> buscarEquivalenciasProductosPorId(String idProducto) throws Exception {
//		ArrayList<EquivalenciasProductos> equivalencias = new ArrayList<>();
//		DAOTablaEquivalenciasProductos daoEquivalenciasProductos= new DAOTablaEquivalenciasProductos();
//		try {
//			////// transaccion
//			this.conn = darConexion();
//			daoEquivalenciasProductos.setConn(conn);
//			equivalencias = daoEquivalenciasProductos.darEquivalenciaProductoPorIdProducto(idProducto);
//
//		} catch (SQLException e) {
//			System.err.println("SQLException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} catch (Exception e) {
//			System.err.println("GeneralException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} finally {
//			try {
//				daoEquivalenciasProductos.cerrarRecursos();
//				if (this.conn != null)
//					this.conn.close();
//			} catch (SQLException exception) {
//				System.err.println("SQLException closing resources:" + exception.getMessage());
//				exception.printStackTrace();
//				throw exception;
//			}
//		}
//		return equivalencias;
//	}
	public ArrayList<EquivalenciasProductos> buscarEquivalenciasProductosPorIdRestaurante(String idRestaurante) throws Exception {
		ArrayList<EquivalenciasProductos> equivalencias = new ArrayList<>();
		DAOTablaEquivalenciasProductos daoEquivalenciasProductos= new DAOTablaEquivalenciasProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasProductos.setConn(conn);
			equivalencias = daoEquivalenciasProductos.darEquivalenciaProductoPorIdRestaurante(idRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalencias;
	}


	public void deleteEquivalenciasProductoPorEquivalencia(EquivalenciasProductos eProductos) throws Exception {
		DAOTablaEquivalenciasProductos daoEquivalenciasProductos = new DAOTablaEquivalenciasProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasProductos.setConn(conn);
			daoEquivalenciasProductos.deleteEquivalenciaPorEquivalencia(eProductos);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		
	}

	public void addEquivalenciasProductos(EquivalenciasProductos eproducto) throws Exception {
		DAOTablaEquivalenciasProductos daoEquivalenciasProductos = new DAOTablaEquivalenciasProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasProductos.setConn(conn);
			daoEquivalenciasProductos.addEquivalenciasProductos(eproducto);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

//	public ArrayList<EquivalenciasIngredientes> buscarEquivalenciasIngredientesPorId(String idIngrediente) throws Exception {
//		ArrayList<EquivalenciasIngredientes> equivalencias = new ArrayList<>();
//		DAOTablaEquivalenciasIngredientes daoEquivalenciasIngredientes= new DAOTablaEquivalenciasIngredientes();
//		try {
//			////// transaccion
//			this.conn = darConexion();
//			daoEquivalenciasIngredientes.setConn(conn);
//			equivalencias = daoEquivalenciasIngredientes.darEquivalenciaIngredientePorIdIngrediente(idIngrediente);
//
//		} catch (SQLException e) {
//			System.err.println("SQLException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} catch (Exception e) {
//			System.err.println("GeneralException:" + e.getMessage());
//			e.printStackTrace();
//			throw e;
//		} finally {
//			try {
//				daoEquivalenciasIngredientes.cerrarRecursos();
//				if (this.conn != null)
//					this.conn.close();
//			} catch (SQLException exception) {
//				System.err.println("SQLException closing resources:" + exception.getMessage());
//				exception.printStackTrace();
//				throw exception;
//			}
//		}
//		return equivalencias;
//	}

	public void addEquivalenciasIngredientes(EquivalenciasIngredientes eingrediente) throws Exception {
		DAOTablaEquivalenciasIngredientes daoEquivalenciasIngredientes = new DAOTablaEquivalenciasIngredientes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasIngredientes.setConn(conn);
			daoEquivalenciasIngredientes.addEquivalenciasIngredientes(eingrediente);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasIngredientes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public void deleteEquivalenciasIngredientePorEquivalencia(EquivalenciasIngredientes eIngredientes) throws Exception {
		DAOTablaEquivalenciasIngredientes daoEquivalenciasIngredientes = new DAOTablaEquivalenciasIngredientes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasIngredientes.setConn(conn);
			daoEquivalenciasIngredientes.deleteEquivalenciaPorEquivalencia(eIngredientes);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasIngredientes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public List<EquivalenciasIngredientes> darEquivalenciasIngredientes() throws Exception {
		List<EquivalenciasIngredientes> equivalenciasIngredientes;
		DAOTablaEquivalenciasIngredientes daoEquivalenciasIngredientes = new DAOTablaEquivalenciasIngredientes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasIngredientes.setConn(conn);
			equivalenciasIngredientes = daoEquivalenciasIngredientes.darEquivalenciasIngredientes();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasIngredientes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalenciasIngredientes;
	}

	public List<OfrecerProductos> darOfrecerProductos() throws SQLException {
		List<OfrecerProductos> productos;
		DAOTablaOfrecerProductos daoOfrecerProductos = new DAOTablaOfrecerProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			productos = daoOfrecerProductos.darOfrecerProductos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public void addOfrecerProductos(OfrecerProductos oproducto) throws SQLException {
		DAOTablaOfrecerProductos daoOfrecerProductos = new DAOTablaOfrecerProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			daoOfrecerProductos.addOfrecerProducto(oproducto);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		
	}
	
	
	
	
	//////////////////////////////////
	/////// Pedidos////////////////////
	//////////////////////////////////
	
	public List<Pedido> darPedidos() throws Exception {
		List<Pedido> pedidos;
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			pedidos = daoPedidos.darPedidos();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidos;
	}
	public Pedido darPedidoPorId(Long id) throws Exception {
		Pedido pedidos;
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			pedidos = daoPedidos.darPedidosPorId(id);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());			
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pedidos;
	}
	

	public void addPedido(Pedido pedido) throws Exception {
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			daoPedidos.addPedidoTotal(pedido);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			conn.rollback();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void addPedidosVarios(List<Pedido> pedidos) throws Exception {
		
		try {
			////// transaccion
			//this.conn = darConexion();
			
			for(Pedido pedido: pedidos){
				addPedidoTotal(pedido);
			}
			//conn.commit();
		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			//conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			//conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
//			try {
//				if (this.conn != null)
//					this.conn.close();
//			} catch (SQLException exception) {
//				System.err.println("SQLException closing resources:" + exception.getMessage());
//				exception.printStackTrace();
//				throw exception;
//			}
		}
	}

	public void addPedidoTotal(Pedido pedido) throws Exception {
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			daoPedidos.addPedidoTotal(pedido);
			conn.commit();

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void updatePedidoServido (Long idPedido) throws Exception {
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			daoPedidos.updatePedidoServido(idPedido);
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			//throw e;
		} finally {
			try {
				conn.commit();
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				//throw exception;
			}
		}
	}
	
	public void updatePedido (Pedido pedido) throws Exception {
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			daoPedidos.updatePedido(pedido);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	public void deletePedido(Pedido pedido) throws Exception {
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			daoPedidos.deletePedido(pedido);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}


	public void updateOfrecerProductos(OfrecerProductos oproducto) throws SQLException {
		DAOTablaOfrecerProductos daoOfrecerProductos = new DAOTablaOfrecerProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			daoOfrecerProductos.updateOfrecerProductos(oproducto);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public void deleteOfrecerProductos(OfrecerProductos oproducto) throws SQLException {
		DAOTablaOfrecerProductos daoOfrecerProductos = new DAOTablaOfrecerProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			daoOfrecerProductos.deleteOfrecerProductos(oproducto);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public List<OfrecerProductos> darOfrecerProductosPorIdRestaurante(String idRestaurante) throws SQLException {
		ArrayList<OfrecerProductos> productos;
		DAOTablaOfrecerProductos daoOfrecerProductos = new DAOTablaOfrecerProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			productos = daoOfrecerProductos.buscarProductosOfecidosPorIdRestaurante(idRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return productos;
	}

	public ArrayList<EquivalenciasIngredientes> buscarEquivalenciasIngredientesPorIdRestaurante(String idRestaurante) throws Exception {
		ArrayList<EquivalenciasIngredientes> equivalencias = new ArrayList<>();
		DAOTablaEquivalenciasIngredientes daoEquivalenciasIngredientes= new DAOTablaEquivalenciasIngredientes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEquivalenciasIngredientes.setConn(conn);
			equivalencias = daoEquivalenciasIngredientes.darEquivalenciaIngredientePorIdRestaurante(idRestaurante);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEquivalenciasIngredientes.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return equivalencias;
	}

	public void surtirRestaurante(List<OfrecerProductos> productos) throws SQLException {
		DAOTablaOfrecerProductos daoOfrecerProductos = new DAOTablaOfrecerProductos();
		try {
			////// transaccion
			this.conn = darConexion();
			daoOfrecerProductos.setConn(conn);
			daoOfrecerProductos.surtirRestaurante(productos);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoOfrecerProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public void updatePedidosServidos(List<Long> idsPedidos) throws Exception {
		DAOTablaPedido daoPedidos = new DAOTablaPedido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoPedidos.setConn(conn);
			daoPedidos.updatePedidosServidos(idsPedidos);
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.commit();
				daoPedidos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	//-----------------------------------------------------------------------------
	////////////////////////////////////////////////////////////////////////////
	//-----------------------------------------------------------------------------
	
	public List<ProductoMasVendido> darProductoMasConsumido() throws Exception {
		DAOProductoMasVendido daoProductos = new DAOProductoMasVendido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoProductos.setConn(conn);
			return daoProductos.darProductosMasVendidos();
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.commit();
				daoProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}
	public ArrayList<ProductoMenosVendido> darProductoMenosConsumido() throws Exception {
		DAOProductoMenosVendido daoProductos = new DAOProductoMenosVendido();
		try {
			////// transaccion
			this.conn = darConexion();
			daoProductos.setConn(conn);
			return daoProductos.darProductosMenosVendidos();
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.commit();
				daoProductos.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}

	public List<RestauranteMF> darRestauranteMasFrecuentados() throws SQLException {
		DAORestauranteMasFrecuentado daoResta = new DAORestauranteMasFrecuentado();
		try {
			////// transaccion
			this.conn = darConexion();
			daoResta.setConn(conn);
			return daoResta.darRestaurantesMasFrecuentados();
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.commit();
				daoResta.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public List<RestauranteMnF> darRestauranteMenosFrecuentados() throws SQLException {
		DAORestauranteMenosFrecuentado daoResta = new DAORestauranteMenosFrecuentado();
		try {
			////// transaccion
			this.conn = darConexion();
			daoResta.setConn(conn);
			return daoResta.darRestaurantesMenosFrecuentados();
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.commit();
				daoResta.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public List<Asistente> darBuenosClientes() throws SQLException {
		DAOTablaAsistentes daoAsis = new DAOTablaAsistentes();
		try {
			////// transaccion
			this.conn = darConexion();
			daoAsis.setConn(conn);
			return daoAsis.darBuenosClientes();
			

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.commit();
				daoAsis.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public void eliminarRestaurante(String nombre) throws SQLException {
		DAOEliminarRestaurante daoEliminarRestaurante = new DAOEliminarRestaurante();
		try {
			////// transaccion
			this.conn = darConexion();
			daoEliminarRestaurante.setConn(conn);
			daoEliminarRestaurante.eliminarRestaurante(nombre);

		} catch (SQLException e) {
			System.err.println("SQLException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			System.err.println("GeneralException:" + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			try {
				daoEliminarRestaurante.cerrarRecursos();
				if (this.conn != null)
					this.conn.close();
			} catch (SQLException exception) {
				System.err.println("SQLException closing resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
	}
	

	
	
	

}
