package co.edu.unbosque.hermes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.hermes.entity.Traduccion;
import java.util.List;



public interface TraduccionRepository extends JpaRepository<Traduccion, Long>{

	List<Traduccion> findByUsuarioIdOrderByFechaDesc(Long id);
	
}
