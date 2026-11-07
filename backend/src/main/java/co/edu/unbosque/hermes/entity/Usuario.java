package co.edu.unbosque.hermes.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	private Rol rol;
	private boolean cuentaNoExpirada;
	@ElementCollection
	private Traduccion traduccion[];

	public Usuario() {
		// TODO Auto-generated constructor stub
	}

	public Usuario(String email, String username, String password, Rol rol) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.rol = rol;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Traduccion[] getTraduccion() {
		return traduccion;
	}

	public void setTraduccion(Traduccion[] traduccion) {
		this.traduccion = traduccion;
	}

	public enum Rol {
		ADMIN, USER
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", userName=" + username + ", password=" + password + ", rol="
				+ rol + ", cuentaNoExpirada=" + cuentaNoExpirada + ", Traduccion=" + traduccion + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, Long.valueOf(id), password, rol, username, traduccion);
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
	}

}
