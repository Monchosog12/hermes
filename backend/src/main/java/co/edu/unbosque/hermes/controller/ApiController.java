package co.edu.unbosque.hermes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.RespuestaIADTO;
import co.edu.unbosque.hermes.dto.TraduccionDTO;
import co.edu.unbosque.hermes.dto.TraduccionRequestDTO;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.service.ExternalHTTPRequestHandler;
import co.edu.unbosque.hermes.service.TraduccionService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Transactional
@Tag(name = "Api IA", description = "Endpoints para Cohere IA, Nemotron IA y Particle IA")
public class ApiController {

	@Autowired
	private TraduccionService traduccionService;

	/** Traducción solo con Cohere. Ruta: POST /cohere/traduccion */
	@PostMapping("/cohere/traduccion")
	ResponseEntity<TraduccionDTO> traduccion(@RequestBody TraduccionRequestDTO req,
			@AuthenticationPrincipal Usuario user) {
		RespuestaIADTO respuesta = ExternalHTTPRequestHandler.traducir(req, ExternalHTTPRequestHandler.COHERE_URL,
				ExternalHTTPRequestHandler.MODEL, ExternalHTTPRequestHandler.getApiKey());
		return new ResponseEntity<TraduccionDTO>(
				guardarTraduccion(req, user, ExternalHTTPRequestHandler.MODEL, respuesta), HttpStatus.ACCEPTED);
	}

	/** Traducción solo con Nemotron. Ruta: POST /nemotron/traduccion */
	@PostMapping("/nemotron/traduccion")
	ResponseEntity<TraduccionDTO> traduccionNemotron(@RequestBody TraduccionRequestDTO req,
			@AuthenticationPrincipal Usuario user) {
		RespuestaIADTO respuesta = ExternalHTTPRequestHandler.traducir(req, ExternalHTTPRequestHandler.NEMOTRON_URL,
				ExternalHTTPRequestHandler.NEMOTRON_MODEL, ExternalHTTPRequestHandler.getApiKey());
		return new ResponseEntity<TraduccionDTO>(
				guardarTraduccion(req, user, ExternalHTTPRequestHandler.NEMOTRON_MODEL, respuesta),
				HttpStatus.ACCEPTED);
	}

	/** Traducción solo con Particle-1 Coder. Ruta: POST /particle/traduccion */
	@PostMapping("/particle/traduccion")
	ResponseEntity<TraduccionDTO> traduccionParticle(@RequestBody TraduccionRequestDTO req,
			@AuthenticationPrincipal Usuario user) {
		RespuestaIADTO respuesta = ExternalHTTPRequestHandler.traducir(req, ExternalHTTPRequestHandler.PARTICLE_URL,
				ExternalHTTPRequestHandler.PARTICLE_MODEL, ExternalHTTPRequestHandler.getApiKeyParticle());
		return new ResponseEntity<TraduccionDTO>(
				guardarTraduccion(req, user, ExternalHTTPRequestHandler.PARTICLE_MODEL, respuesta),
				HttpStatus.ACCEPTED);
	}

	/**
	 * Guarda la traducción cifrada en el historial y devuelve el DTO con texto
	 * normal.
	 */
	private TraduccionDTO guardarTraduccion(TraduccionRequestDTO req, Usuario user, String modelo,
			RespuestaIADTO respuesta) {
		boolean sinOrigen = req.getLenguajeOrigen() == null || req.getLenguajeOrigen().isBlank();
		TraduccionDTO dto = new TraduccionDTO(req.getCodigo(), respuesta.getCodigo(),
				sinOrigen ? respuesta.getLenguajeDetectado() : req.getLenguajeOrigen(), req.getLenguajeDestino(),
				modelo, null, user);
		dto.setMejoras(respuesta.getMejoras());
		return traduccionService.guardar(dto);
	}
}
