package co.edu.unbosque.hermes.dto;

import java.util.Arrays;
import java.util.Objects;

import co.edu.unbosque.hermes.entity.Usuario.Rol;

public class UsuarioDTO {

	private Long id;
	private String email;
	private String username;
	private String password;
	private Rol rol;
	private TraduccionDTO[] traduccion;

	public UsuarioDTO() {
		// TODO Auto-generated constructor stub
	}

	public UsuarioDTO(String email, String username, String password, Rol rol) {
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

	public TraduccionDTO[] getTraduccion() {
		return traduccion;
	}

	public void setTraduccion(TraduccionDTO[] traduccion) {
		this.traduccion = traduccion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(traduccion);
		result = prime * result + Objects.hash(email, id, password, rol, username);
		return result;
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
		return Objects.equals(email, other.email) && Objects.equals(id, other.id)
				&& Objects.equals(password, other.password) && rol == other.rol
				&& Arrays.equals(traduccion, other.traduccion) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", rol=" + rol + ", traduccion=" + Arrays.toString(traduccion) + "]";
	}

}
