package co.edu.unbosque.hermes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.hermes.dto.EstadisticasDTO;
import co.edu.unbosque.hermes.service.EstadisticasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST que expone los endpoints para consultar las estadísticas de
 * uso y registros de la plataforma Hermes.
 */
@RestController
@RequestMapping("/estadisticas")
@Tag(name = "Estadísticas", description = "Endpoints para métricas de usuarios y uso de traducciones")
public class EstadisticasController {

	/** Servicio de negocio para el cálculo de estadísticas. */
	private EstadisticasService estadisticaServ;

	/**
	 * Constructor que inyecta el servicio de estadísticas.
	 * 
	 * @param estadisticasService Servicio de estadísticas.
	 */
	public EstadisticasController(EstadisticasService estadisticaServ) {
		this.estadisticaServ = estadisticaServ;
	}

	@Operation(summary = "Obtener estadísticas generales", description = "Devuelve el total de usuarios registrados, el total de traducciones y el desglose por lenguajes.")
	@GetMapping
	public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {
		return ResponseEntity.ok(estadisticaServ.obtenerEstadisticas());

	}

}
