package co.edu.unbosque.hermes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.hermes.entity.Usuario;
import java.util.Optional;
import java.util.List;
import co.edu.unbosque.hermes.entity.Usuario.Rol;



public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> searchByUsername(String username);
	Optional<List<Usuario>> searchByRol(Rol rol);
	void deleteByUsername(String username);
}
