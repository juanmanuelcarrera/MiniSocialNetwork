/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.logica;

import java.sql.Blob;
import java.sql.Date;

public class Foto {
	private int idFoto;
	private String titulo;
	private String descripcion;
	private Date fecha;
	private int ancho;
	private int alto;
	private Blob foto;

	public Foto() {
		this.idFoto = -1;
	}

	public Foto(int id, String tit, String des, Date fech, int anc, int alt, Blob f) {
		this.idFoto = id;
		this.titulo = tit;
		this.descripcion = des;
		this.fecha = fech;
		this.ancho = anc;
		this.alto = alt;
		this.foto = f;
	}

	public String toString() {
		return this.titulo;
	}

	public void setIdFoto(int id) {
		this.idFoto = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public void setFoto(Blob foto) {
		this.foto = foto;
	}

	public int getIdFoto() {
		return this.idFoto;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public int getAlto() {
		return this.alto;
	}

	public int getAncho() {
		return this.ancho;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public Blob getFoto() {
		return this.foto;
	}
}
