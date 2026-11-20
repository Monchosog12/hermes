package co.edu.unbosque.hermes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.CorreoDTO;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.service.CorreoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST encargado de gestionar los endpoints relacionados con el
 * envío de correos electrónicos a los usuarios de la plataforma Hermes.
 */
@RestController
@RequestMapping("/correo")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" })
@Tag(name = "Gestión de Correo", description = "Endpoints para enviar correos electrónicos")
public class CorreoController {

	@Autowired
	private CorreoService correoServ;

	/**
	 * Endpoint HTTP POST para enviar un correo electrónico utilizando los datos
	 * encapsulados en un {@link CorreoDTO}.
	 * 
	 * @param emailDTO Objeto que contiene el destinatario, asunto y mensaje del correo.
	 * @return Una respuesta HTTP indicando el éxito o el fallo de la operación.
	 */
	@PostMapping("/enviar")
	public ResponseEntity<String> enviarCorreo(@RequestBody CorreoDTO emailDTO,
			@AuthenticationPrincipal Usuario user) {

		// El destinatario siempre es el correo con el que se registró el usuario
		// autenticado (ya viene descifrado desde UserDetailsServiceImpl). Se ignora
		// cualquier destinatario enviado por el cliente.
		String destinatario = user == null ? null : user.getEmail();
		if (destinatario == null || destinatario.isBlank()) {
			return new ResponseEntity<String>("El usuario no tiene un correo registrado",
					HttpStatus.BAD_REQUEST);
		}

		try {
			correoServ.enviarCorreoRespuesta(destinatario, emailDTO.getAsunto(), emailDTO.getMensaje());
			return new ResponseEntity<String>("Correo enviado exitosamente", HttpStatus.ACCEPTED);
		} catch (Exception e) {
			// Se busca la causa raíz para mostrar la respuesta real del servidor SMTP
			// (ej. "535 5.7.8 Authentication failed" o "525 5.7.1 Unauthorized IP address").
			Throwable causa = e;
			while (causa.getCause() != null && causa.getCause() != causa) {
				causa = causa.getCause();
			}
			String detalle = causa == e ? e.getMessage() : e.getMessage() + " -> " + causa.getMessage();
			return new ResponseEntity<String>("Error al enviar el correo: " + detalle,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
