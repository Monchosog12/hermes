package co.edu.unbosque.hermes.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	private String username;
	@Column(unique = true, nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	private Rol rol;
	private boolean cuentaNoExpirada;
	
	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	public Usuario(String email, String password, Rol rol, boolean cuentaNoExpirada) {
		this.email = email;
		this.password = password;
		this.rol = Rol.USER;
		this.cuentaNoExpirada = cuentaNoExpirada;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public boolean isCuentaNoExpirada() {
		return cuentaNoExpirada;
	}

	public void setCuentaNoExpirada(boolean cuentaNoExpirada) {
		this.cuentaNoExpirada = cuentaNoExpirada;
	}

	public enum Rol {
		ADMIN, USER
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", userName=" + username + ", password=" + password + ", rol="
				+ rol + ", cuentaNoExpirada=" + cuentaNoExpirada + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(cuentaNoExpirada), email, Long.valueOf(id), password, rol, username);
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
		return cuentaNoExpirada == other.cuentaNoExpirada && Objects.equals(email, other.email) && id == other.id
				&& Objects.equals(password, other.password) && rol == other.rol
				&& Objects.equals(username, other.username);
	}

}
