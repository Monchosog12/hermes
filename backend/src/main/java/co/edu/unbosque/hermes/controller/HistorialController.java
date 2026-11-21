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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.HistorialDTO;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.service.TraduccionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST del historial de traducciones del usuario autenticado.
 * <p>
 * Todas las operaciones se limitan a las traducciones del usuario dueño del
 * token: no es posible consultar, modificar ni eliminar las de otra persona.
 * </p>
 */
@RestController
@RequestMapping("/historial")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" })
@Transactional
@Tag(name = "Historial", description = "Historial de traducciones del usuario autenticado")
@SecurityRequirement(name = "bearerAuth")
public class HistorialController {

	@Autowired
	private TraduccionService traduccionService;

	/**
	 * Lista las traducciones del usuario autenticado, de la más reciente a la más
	 * antigua. Ruta: GET /historial
	 */
	@GetMapping
	public ResponseEntity<List<HistorialDTO>> listar(@AuthenticationPrincipal Usuario user) {
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return ResponseEntity.ok(traduccionService.historialDeUsuario(user.getId()));
	}

	/**
	 * Modifica los códigos y lenguajes de una traducción propia. Ruta: PUT
	 * /historial/{id}
	 */
	@PutMapping("actualizar/{id}")
	public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody HistorialDTO dato,
			@AuthenticationPrincipal Usuario user) {
		if (user == null) {
			return new ResponseEntity<>("Debes iniciar sesión", HttpStatus.UNAUTHORIZED);
		}
		if (dato.getCodigoOriginal() == null || dato.getCodigoOriginal().isBlank()) {
			return new ResponseEntity<>("El código original es obligatorio", HttpStatus.BAD_REQUEST);
		}
		if (dato.getLenguajeDestino() == null || dato.getLenguajeDestino().isBlank()) {
			return new ResponseEntity<>("El lenguaje de destino es obligatorio", HttpStatus.BAD_REQUEST);
		}

		int status = traduccionService.actualizarDeUsuario(id, user.getId(), dato);
		if (status == 0) {
			return new ResponseEntity<>("Traducción actualizada correctamente", HttpStatus.OK);
		}
		return new ResponseEntity<>("La traducción no existe", HttpStatus.NOT_FOUND);
	}

	/** Elimina una traducción propia. Ruta: DELETE /historial/{id} */
	@DeleteMapping("eliminar/{id}")
	public ResponseEntity<String> eliminar(@PathVariable Long id, @AuthenticationPrincipal Usuario user) {
		if (user == null) {
			return new ResponseEntity<>("Debes iniciar sesión", HttpStatus.UNAUTHORIZED);
		}
		int status = traduccionService.eliminarDeUsuario(id, user.getId());
		if (status == 0) {
			return new ResponseEntity<>("Traducción eliminada correctamente", HttpStatus.OK);
		}
		return new ResponseEntity<>("La traducción no existe", HttpStatus.NOT_FOUND);
	}
}
