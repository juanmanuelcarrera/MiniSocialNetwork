/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.logica;

import java.sql.*;

public class Usuario {
	private String correo;
	private String password;
	private String nick;
	private Date fechaNacimiento;
	private String descripcion;
	private Foto avatar;

	public Usuario() {
		this.correo = "";
	}

	public Usuario(String cor, String pass, String nic, Date fech, String des, Foto fot) {
		this.correo = cor;
		this.password = pass;
		this.nick = nic;
		this.fechaNacimiento = fech;
		this.descripcion = des;
		this.avatar = fot;
	}

	public boolean equals(Object obj) {
		return this.correo.equalsIgnoreCase(((Usuario) obj).correo);
	}

	public String toString() {
		return this.nick;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public void setAvatar(Foto avatar) {
		this.avatar = avatar;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCorreo() {
		return this.correo;
	}

	public String getPassword() {
		return this.password;
	}

	public String getNick() {
		return this.nick;
	}

	public Date getFecha() {
		return this.fechaNacimiento;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public Foto getFoto() {
		return this.avatar;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public Foto getAvatar() {
		return this.avatar;
	}

}
