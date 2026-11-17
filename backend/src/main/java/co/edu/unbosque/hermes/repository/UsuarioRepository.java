package co.edu.unbosque.hermes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.hermes.entity.Usuario;
import java.util.Optional;
import java.util.List;
import co.edu.unbosque.hermes.entity.Usuario.Rol;

/**
 * Repositorio de Spring Data JPA para la gestión y persistencia de la entidad {@link Usuario}.
 * <p>
 * Proporciona métodos CRUD estándar heredados de {@link JpaRepository}, así como consultas personalizadas 
 * para la búsqueda y eliminación de usuarios por su nombre de usuario o su rol asignado.
 * </p>
 * 
 * @author Universidad El Bosque - Hermes
 * @version 1.0
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	/**
	 * Busca un usuario por su nombre de usuario.
	 * 
	 * @param username Nombre de usuario a buscar (generalmente almacenado de forma cifrada).
	 * @return Un objeto {@link Optional} que contiene el usuario si es encontrado, o vacío en caso contrario.
	 */
	Optional<Usuario> searchByUsername(String username);
	
	/**
	 * Busca una lista de usuarios asociados a un rol específico dentro del sistema.
	 * 
	 * @param rol Objeto {@link Rol} por el cual se filtrarán los usuarios.
	 * @return Un {@link Optional} que contiene una lista con los usuarios que poseen dicho rol.
	 */
	Optional<List<Usuario>> searchByRol(Rol rol);
	
	/**
	 * Elimina un registro de usuario de la base de datos basándose en su nombre de usuario.
	 * 
	 * @param username Nombre de usuario del registro que se desea eliminar.
	 */
	void deleteByUsername(String username);
	
	/**
	 * Encuentra un usuario utilizando su nombre de usuario.
	 * 
	 * @param username Nombre de usuario a consultar.
	 * @return Un objeto {@link Optional} con el usuario correspondiente.
	 */
	Optional<Usuario> findByUsername(String username);
}
