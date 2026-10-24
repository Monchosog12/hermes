package co.edu.unbosque.hermes.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "rol", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String userName;
	@Column(unique = true, nullable = false)
	private String password;
	@Column(nullable = false)
	private Rol rol;
	@Column(nullable = false)
	private LocalDateTime fechaCreacion;

	public Usuario(String email, String password, Rol rol, LocalDateTime fechaCreacion) {
		this.email = email;
		this.password = password;
		this.rol = rol;
		this.fechaCreacion = fechaCreacion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", userName=" + userName + ", password=" + password + ", rol="
				+ rol + ", fechaCreacion=" + fechaCreacion + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, fechaCreacion, Long.valueOf(id), password, rol, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(email, other.email) && Objects.equals(fechaCreacion, other.fechaCreacion)
				&& id == other.id && Objects.equals(password, other.password) && Objects.equals(rol, other.rol)
				&& Objects.equals(userName, other.userName);
	}

	public enum Rol{
		ADMIN, 
		CLIENTE
	}

}
