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

@Configuration
public class LoadDataBase {

	private static final Logger log = LoggerFactory.getLogger(LoadDataBase.class);

	@Bean
	CommandLineRunner initDatabase(UsuarioRepository userRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			Optional<Usuario> found = userRepo.findByUsername("admin");
			if (found.isPresent()) {
				log.info("El administrador ya existe, omitiendo la creación del administrador");
			} else {
				Usuario adminUser = new Usuario("admin", passwordEncoder.encode("12345678"), Usuario.Rol.ADMIN);
				userRepo.save(adminUser);
				log.info("Precargando usuario administrador");
			}
			Optional<Usuario> found2 = userRepo.findByUsername("normaluser");
			if (found2.isPresent()) {
				log.info("El usuario normal ya existe, omitiendo la creación del usuario normal...");
			} else {
				Usuario normalUser = new Usuario("normaluser", passwordEncoder.encode("1234567890"), Usuario.Rol.USER);
				userRepo.save(normalUser);
				log.info("Precargando usuario normal");
			}
		};
	}

}
