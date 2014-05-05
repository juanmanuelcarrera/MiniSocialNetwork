/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.logica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Conexion {
	private Connection con;
	private PreparedStatement pst;

	// ------------------------------------ OPERACIONES CON LOS USUARIO -------------------------------------

	// ------------------------------------ AÑADE UN USUARIO -------------------------------------

	public void añadeUsuario(Usuario user) {
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("INSERT INTO usuario (Correo,  Contraseña, Nick, FechaNacimiento, Descripcion, IdFoto) "
							+ "VALUES (?, ?, ?, ?, ?, ?)");

			this.pst.setString(1, user.getCorreo());
			this.pst.setString(2, user.getPassword());
			this.pst.setString(3, user.getNick());
			this.pst.setDate(4, user.getFecha());
			this.pst.setString(5, user.getDescripcion());
			this.pst.setInt(6, user.getFoto().getIdFoto());
			this.pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	// ------------------------------------ MODIFICA UN USUARIO -------------------------------------

	public void modificaUsuario(Usuario user) {
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("UPDATE usuario SET Contraseña=?, Nick=?, FechaNacimiento=?, Descripcion=?, IdFoto=? WHERE correo=?");

			this.pst.setString(1, user.getPassword());
			this.pst.setString(2, user.getNick());
			this.pst.setDate(3, user.getFecha());
			this.pst.setString(4, user.getDescripcion());
			this.pst.setInt(5, user.getFoto().getIdFoto());
			this.pst.setString(6, user.getCorreo());

			this.pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	// ------------------------------------ LOGIN -------------------------------------

	public Usuario login(String user, String password) {
		Usuario usuario = null;
		ResultSet rs;

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT u.correo, u.contraseña, u.nick, u.fechanacimiento, u.descripcion, "
								+ "f.idFoto, f.titulo, f.descripcion, f.fecha, f.ancho, f.alto, f.foto " 
								+ "FROM usuario u, foto f WHERE u.correo=? AND u.IdFoto=f.IdFoto AND contraseña=?");
			
			this.pst.setString(1, user);
			this.pst.setString(2, password);
			rs = this.pst.executeQuery();

			if (rs.next()) {
				Foto f = new Foto(rs.getInt(6), rs.getString(7), rs.getString(8), rs.getDate(9), rs.getInt(10), rs.getInt(11), rs.getBlob(12));
				usuario = new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getString(5), f);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return usuario;
	}

	
	// ------------------------------------ BUSCA USUARIO -------------------------------------

	public Usuario buscaUsuario(String user) {
		Usuario usuario = null;
		ResultSet rs;

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT u.correo, u.contraseña, u.nick, u.fechanacimiento, u.descripcion, "
							+ "f.idFoto, f.titulo, f.descripcion, f.fecha, f.ancho, f.alto, f.foto "
							+ "FROM usuario u, foto f WHERE u.correo=? AND u.IdFoto=f.IdFoto");
			
			this.pst.setString(1, user);
			rs = this.pst.executeQuery();

			if (rs.next()) {
				Foto f = new Foto(rs.getInt(6), rs.getString(7), rs.getString(8), rs.getDate(9), rs.getInt(10), rs.getInt(11), rs.getBlob(12));
				usuario = new Usuario(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getString(5), f);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return usuario;
	}

	// ------------------------------------ BUSCA AMIGOS -------------------------------------

	public Object[][] cargaAmigos(Usuario user) {
		List<Usuario> auxUser = new ArrayList<Usuario>();
		List<Date> auxDate = new ArrayList<Date>();
		ResultSet rs;

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT correo1, fecha FROM amigos WHERE correo2=?");
			
			this.pst.setString(1, user.getCorreo());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Usuario usuario = this.buscaUsuario(rs.getString(1));
				auxUser.add(usuario);
				auxDate.add(rs.getDate(2));
			}

			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT correo2, fecha FROM amigos WHERE correo1=?");
			
			this.pst.setString(1, user.getCorreo());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Usuario usuario = this.buscaUsuario(rs.getString(1));
				auxUser.add(usuario);
				auxDate.add(rs.getDate(2));
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		Object[][] a = new Object[auxUser.size()][2];

		for (int i = 0; i < auxUser.size(); i++) {
			a[i][0] = auxUser.get(i);
			a[i][1] = auxDate.get(i);
		}

		return a;
	}

	// ------------------------------------ BUSCA PERSONAS -------------------------------------

	public List<Usuario> buscaPersonas(String nick) {
		List<Usuario> aux = new ArrayList<Usuario>();
		ResultSet rs;
		Statement st;

		try {
			this.connect();

			/*
			 * this.pst = this.con.prepareStatement(
			 * "SELECT correo FROM usuario WHERE nick like ?");
			 * 
			 * String busca = "'%" + nick + "%'"; System.out.println(busca);
			 * this.pst.setString(1, busca); rs = this.pst.executeQuery();
			 */

			st = this.con.createStatement();
			rs = st.executeQuery("SELECT correo FROM usuario WHERE nick like "
								+ "'%" + nick + "%'");

			while (rs.next()) {
				Usuario usuario = this.buscaUsuario(rs.getString(1));
				aux.add(usuario);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	// --------------------------------------- AÑADIR UN AMIGO -----------------------------------------------------------

	public boolean añadeAmigos(Usuario usuario, Usuario amigo) {
		boolean ok = false;
		if (!usuario.equals(amigo)) {
			try {
				this.connect();
	
				this.pst = this.con
						.prepareStatement("SELECT * FROM amigos WHERE (correo1=? && correo2=?) ||  (correo1=? && correo2=?)");
				
				this.pst.setString(1, usuario.getCorreo());
				this.pst.setString(2, amigo.getCorreo());
				this.pst.setString(3, amigo.getCorreo());
				this.pst.setString(4, usuario.getCorreo());
				ResultSet rs = this.pst.executeQuery();
	
				if (!rs.next()) {
					ok = true;
					java.util.Date utilDate = new java.util.Date();
					java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
	
					this.connect();
	
					this.pst = this.con
							.prepareStatement("INSERT INTO amigos (Correo1 ,Correo2, Fecha) VALUES (?, ?, ?)");
					
					this.pst.setString(1, usuario.getCorreo());
					this.pst.setString(2, amigo.getCorreo());
					this.pst.setDate(3, sqlDate);
	
					this.pst.executeUpdate();
				}
			
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} finally {
				this.disconnect();
			}
		}
		else 
			ok = false;

		return ok;
	}

	// --------------------------------------- BORRAR UN AMIGO -----------------------------------------------------------

	public void borraAmigo(Usuario usuario, Usuario amigo) {
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM amigos WHERE (correo1=? && correo2=?) ||  (correo1=? && correo2=?)");
			
			this.pst.setString(1, usuario.getCorreo());
			this.pst.setString(2, amigo.getCorreo());
			this.pst.setString(3, amigo.getCorreo());
			this.pst.setString(4, usuario.getCorreo());
			this.pst.executeUpdate();
		
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	// ------------------------------------ OPERACIONES CON ALBUMES ------------------------------------------------------

	// ----------------------------------- CARGAR ALBUMES A UN USUARIO ------------------------------------

	public List<Album> cargaAlbumes(Usuario user) {
		ResultSet rs;
		List<Album> aux = new ArrayList<Album>();

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT idalbum, titulo, descripcion, fecha"
							+ " FROM album WHERE correo=?");
			
			this.pst.setString(1, user.getCorreo());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Album a = new Album(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4));
				aux.add(a);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	// ----------------------------------- CARGAR FOTOS DE UN ALBUM ------------------------------------

	public List<Foto> cargaFotosAlbum(Album alb) {
		ResultSet rs;
		List<Foto> aux = new ArrayList<Foto>();

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT f.idfoto, f.titulo, f.descripcion, f.fecha, f.ancho, f.alto, f.foto"
							+ " FROM album_foto af, foto f WHERE af.idAlbum=?"
							+ " AND af.idFoto=f.idFoto");
			
			this.pst.setInt(1, alb.getIdAlbum());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Foto f = new Foto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getInt(5), rs.getInt(6), rs.getBlob(7));
				aux.add(f);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	// --------------------------------------- BORRAR UN ALBUM -----------------------------------------------------------

	public void borraAlbum(Album alb) {
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM Comentario WHERE idComentario IN " +
							"(SELECT idComentario FROM asocia_foto WHERE idFoto IN " +
							"(SELECT idFoto FROM album_foto WHERE idalbum=?))");
			
			this.pst.setInt(1, alb.getIdAlbum());
			this.pst.executeUpdate();
			
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM Foto WHERE idFoto IN " +
							"(SELECT idFoto FROM album_foto WHERE idalbum=?)");
			
			this.pst.setInt(1, alb.getIdAlbum());
			this.pst.executeUpdate();
						
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM Album WHERE idAlbum=?");
			
			this.pst.setInt(1, alb.getIdAlbum());
			this.pst.executeUpdate();
					
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	// --------------------------------------- AÑADIR UN ALBUM -----------------------------------------------------------

	public int añadeAlbum(Album alb, Usuario usu) {
		int aux = -1;

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement(
							"INSERT INTO album (Correo, IdAlbum, Titulo, Fecha, Descripcion) VALUES (?, null, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			
			this.pst.setString(1, usu.getCorreo());
			this.pst.setString(2, alb.getTitulo());
			this.pst.setDate(3, alb.getFecha());
			this.pst.setString(4, alb.getDescripcion());
			this.pst.executeUpdate();

			ResultSet rs = this.pst.getGeneratedKeys();
			if (rs.next())
				aux = rs.getInt(1);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}
		
	// --------------------------------------- OPERACIONES SOBRE FOTOS -----------------------------------------------------------

	// --------------------------------------- BORRAR UNA FOTO -----------------------------------------------------------

	public void borraFoto(int idFot) {
		try {
			this.connect();
			
			this.pst = this.con
					.prepareStatement("DELETE FROM Comentario WHERE idComentario IN " +
							"(SELECT idComentario FROM asocia_foto WHERE idFoto=?)");
			
			this.pst.setInt(1, idFot);
			this.pst.executeUpdate();
			
			this.connect();

			this.pst = this.con.prepareStatement("DELETE FROM Foto WHERE idFoto=?");
			
			this.pst.setInt(1, idFot);
			this.pst.executeUpdate();
		
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	

	// --------------------------------------- AÑADIR UNA FOTO A UN ALBUM -----------------------------------------------------------

	public int añadeFotoAlbum(Foto fot, Album alb) {
		int aux = -1;
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement(
							"INSERT INTO foto (IdFoto, Titulo, Descripcion, Fecha, Ancho, Alto, Foto) VALUES (null, ?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			
			this.pst.setString(1, fot.getTitulo());
			this.pst.setString(2, fot.getDescripcion());
			this.pst.setDate(3, fot.getFecha());
			this.pst.setInt(4, fot.getAncho());
			this.pst.setInt(5, fot.getAncho());
			this.pst.setBlob(6, fot.getFoto());
			this.pst.executeUpdate();

			ResultSet rs = this.pst.getGeneratedKeys();
			if (rs.next()) {
				aux = rs.getInt(1);
				
				this.connect();

				this.pst = this.con
						.prepareStatement("INSERT INTO album_foto(IdAlbum, IdFoto) VALUES (?, ?)");
				
				this.pst.setInt(1, alb.getIdAlbum());
				this.pst.setInt(2, rs.getInt(1));

				this.pst.executeUpdate();
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	// --------------------------------------- AÑADIR UNA FOTO (PARA PERFIL) -----------------------------------------------------------

	public int añadeFoto(Foto fot) {
		int aux = -1;
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement(
							"INSERT INTO foto (IdFoto, Titulo, Descripcion, Fecha, Ancho, Alto, Foto) VALUES (null, ?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			
			this.pst.setString(1, fot.getTitulo());
			this.pst.setString(2, fot.getDescripcion());
			this.pst.setDate(3, fot.getFecha());
			this.pst.setInt(4, fot.getAncho());
			this.pst.setInt(5, fot.getAncho());
			this.pst.setBlob(6, fot.getFoto());

			this.pst.executeUpdate();

			ResultSet rs = this.pst.getGeneratedKeys();
			if (rs.next())
				aux = rs.getInt(1);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	// --------------------------------------- OPERACIONES SOBRE COMENTARIOS -----------------------------------------------------------

	// --------------------------------------- AÑADIR UN COMENTARIO A UNA FOTO -----------------------------------------------------------

	public int añadeComentarioFoto(Foto fot, Comentario comen) {
		int aux = -1;
		
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement(
							"INSERT INTO comentario (Correo, IdComentario, Fecha, Texto) VALUES (?, null, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			
			this.pst.setString(1, comen.getUsuario().getCorreo());
			this.pst.setDate(2, comen.getFecha());
			this.pst.setString(3, comen.getTexto());

			this.pst.executeUpdate();

			ResultSet rs = this.pst.getGeneratedKeys();
			if (rs.next()) {
				aux = rs.getInt(1);
				
				this.connect();

				this.pst = this.con
						.prepareStatement("INSERT INTO asocia_foto (IdComentario, IdFoto) VALUES (?, ?)");
				
				this.pst.setInt(1, rs.getInt(1));
				this.pst.setInt(2, fot.getIdFoto());

				this.pst.executeUpdate();
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	// ----------------------------------- CARGAR COMENTARIOS DE UNA FOTO ------------------------------------

	public List<Comentario> cargaComentariosFoto(Foto fot) {
		ResultSet rs;
		List<Comentario> aux = new ArrayList<Comentario>();

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT c.idComentario, c.texto, c.fecha, c.correo"
							+ " FROM comentario c, asocia_foto f WHERE f.idFoto=? AND f.idComentario=c.idComentario");
			
			this.pst.setInt(1, fot.getIdFoto());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Comentario c = new Comentario(rs.getInt(1), rs.getString(2), rs.getDate(3), this.buscaUsuario(rs.getString(4)));
				aux.add(c);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	// --------------------------------------- BORRAR UN COMENTARIO -----------------------------------------------------------

	public void borraComentario(int idComen) {
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM comentario WHERE idComentario=?");
			
			this.pst.setInt(1, idComen);

			this.pst.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	// --------------------------------------- NUMERO DE COMENTARIOS UNA FOTO -----------------------------------------------------------

	public int comentariosFoto(Foto fot) {
		int cont = 0;

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT COUNT(idFoto) FROM asocia_foto WHERE idFoto=? GROUP BY idFoto;");
			
			this.pst.setInt(1, fot.getIdFoto());
			ResultSet rs = this.pst.executeQuery();

			if (rs.next())
				cont = rs.getInt(1);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return cont;
	}

	
	// ------------------------------------ OPERACIONES CON PUBLICACIONES ------------------------------------------------------

	// ----------------------------------- CARGAR PUBLICACIONES A UN USUARIO ------------------------------------
	
	public List<Publicacion> cargaPublicaciones(Usuario user) {
		ResultSet rs;
		List<Publicacion> aux = new ArrayList<Publicacion>();

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT idPublicacion, Fecha, Texto, CorreoCreador, CorreoDestinatario"
							+ " FROM publicacion WHERE CorreoDestinatario=?");
			
			this.pst.setString(1, user.getCorreo());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Publicacion p = new Publicacion(rs.getInt(1), rs.getString(3), rs.getDate(2), this.buscaUsuario(rs.getString(4)),
						this.buscaUsuario(rs.getString(5)));
				aux.add(p);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}
	
	// ----------------------------------- AÑADE PUBLICACIONES A UN USUARIO ------------------------------------
	
	public int añadePublicacion(Publicacion publi) {
		int aux = -1;
		
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement(
							"INSERT INTO publicacion (IdPublicacion, Fecha, Texto, CorreoCreador, CorreoDestinatario)" +
							" VALUES (null, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			
			this.pst.setDate(1, publi.getFecha());
			this.pst.setString(2, publi.getTexto());
			this.pst.setString(3, publi.getCreador().getCorreo());
			this.pst.setString(4, publi.getDestinatario().getCorreo());

			this.pst.executeUpdate();

			ResultSet rs = this.pst.getGeneratedKeys();
			if (rs.next())
				aux = rs.getInt(1);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}
	
	// ----------------------------------- AÑADE FOTOS A UNA PUBLICACIO ------------------------------------
	
	public int añadeFotoPublicacion(Publicacion publi, Foto fot) {
		int aux = -1;
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement(
							"INSERT INTO foto (IdFoto, Titulo, Descripcion, Fecha, Ancho, Alto, Foto) VALUES (null, ?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
		
			this.pst.setString(1, fot.getTitulo());
			this.pst.setString(2, fot.getDescripcion());
			this.pst.setDate(3, fot.getFecha());
			this.pst.setInt(4, fot.getAncho());
			this.pst.setInt(5, fot.getAncho());
			this.pst.setBlob(6, fot.getFoto());


			this.pst.executeUpdate();

			ResultSet rs = this.pst.getGeneratedKeys();
			if (rs.next()) {
				aux = rs.getInt(1);
				
				this.connect();

				this.pst = this.con
						.prepareStatement("INSERT INTO publicacion_foto (IdPublicacion, IdFoto) VALUES (?, ?)");
				
				this.pst.setInt(1, publi.getIdPublicacion());
				this.pst.setInt(2, rs.getInt(1));

				this.pst.executeUpdate();
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
		return aux;
	}

	// ----------------------------------- CARGA FOTOS DE UNA PUBLICACION ------------------------------------

	public List<Foto> cargaFotosPublicacion(Publicacion publi) {
		ResultSet rs;
		List<Foto> aux = new ArrayList<Foto>();

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT f.IdFoto, f.Titulo, f.Descripcion, f.Fecha, f.Ancho, f.Alto, f.Foto"
							+ " FROM foto f, publicacion_foto pf WHERE f.IdFoto=pf.IdFoto AND pf.IdPublicacion=?");
			
			
			this.pst.setInt(1, publi.getIdPublicacion());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Foto f = new Foto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getInt(5), rs.getInt(6), rs.getBlob(7));
				aux.add(f);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	
	// --------------------------------------- BORRAR FOTOS DE UNA PUBLICACION -----------------------------------------------------------

	public void borraPublicacion(Publicacion publi) {
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM Comentario WHERE idComentario IN " +
							"(SELECT idComentario FROM asocia_foto WHERE idFoto IN " +
							"(SELECT idFoto FROM publicacion_foto WHERE idPublicacion=?))");
			
			this.pst.setInt(1, publi.getIdPublicacion());
			this.pst.executeUpdate();
			
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM Foto WHERE idFoto IN " +
							"(SELECT idFoto FROM publicacion_foto WHERE idPublicacion=?)");
			
			this.pst.setInt(1, publi.getIdPublicacion());
			this.pst.executeUpdate();
						
			this.connect();
			
			this.pst = this.con
					.prepareStatement("DELETE FROM Comentario WHERE idComentario IN " +
							"(SELECT idComentario FROM asocia_publicacion WHERE idPublicacion=?)");
			
			this.pst.setInt(1, publi.getIdPublicacion());
			this.pst.executeUpdate();
			
			this.connect();

			this.pst = this.con
					.prepareStatement("DELETE FROM publicacion WHERE idPublicacion=?");
			
			this.pst.setInt(1, publi.getIdPublicacion());
			this.pst.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	// ----------------------------------- CARGAR COMENTARIOS DE UNA FOTO ------------------------------------

	public List<Comentario> cargaComentariosPublicacion(Publicacion publi) {
		ResultSet rs;
		List<Comentario> aux = new ArrayList<Comentario>();

		try {
			this.connect();

			this.pst = this.con
					.prepareStatement("SELECT c.idComentario, c.texto, c.fecha, c.correo"
							+ " FROM comentario c, asocia_publicacion pc WHERE pc.IdPublicacion=? AND pc.idComentario=c.idComentario");
			
			this.pst.setInt(1, publi.getIdPublicacion());
			rs = this.pst.executeQuery();

			while (rs.next()) {
				Comentario c = new Comentario(rs.getInt(1), rs.getString(2), rs.getDate(3), this.buscaUsuario(rs.getString(4)));
				aux.add(c);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}
	
	// --------------------------------------- AÑADIR UN COMENTARIO A UNA FOTO -----------------------------------------------------------

	public int añadeComentarioPublicacion(Publicacion publi, Comentario comen) {
		int aux = -1;
		
		try {
			this.connect();

			this.pst = this.con
					.prepareStatement(
							"INSERT INTO comentario (Correo, IdComentario, Fecha, Texto) VALUES (?, null, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			
			this.pst.setString(1, comen.getUsuario().getCorreo());
			this.pst.setDate(2, comen.getFecha());
			this.pst.setString(3, comen.getTexto());

			this.pst.executeUpdate();

			ResultSet rs = this.pst.getGeneratedKeys();
			if (rs.next()) {
				aux = rs.getInt(1);
				
				this.connect();

				this.pst = this.con
						.prepareStatement("INSERT INTO asocia_publicacion (IdComentario, IdPublicacion) VALUES (?, ?)");
				
				this.pst.setInt(1, rs.getInt(1));
				this.pst.setInt(2, publi.getIdPublicacion());

				this.pst.executeUpdate();
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}

		return aux;
	}

	
	// --------------------------------------- NUMERO DE COMENTARIOS UNA PUBLICACION -----------------------------------------------------------

		public int comentariosPublicacion(Publicacion publi) {
			int cont = 0;

			try {
				this.connect();

				this.pst = this.con
						.prepareStatement("SELECT COUNT(IdPublicacion) FROM asocia_publicacion WHERE IdPublicacion=? GROUP BY IdPublicacion;");
				
				this.pst.setInt(1, publi.getIdPublicacion());
				ResultSet rs = this.pst.executeQuery();

				if (rs.next())
					cont = rs.getInt(1);

			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} finally {
				this.disconnect();
			}

			return cont;
		}
	// ------------------------------------ CONEXIÓN Y DESCONEXIÓN DE LA BASE DE DATOS -------------------------------------

	private void connect() throws SQLException, ClassNotFoundException {
		String className = "org.gjt.mm.mysql.Driver";
		Class.forName(className);
		this.con = DriverManager.getConnection(
				"jdbc:mysql://localhost/Practica1", "UsuarioP1", "PasswordP1");
	}

	private void disconnect() {
		try {
			if (this.con != null)
				this.con.close();
		} catch (Exception e) {
		}
	}
}
	

