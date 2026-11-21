package co.edu.unbosque.hermes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.UsuarioDTO;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.service.UsuarioService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST encargado de gestionar las operaciones CRUD y de
 * administración para los usuarios .
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" })
@Transactional
@Tag(name = "Gestión de Usuarios", description = "Endpoints para administrar usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

	/**
	 * Servicio para manejar la logica de negocio relacionado con los usuarios.
	 */

	@Autowired
	private UsuarioService usuarioService;

	/**
	 * Crea un nuevo usuario en el sistema a partir de un objeto JSON recibido en el
	 * cuerpo de la petición.
	 * 
	 * @param newUser Objeto {@link UsuarioDTO} con la información del usuario a
	 *                registrar.
	 * @return ResponseEntity con un mensaje indicando el resultado de la operación
	 *         y su código HTTP.
	 * 
	 */
	@PostMapping("/createjson")
	ResponseEntity<String> createNewUser(
			@Parameter(description = "Datos del nuevo usuario", required = true, schema = @Schema(implementation = UsuarioDTO.class), examples = @ExampleObject(value = """
					{
						"email": "nuevoUsuario@correo.com",
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
	
	/**
	 * Actualiza la información de un usuario existente buscando por su identificador único.
	 * Ruta: POST /user/updateuser/{id}
	 *
	 * @param id      Identificador único del usuario a actualizar.
	 * @param newUser Objeto {@link UsuarioDTO} con los nuevos datos. La contraseña es
	 *                opcional: si viene vacía se conserva la actual.
	 * @return ResponseEntity indicando si la actualización fue exitosa o si ocurrió un error.
	 */
	@PostMapping("/updateuser/{id}")
	ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UsuarioDTO newUser) {
		if (newUser.getUsername() == null || newUser.getUsername().isBlank() || newUser.getEmail() == null
				|| newUser.getEmail().isBlank()) {
			return new ResponseEntity<String>("El nombre de usuario y el correo son obligatorios",
					HttpStatus.BAD_REQUEST);
		}
		if (newUser.getUsername().contains("<") || newUser.getUsername().contains(">")) {
			return new ResponseEntity<String>("Solicitud con caracteres invalidos", HttpStatus.BAD_REQUEST);
		}
		newUser.setUsername(newUser.getUsername().trim());
		newUser.setEmail(newUser.getEmail().trim());

		int status = usuarioService.actualizarPorId(id, newUser);
		if (status == 0) {
			return new ResponseEntity<String>("Usuario actualizado correctamente", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<String>("El usuario no existe", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>("El nombre de usuario ya está en uso por otra cuenta",
					HttpStatus.CONFLICT);
		}
	}

	/**
	 * Elimina un usuario del sistema basándose en su identificador único, junto con
	 * su historial de traducciones. Ruta: DELETE /user/deletebyid/{id}
	 *
	 * @param id         Identificador único del usuario que se desea eliminar.
	 * @param autenticado Administrador que realiza la petición (no puede eliminarse a sí mismo).
	 * @return ResponseEntity con el estado de la eliminación.
	 */
	@DeleteMapping("/deletebyid/{id}")
	ResponseEntity<String> deleteById(@PathVariable Long id, @AuthenticationPrincipal Usuario autenticado) {
		if (autenticado != null && id.equals(autenticado.getId())) {
			return new ResponseEntity<String>("No puedes eliminar tu propia cuenta", HttpStatus.BAD_REQUEST);
		}
		int status = usuarioService.eliminarPorId(id);
		if (status == 0) {
			return new ResponseEntity<String>("Usuario eliminado correctamente", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<String>("El usuario no existe", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getall")
	ResponseEntity<List<UsuarioDTO>> getAll() {
		List<UsuarioDTO> users = usuarioService.getAll();
		if (users.isEmpty()) {
			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
		}
	}
}
