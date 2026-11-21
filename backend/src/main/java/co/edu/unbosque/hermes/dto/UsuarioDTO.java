package co.edu.unbosque.hermes.dto;

import java.util.Objects;

import co.edu.unbosque.hermes.entity.Usuario.Rol;

/**
 * Representa el objeto de transferencia de datos para le gestion de ususuarios
 * en el sistema.
 *<p>
 * Contiene la informacón básica de autenticación y perfiles de usuario,
 * incluyendo su identificador único, correo electrónico, nombre de usuario,
 * contraseña y el rol asignado. 
 * </p>
 */
public class UsuarioDTO {

	private Long id;
	private String email;
	private String username;
	private String password;
	private Rol rol;

	public UsuarioDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con los campos principales de registro (sin ID).
	 * 
	 * @param email    Correo electrónico del usuario.
	 * @param username Nombre de usuario único para la autenticación.
	 * @param password Contraseña de acceso del usuario.
	 * @param rol      Rol o nivel de privilegios asignado (tipo {@link Rol}).
	 */
	public UsuarioDTO(String email, String username, String password, Rol rol) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.rol = rol;
	}

	/**
	 * Obtiene el identificador único del usuario.
	 *
	 * @return ID único de la entidad.
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
	 * @return Cadena con el correo electrónico.
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
	 * @return Cadena con el nombre de usuario.
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
	 * @return Cadena con la contraseña.
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
	 * @return Objeto {@link Rol} con los permisos del usuario.
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * Establece el rol asignado al usuario.
	 * 
	 * @param rol Rol a asignar.
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, password, rol, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(password, other.password) && rol == other.rol
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", rol=" + rol + "]";
	}
}