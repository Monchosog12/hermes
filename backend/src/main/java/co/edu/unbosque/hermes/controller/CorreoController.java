package co.edu.unbosque.hermes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.CorreoDTO;
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
	public ResponseEntity<String> enviarCorreo(@RequestBody CorreoDTO emailDTO) {

		if (emailDTO.getDestinatario() == null || emailDTO.getDestinatario().isBlank()) {
			return new ResponseEntity<String>("El correo del destinatario no puede estar vacío",
					HttpStatus.BAD_REQUEST);
		}

		try {
			correoServ.enviarCorreoRespuesta(emailDTO.getDestinatario(), emailDTO.getAsunto(), emailDTO.getMensaje());
			return new ResponseEntity<String>("Correo enviado exitosamente", HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<String>("Error al enviar el correo: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
