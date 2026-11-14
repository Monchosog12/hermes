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

import co.edu.unbosque.hermes.dto.TraduccionDTO;
import co.edu.unbosque.hermes.dto.TraduccionRequest;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.service.CohereService;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/cohere")
@Transactional
@Tag(name = "Api cohere", description = "Endpoints para cohereIA")
public class ApiController {
	

	@Autowired
	private CohereService service;
	
	@PostMapping("/traduccion")
	ResponseEntity<TraduccionDTO>traduccion(@RequestBody TraduccionRequest req, @AuthenticationPrincipal Usuario user){
		TraduccionDTO respuesta = service.traducir(req, user);
		return new ResponseEntity<TraduccionDTO>(respuesta,  HttpStatus.ACCEPTED);
	}
	
	
	
}
