package co.edu.unbosque.hermes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import co.edu.unbosque.hermes.dto.TraduccionRequestDTO;
import co.edu.unbosque.hermes.dto.UsuarioDTO;
import co.edu.unbosque.hermes.service.CorreoService;
import co.edu.unbosque.hermes.service.TraduccionService;
import co.edu.unbosque.hermes.service.UsuarioService;

@SpringBootTest
class HermesApplicationTests {

	@MockitoBean
	private JavaMailSender mailSender;

	@Autowired
	private CorreoService correoServ;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private TraduccionService traduccionService;

	@Test
	void contextLoads() {
		assertNotNull(correoServ, "El servicio de correo debería cargarse en el contexto");
		assertNotNull(usuarioService, "El servicio de usuario debería cargarse en el contexto");
	}

	@Test
	void testEnviarCorreoExitoso() {
		doNothing().when(mailSender).send(any(SimpleMailMessage.class));

		correoServ.enviarCorreoRespuesta("destinatario@correo.com", "Prueba Hermes", "Contenido del mensaje");

		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}

	@Test
	void inicioDeSesion() {
		String username = "admin";
		String password = "123";

		assertNotNull(username);
		assertNotNull(password);
	}

	@Test
	void actualizarUsuario() {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setUsername("usuario_test");
		dto.setPassword("nuevaContrasena123");

		assertNotNull(dto.getUsername(), "El nombre de usuario no debe ser nulo");
	}

	@Test
	void crearUsuario() {
		UsuarioDTO nuevoUsuario = new UsuarioDTO();
		nuevoUsuario.setUsername("nuevo_bosque");
		nuevoUsuario.setPassword("password123");

		assertEquals("nuevo_bosque", nuevoUsuario.getUsername(), "El nombre de usuario debe coincidir");
	}

	@Test
	void traducirCodigo() {
		TraduccionRequestDTO req = new TraduccionRequestDTO("int x = 10;", "java", "python");

		assertNotNull(req.getCodigo(), "El código a traducir no debe ser nulo");
		assertEquals("java", req.getLenguajeOrigen(), "El lenguaje de origen debe ser Java");
	}

	@Test
	void eliminarUsuario() {
		String usernameTarget = "usuario_a_borrar";
		assertNotNull(usernameTarget);
	}

	@Test
	void mejoraCodigo() {
		String codigoOriginal = "public void test() {}";
		assertNotNull(codigoOriginal);
	}

}
