package co.edu.unbosque.hermes.configuration;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.LoggerFactory;

import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.repository.UsuarioRepository;
import co.edu.unbosque.hermes.util.AESUtil;

@Configuration
public class LoadDataBase {

	private static final Logger log = LoggerFactory.getLogger(LoadDataBase.class);

	@Bean
	CommandLineRunner initDatabase(UsuarioRepository userRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			asegurarUsuario(userRepo, passwordEncoder, "admin", "admin@hermes.com", "12345678", Usuario.Rol.ADMIN);
			asegurarUsuario(userRepo, passwordEncoder, "normaluser", "normaluser@hermes.com", "1234567890",
					Usuario.Rol.USER);
		};
	}

	/**
	 * Garantiza que el usuario precargado exista con usuario y correo cifrados, igual
	 * que los usuarios creados desde el registro (el login busca por el username
	 * cifrado).
	 * <ul>
	 * <li>Si ya existe cifrado, no hace nada.</li>
	 * <li>Si existe sin cifrar (versiones anteriores de esta clase), le cifra
	 * usuario y correo conservando id, contraseña y rol.</li>
	 * <li>Si no existe, lo crea cifrado.</li>
	 * </ul>
	 */
	private static void asegurarUsuario(UsuarioRepository userRepo, PasswordEncoder passwordEncoder, String username,
			String email, String password, Usuario.Rol rol) {
		String usernameCifrado = AESUtil.encrypt(username);

		if (userRepo.findByUsername(usernameCifrado).isPresent()) {
			log.info("El usuario '{}' ya existe, omitiendo su creación", username);
			return;
		}

		Optional<Usuario> sinCifrar = userRepo.findByUsername(username);
		if (sinCifrar.isPresent()) {
			Usuario usuario = sinCifrar.get();
			usuario.setUsername(usernameCifrado);
			usuario.setEmail(AESUtil.encrypt(usuario.getEmail() != null ? usuario.getEmail() : email));
			userRepo.save(usuario);
			log.info("Usuario '{}' migrado: usuario y correo ahora están cifrados", username);
			return;
		}

		Usuario nuevo = new Usuario(AESUtil.encrypt(email), usernameCifrado, passwordEncoder.encode(password), rol);
		userRepo.save(nuevo);
		log.info("Precargando usuario '{}'", username);
	}
}
