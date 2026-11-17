package co.edu.unbosque.hermes.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
import co.edu.unbosque.hermes.exception.IAServiceException;
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
	 * Usa las tres IA al mismo tiempo: las llamadas a Cohere, Qwen y particle se
	 * lanzan en paralelo y se devuelven las tres traducciones. Si una falla, las
	 * otras se devuelve igual y la que falló trae el motivo del error. Ruta: POST
	 * /ia/traduccion
	 */
	@PostMapping("/ia/traduccion")
	ResponseEntity<Map<String, Object>> traduccionAmbas(@RequestBody TraduccionRequestDTO req,
			@AuthenticationPrincipal Usuario user) {

		CompletableFuture<RespuestaIADTO> cohere = CompletableFuture
				.supplyAsync(() -> ExternalHTTPRequestHandler.traducir(req, ExternalHTTPRequestHandler.COHERE_URL,
						ExternalHTTPRequestHandler.MODEL, ExternalHTTPRequestHandler.getApiKey()));
		CompletableFuture<RespuestaIADTO> qwen = CompletableFuture
				.supplyAsync(() -> ExternalHTTPRequestHandler.traducir(req, ExternalHTTPRequestHandler.NEMOTRON_URL,
						ExternalHTTPRequestHandler.NEMOTRON_MODEL, ExternalHTTPRequestHandler.getApiKeyNemotron()));
		CompletableFuture<RespuestaIADTO> particle = CompletableFuture
				.supplyAsync(() -> ExternalHTTPRequestHandler.traducir(req, ExternalHTTPRequestHandler.PARTICLE_URL,
						ExternalHTTPRequestHandler.PARTICLE_MODEL, ExternalHTTPRequestHandler.getApiKeyParticle()));

		Map<String, Object> resultado = new LinkedHashMap<>();
		resultado.put("cohere", resolver(cohere, req, user, ExternalHTTPRequestHandler.MODEL));
		resultado.put("qwen", resolver(qwen, req, user, ExternalHTTPRequestHandler.NEMOTRON_MODEL));
		resultado.put("particle", resolver(particle, req, user, ExternalHTTPRequestHandler.PARTICLE_MODEL));

		boolean alguna = resultado.values().stream().anyMatch(v -> v instanceof TraduccionDTO);
		return new ResponseEntity<Map<String, Object>>(resultado,
				alguna ? HttpStatus.ACCEPTED : HttpStatus.BAD_GATEWAY);
	}

	/** Espera la respuesta de una IA y la guarda. si falló, devuelve el error. */
	private Object resolver(CompletableFuture<RespuestaIADTO> futuro, TraduccionRequestDTO req, Usuario user,
			String modelo) {
		try {
			return guardarTraduccion(req, user, modelo, futuro.join());
		} catch (CompletionException e) {
			Throwable causa = e.getCause() != null ? e.getCause() : e;
			Map<String, Object> error = new LinkedHashMap<>();
			error.put("modelo", modelo);
			error.put("error", causa.getMessage());
			if (causa instanceof IAServiceException ia) {
				error.put("status", ia.getStatus().value());
			}
			return error;
		}
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
