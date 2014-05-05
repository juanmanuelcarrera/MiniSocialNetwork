/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.logica;

import java.sql.Date;

public class Album {
	//private List<Foto> fotos;
	private int idAlbum;
	private String titulo;
	private Date fecha;
	private String descripcion;
	
	public Album() {
		this.idAlbum = -1;
	}

	public Album(int id, String tit, String des, Date fech) {
		this.idAlbum = id;
		this.titulo = tit;
		this.fecha = fech;
		this.descripcion = des;
	}

	public String toString() {
		return this.titulo;
	}

	public void setIdAlbum(int id) {
		this.idAlbum = id;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdAlbum() {
		return this.idAlbum;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public String getDescripcion() {
		return this.descripcion;
	}	
}
