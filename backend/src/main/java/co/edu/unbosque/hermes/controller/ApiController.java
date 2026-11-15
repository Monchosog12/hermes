package co.edu.unbosque.hermes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.ChatResponseDTO;
import co.edu.unbosque.hermes.dto.RespuestaIADTO;
import co.edu.unbosque.hermes.dto.TraduccionDTO;
import co.edu.unbosque.hermes.dto.TraduccionRequestDTO;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.exception.IAServiceException;
import co.edu.unbosque.hermes.service.ExternalHTTPRequestHandler;
import co.edu.unbosque.hermes.service.TraduccionService;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/cohere")
@Transactional
@Tag(name = "Api cohere", description = "Endpoints para cohereIA")
public class ApiController {


	@Autowired
	private TraduccionService traduccionService;

	@PostMapping("/traduccion")
	ResponseEntity<TraduccionDTO>traduccion(@RequestBody TraduccionRequestDTO req, @AuthenticationPrincipal Usuario user){
		String json = ExternalHTTPRequestHandler.crearBodyTraduccion(req);
		ChatResponseDTO res = ExternalHTTPRequestHandler.doPostAndConvertToDto(ExternalHTTPRequestHandler.COHERE_URL, json);

		if (res.getStatuscode() == 429) {
			// 429: el modelo (normalmente el pool compartido de los modelos :free) está
			// saturado. No es culpa del cliente, por eso se responde 503 con Retry-After.
			throw new IAServiceException(HttpStatus.SERVICE_UNAVAILABLE,
					"El modelo de IA está saturado en este momento. Intenta de nuevo en unos minutos.",
					res.getRetryAfter() != null ? res.getRetryAfter() : "60", null);
		}
		if (res.getStatuscode() < 200 || res.getStatuscode() >= 300 || res.getPrimerContenido() == null) {
			throw new IAServiceException(HttpStatus.BAD_GATEWAY,
					"El proveedor de IA respondió con el código " + res.getStatuscode() + ".", null, null);
		}

		RespuestaIADTO respuesta = ExternalHTTPRequestHandler.parsearRespuesta(res.getPrimerContenido());

		boolean sinOrigen = req.getLenguajeOrigen() == null || req.getLenguajeOrigen().isBlank();
		TraduccionDTO dto = new TraduccionDTO(req.getCodigo(), respuesta.getCodigo(),
				// Si el usuario no indicó el lenguaje origen, se guarda el que detectó la IA
				sinOrigen ? respuesta.getLenguajeDetectado() : req.getLenguajeOrigen(), req.getLenguajeDestino(),
				ExternalHTTPRequestHandler.MODEL, null, user);
		dto.setMejoras(respuesta.getMejoras());

		TraduccionDTO guardada = traduccionService.guardar(dto);
		return new ResponseEntity<TraduccionDTO>(guardada,  HttpStatus.ACCEPTED);
	}



}
