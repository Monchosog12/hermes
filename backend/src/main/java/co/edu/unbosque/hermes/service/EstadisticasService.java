package co.edu.unbosque.hermes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.edu.unbosque.hermes.dto.EstadisticasDTO;
import co.edu.unbosque.hermes.entity.Traduccion;
import co.edu.unbosque.hermes.repository.TraduccionRepository;
import co.edu.unbosque.hermes.repository.UsuarioRepository;

@Service
public class EstadisticasService {
	
	private UsuarioRepository usuarioRepo ;
	
	private TraduccionRepository traduccionRepo;
	
	
	public EstadisticasService() {
		// TODO Auto-generated constructor stub
	}



	public EstadisticasService(UsuarioRepository usuarioRepo, TraduccionRepository traduccionRepo) {
		this.usuarioRepo = usuarioRepo;
		this.traduccionRepo = traduccionRepo;
	}
	
	public EstadisticasDTO obtenerEstadisticas() {
		long totalUsuarios = usuarioRepo.count();
		long totalTraducciones= traduccionRepo.count();
		
		Map<String,Long> porLenguaje= new HashMap<>();
		List<Traduccion> traducciones = traduccionRepo.findAll();
		
		for(Traduccion t : traducciones) {
			String lenguaje = t.getLenguajeDestino();
			if(lenguaje == null || lenguaje.isBlank()) {
				lenguaje = "sin especificar";
			}
			long actual = porLenguaje.getOrDefault(lenguaje, 0l);
			porLenguaje.put(lenguaje, actual + 1);
		}
		return new EstadisticasDTO(totalUsuarios, totalTraducciones,porLenguaje);
	}
	
	
	

}
