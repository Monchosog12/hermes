package co.edu.unbosque.hermes.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa la entidad de Usuario dentro del sistema Hermes.
 * <p>
 * Implementa la interfaz {@link UserDetails} de Spring Security para gestionar 
 * la autenticación, autorización, roles y el estado de las cuentas de usuario 
 * en la base de datos.
 * </p>
 */
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Rol rol;
	
	private boolean cuentaNoExpirada;

	/**
	 * Constructor vacio por defecto para la entidad Usuario.
	 */
	public Usuario() {
		// Constructor por defecto requerido por JPA
	}

	/**
	 * Constructor con parámetros para inicializar un nuevo usuario con información básica.
	 * 
	 * @param email Correo electrónico único del usuario.
	 * @param username Nombre de usuario para el sistema.
	 * @param password Contraseña de acceso (generalmente cifrada).
	 * @param rol Rol asignado dentro del sistema ({@link Rol}).
	 */
	public Usuario(String email, String username, String password, Rol rol) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.rol = rol;
	}

	/**
	 * Obtiene el identificador único del usuario.
	 * 
	 * @return ID del usuario.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Establece el identificador único del usuario.
	 * 
	 * @param id ID a asignar.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtiene el correo electrónico del usuario.
	 * 
	 * @return Correo electrónico.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece el correo electrónico del usuario.
	 * 
	 * @param email Correo electrónico a asignar.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtiene el nombre de usuario.
	 * 
	 * @return Nombre de usuario.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Establece el nombre de usuario.
	 * 
	 * @param username Nombre de usuario a asignar.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Obtiene la contraseña del usuario.
	 * 
	 * @return Contraseña cifrada.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la contraseña del usuario.
	 * 
	 * @param password Contraseña a asignar.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Obtiene el rol asignado al usuario.
	 * 
	 * @return Objeto {@link Rol}.
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * Establece el rol del usuario.
	 * 
	 * @param rol Rol a asignar.
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * Verifica si la cuenta del usuario no ha expirado.
	 * 
	 * @return true si la cuenta está vigente, false en caso contrario.
	 */
	public boolean isCuentaNoExpirada() {
		return cuentaNoExpirada;
	}

	/**
	 * Define el estado de expiración de la cuenta de usuario.
	 * 
	 * @param cuentaNoExpirada Estado booleano de vigencia.
	 */
	public void setCuentaNoExpirada(boolean cuentaNoExpirada) {
		this.cuentaNoExpirada = cuentaNoExpirada;
	}

	/**
	 * Enumeración que define los roles disponibles para los usuarios en el sistema.
	 */
	public enum Rol {
		/** Rol con privilegios de administración en la plataforma. */
		ADMIN, 
		/** Rol estándar para usuarios comunes del sistema. */
		USER
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password + ", rol="
				+ rol + ", cuentaNoExpirada=" + cuentaNoExpirada + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(cuentaNoExpirada), email, id, password, rol, username);
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
		return cuentaNoExpirada == other.cuentaNoExpirada && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(password, other.password) && rol == other.rol
				&& Objects.equals(username, other.username);
	}

	/**
	 * Retorna las autoridades (roles y permisos) concedidas al usuario para Spring Security.
	 * 
	 * @return Colección de objetos {@link GrantedAuthority} basados en el rol asignado.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Evita NullPointerException con usuarios antiguos guardados sin rol
		if (rol == null) {
			return List.of();
		}
		return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
	}

}
