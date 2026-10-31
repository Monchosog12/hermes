package co.edu.unbosque.hermes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.UsuarioDTO;
import co.edu.unbosque.hermes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:8080", "http://localhost:8081" })
@Transactional
@Tag(name = "Gestión de Usuarios", description = "Endpoints para administrar usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/createjson")
	ResponseEntity<String> createNewUser(
			@Parameter(description = "Datos del nuevo usuario", required = true, schema = @Schema(implementation = UsuarioDTO.class), examples = @ExampleObject(value = """
					{
						"username": "nuevoUsuario",
						"password": "contraseña123",
						"role": "ADMIN"
					}
					""")) @RequestBody UsuarioDTO newUser) {
		if (newUser.getUsername().contains("<") || newUser.getUsername().contains(">")) {
			return new ResponseEntity<String>("Solicitud con caracteres invalidos", HttpStatus.BAD_REQUEST);
		}
		int status = usuarioService.crear(newUser);
		if (status == 0) {
			return new ResponseEntity<String>("Usuario creado exitosamente", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<String>(
					"Error al crear el usuario, posiblemente el nombre de usuario ya está en uso",
					HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	
	@GetMapping("/getall")
	ResponseEntity<List<UsuarioDTO>>getAll(){
		List<UsuarioDTO>users = usuarioService.getAll();
		if(users.isEmpty()) {
			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
		}
	}
}
