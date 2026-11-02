package co.edu.unbosque.hermes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.hermes.dto.UsuarioDTO;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.repository.UsuarioRepository;
import co.edu.unbosque.springfirstapp.model.User;

@Service
public class UsuarioService implements CRUDOperation<UsuarioDTO> {

	@Autowired
	private UsuarioRepository userRepo;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private PasswordEncoder encoder;
	
	public int crear(UsuarioDTO dato) {
		Usuario usuario = mapper.map(dato, Usuario.class);
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		if (usuario.getRol() != null) {
			usuario.setRol(dato.getRol());
		}
		userRepo.save(usuario);
		return 0;
	}

	@Override
	public int eliminarPorUsername(String username) {
		Optional<Usuario> usuario = userRepo.searchByUsername(username);
		if (usuario.isPresent()) {
			userRepo.deleteByUsername(username);
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public List<UsuarioDTO> getAll() {
		List<Usuario> entityList = userRepo.findAll();
		List<UsuarioDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			UsuarioDTO dto = mapper.map(entity, UsuarioDTO.class);
			dtoList.add(dto);
		});
		return dtoList;
	}
	
	@Override
	public int actualizarUsuarioPorId(Long id, UsuarioDTO nuevoDato) {
		Optional<Usuario> found = userRepo.findById(id);
		Optional<Usuario> newFound = userRepo.searchByUsername(nuevoDato.getUsername());
		if (found.isPresent() && !newFound.isPresent()) {
			Usuario temp = found.get();
			temp.setUsername(nuevoDato.getUsername());
			temp.setEmail(nuevoDato.getEmail());
			temp.setPassword(encoder.encode(nuevoDato.getPassword()));
			if (nuevoDato.getRol() != null) {
				temp.setRol(nuevoDato.getRol());
			}
			userRepo.save(temp);
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public boolean exist(Long id) {
		if (userRepo.existsById(id)) {
			return true;
		}
		return false;
	}

	@Override
	public int eliminarPorId(Long id) {
		if(userRepo.existsById(id)) {
			userRepo.deleteById(id);
			return 0;
		}
		return 1;
	}
	
	/**
	 * Verifica si un nombre de usuario ya está en uso.
	 *
	 * @param newUser Usuario con el nombre de usuario a verificar
	 * @return true si el nombre de usuario ya está en uso, false en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(User newUser) {
		Optional<Usuario> found = userRepo.findByUsername(newUser.getUsername());
		if (found.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verifica si un nombre de usuario ya está en uso.
	 *
	 * @param username Nombre de usuario a verificar
	 * @return true si el nombre de usuario ya está en uso, false en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(String username) {
		Optional<Usuario> found = userRepo.findByUsername(username);
		return found.isPresent();
	}

	/**
	 * Valida las credenciales de un usuario.
	 *
	 * @param username Nombre de usuario
	 * @param password Contraseña sin encriptar
	 * @return 0 si las credenciales son válidas, 1 si son inválidas
	 */
	public int validateCredentials(String username, String password) {
		// Buscar usuario por nombre de usuario
		Optional<Usuario> userOpt = userRepo.findByUsername(username);

		// Verificar si el usuario existe y la contraseña coincide
		if (userOpt.isPresent()) {
			Usuario user = userOpt.get();
			if (encoder.matches(password, user.getPassword())) {
				return 0; // Éxito
			}
		}
		return 1; // Credenciales inválidas
	}
}