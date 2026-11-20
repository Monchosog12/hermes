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
 * Servicio de lógica de negocio para la gestión y administración de usuarios.
 * <p>
 * El correo electrónico y el nombre de usuario se almacenan cifrados utilizando
 * {@link AESUtil}. Este servicio se encarga de cifrar los datos antes de
 * enviarlos al repositorio y de descifrarlos en las consultas, permitiendo que
 * los controladores operen siempre con texto plano.
 * </p>
 * <p>
 * Las búsquedas por nombre de usuario cifran el parámetro antes de consultar,
 * garantizando la consistencia con la base de datos cifrada.
 * </p>
 */
@Service
public class UsuarioService implements CRUDOperation<UsuarioDTO> {

	/** Repositorio para el acceso a datos de la entidad {@link Usuario}. */
	@Autowired
	private UsuarioRepository userRepo;

	/** Mapeador para la conversión entre entidades y DTOs. */
	@Autowired
	private ModelMapper mapper;

	/** Codificador para el almacenamiento seguro de contraseñas (BCrypt). */
	@Autowired
	private PasswordEncoder encoder;

	/**
	 * Crea un nuevo usuario en el sistema.
	 * <p>
	 * Cifra el correo y el nombre de usuario, codifica la contraseña y asigna un
	 * rol por defecto ("USER") en caso de que no se especifique ninguno.
	 * </p>
	 * 
	 * @param dato Objeto {@link UsuarioDTO} con la información del nuevo usuario.
	 * @return {@code 0} si la creación fue exitosa.
	 */
	public int crear(UsuarioDTO dato) {
		Usuario usuario = mapper.map(dato, Usuario.class);
		usuario.setEmail(cifrar(usuario.getEmail()));
		usuario.setUsername(cifrar(usuario.getUsername()));
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		// Si no se envia rol, se asigna USER por defecto (rol null rompe
		// getAuthorities())
		if (usuario.getRol() == null) {
			usuario.setRol(Usuario.Rol.USER);
		}
		userRepo.save(usuario);
		return 0;
	}

	/**
	 * Elimina un usuario del sistema buscando por su nombre de usuario.
	 * 
	 * @param username Nombre de usuario en texto plano.
	 * @return {@code 0} si fue eliminado con éxito; {@code 1} si el usuario no fue
	 *         encontrado.
	 */
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

	/**
	 * Obtiene una lista con todos los usuarios registrados en el sistema,
	 * descifrando sus datos sensibles a nivel de DTO para no comprometer la
	 * persistencia en la base de datos.
	 * 
	 * @return Lista de objetos {@link UsuarioDTO}.
	 */
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

	/**
	 * Actualiza los datos de un usuario existente identificado por su ID.
	 * 
	 * @param id        Identificador único del usuario a actualizar.
	 * @param nuevoDato Objeto {@link UsuarioDTO} con la nueva información.
	 * @return {@code 0} si la actualización fue exitosa; {@code 1} si el usuario no
	 *         existe o el nombre de usuario ya está ocupado.
	 */
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

	/**
	 * Comprueba si existe un usuario registrado con el ID especificado.
	 * 
	 * @param id Identificador único del usuario.
	 * @return {@code true} si el usuario existe; {@code false} en caso contrario.
	 */
	@Override
	public boolean exist(Long id) {
		if (userRepo.existsById(id)) {
			return true;
		}
		return false;
	}

	/**
	 * Elimina un usuario del sistema utilizando su identificador único.
	 * 
	 * @param id Identificador único del usuario.
	 * @return {@code 0} si fue eliminado con éxito; {@code 1} si el usuario no fue
	 *         encontrado.
	 */
	@Override
	public int eliminarPorId(Long id) {
		if (userRepo.existsById(id)) {
			userRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	/**
	 * Verifica si un nombre de usuario ya se encuentra registrado en el sistema.
	 *
	 * @param newUser Objeto {@link Usuario} que contiene el nombre de usuario a
	 *                verificar (en texto plano).
	 * @return {@code true} si el nombre de usuario ya está en uso; {@code false} en
	 *         caso contrario.
	 */
	public boolean findUsernameAlreadyTaken(Usuario newUser) {
		return findUsernameAlreadyTaken(newUser.getUsername());
	}

	/**
	 * Verifica si un nombre de usuario ya se encuentra registrado en el sistema.
	 *
	 * @param username Nombre de usuario a verificar (en texto plano).
	 * @return {@code true} si el nombre de usuario ya está en uso; {@code false} en
	 *         caso contrario.
	 */
	public boolean findUsernameAlreadyTaken(String username) {
		Optional<Usuario> found = userRepo.findByUsername(cifrar(username));
		return found.isPresent();
	}

	/**
	 * Valida las credenciales de acceso de un usuario.
	 *
	 * @param username Nombre de usuario (en texto plano).
	 * @param password Contraseña sin encriptar ingresada por el usuario.
	 * @return {@code 0} si las credenciales son válidas; {@code 1} si son inválidas
	 *         o el usuario no existe.
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

	/**
	 * Cifra una cadena de texto plano utilizando {@link AESUtil}.
	 * 
	 * @param texto Cadena en texto plano.
	 * @return Cadena cifrada, o {@code null} si el texto de entrada es nulo.
	 */
	private static String cifrar(String texto) {
		return texto == null ? null : AESUtil.encrypt(texto);
	}

	/**
	 * Descifra una cadena cifrada utilizando {@link AESUtil}.
	 * 
	 * @param cifrado Cadena cifrada.
	 * @return Cadena descifrada en texto plano, o {@code null} si la entrada es
	 *         nula.
	 */
	private static String descifrar(String cifrado) {
		return cifrado == null ? null : AESUtil.decrypt(cifrado);
	}
}
