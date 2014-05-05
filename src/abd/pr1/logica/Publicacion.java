/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.logica;

import java.sql.Date;

public class Publicacion {
	private int idPublicacion;
	private String texto;
	private Date fecha;
	private Usuario creador;
	private Usuario destinatario;
	public int getIdPublicacion() {
		return idPublicacion;
	}
	
	public Publicacion(int id, String text, Date fech, Usuario creado, Usuario destino) {
		this.idPublicacion = id;
		this.texto = text;
		this.fecha = fech;
		this.creador = creado;
		this.destinatario = destino;
	}
	
	
	public void setIdPublicacion(int idPublicacion) {
		this.idPublicacion = idPublicacion;
	}
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Usuario getCreador() {
		return creador;
	}
	public void setCreador(Usuario creador) {
		this.creador = creador;
	}
	public Usuario getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}	
}
