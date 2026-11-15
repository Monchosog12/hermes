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
import co.edu.unbosque.hermes.util.AESUtil;

/**
 * El email y el username se guardan cifrados con {@link AESUtil}. Este service
 * cifra antes de llamar al repositorio y descifra lo que devuelve, así que los
 * controllers siempre trabajan con texto normal.
 *
 * Las búsquedas por username (findByUsername, searchByUsername...) funcionan
 * cifrando el parámetro antes de consultar: AESUtil usa un IV fijo, así que el
 * mismo texto siempre produce el mismo cifrado.
 */
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
		usuario.setEmail(cifrar(usuario.getEmail()));
		usuario.setUsername(cifrar(usuario.getUsername()));
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		// Si no se envia rol, se asigna USER por defecto (rol null rompe getAuthorities())
		if (usuario.getRol() == null) {
			usuario.setRol(Usuario.Rol.USER);
		}
		userRepo.save(usuario);
		return 0;
	}

	public int eliminarPorUsername(String username) {
		String usernameCifrado = cifrar(username);
		Optional<Usuario> usuario = userRepo.searchByUsername(usernameCifrado);
		if (usuario.isPresent()) {
			userRepo.deleteByUsername(usernameCifrado);
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
			// Se descifra el DTO, no la entidad: si se modificara la entidad dentro de
			// una transacción, Hibernate guardaría el texto plano en la base de datos.
			UsuarioDTO dto = mapper.map(entity, UsuarioDTO.class);
			dto.setEmail(descifrar(dto.getEmail()));
			dto.setUsername(descifrar(dto.getUsername()));
			dtoList.add(dto);
		});
		return dtoList;
	}

	@Override
	public int actualizarPorId(Long id, UsuarioDTO nuevoDato) {
		Optional<Usuario> found = userRepo.findById(id);
		Optional<Usuario> newFound = userRepo.searchByUsername(cifrar(nuevoDato.getUsername()));
		if (found.isPresent() && !newFound.isPresent()) {
			Usuario temp = found.get();
			temp.setUsername(cifrar(nuevoDato.getUsername()));
			temp.setEmail(cifrar(nuevoDato.getEmail()));
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
	 * @param newUser Usuario con el nombre de usuario a verificar (en texto normal)
	 * @return true si el nombre de usuario ya está en uso, false en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(Usuario newUser) {
		return findUsernameAlreadyTaken(newUser.getUsername());
	}

	/**
	 * Verifica si un nombre de usuario ya está en uso.
	 *
	 * @param username Nombre de usuario a verificar (en texto normal)
	 * @return true si el nombre de usuario ya está en uso, false en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(String username) {
		Optional<Usuario> found = userRepo.findByUsername(cifrar(username));
		return found.isPresent();
	}

	/**
	 * Valida las credenciales de un usuario.
	 *
	 * @param username Nombre de usuario (en texto normal)
	 * @param password Contraseña sin encriptar
	 * @return 0 si las credenciales son válidas, 1 si son inválidas
	 */
	public int validateCredentials(String username, String password) {
		// Buscar usuario por nombre de usuario
		Optional<Usuario> userOpt = userRepo.findByUsername(cifrar(username));

		// Verificar si el usuario existe y la contraseña coincide
		if (userOpt.isPresent()) {
			Usuario user = userOpt.get();
			if (encoder.matches(password, user.getPassword())) {
				return 0; // Éxito
			}
		}
		return 1; // Credenciales inválidas
	}

	private static String cifrar(String texto) {
		return texto == null ? null : AESUtil.encrypt(texto);
	}

	private static String descifrar(String cifrado) {
		return cifrado == null ? null : AESUtil.decrypt(cifrado);
	}
}
