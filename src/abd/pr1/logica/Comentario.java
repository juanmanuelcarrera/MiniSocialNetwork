/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.logica;

import java.sql.Date;

public class Comentario {
	private int idComentario;
	private String texto;
	private Date fecha;
	private Usuario creador;

	public Comentario(int idCom, String text, Date fech, Usuario usu) {
		this.idComentario = idCom;
		this.texto = text;
		this.fecha = fech;
		this.creador = usu;
	}

	public void setIdComentario(int id) {
		this.idComentario = id;
	}

	public int getIdComentario() {
		return this.idComentario;
	}

	public String getTexto() {
		return this.texto;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public Usuario getUsuario() {
		return this.creador;
	}
}
