package co.edu.unbosque.hermes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.hermes.entity.Traduccion;
import java.util.List;

/**
 * Repositorio de Spring Data JPA para la gestión y persistencia de la entidad {@link Traduccion}.
 * <p>
 * Permite realizar operaciones CRUD básicas heredadas de {@link JpaRepository} e incluye 
 * consultas especializadas para recuperar el historial de traducciones realizadas por los usuarios.
 * </p>
 * 
 */
public interface TraduccionRepository extends JpaRepository<Traduccion, Long>{

	/**
	 * Recupera una lista con todas las traducciones asociadas a un usuario específico, 
	 * ordenadas de manera descendente según su fecha de realización.
	 * 
	 * @param id Identificador único del usuario cuyas traducciones se desean consultar.
	 * @return Lista de objetos {@link Traduccion} ordenados de la más reciente a la más antigua.
	 */
	List<Traduccion> findByUsuarioIdOrderByFechaDesc(Long id);
	
}
