package co.edu.unbosque.hermes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.hermes.dto.TraduccionDTO;
import co.edu.unbosque.hermes.entity.Traduccion;
import co.edu.unbosque.hermes.repository.TraduccionRepository;
import co.edu.unbosque.hermes.util.AESUtil;
import jakarta.annotation.PostConstruct;

@Service
public class TraduccionService implements CRUDOperation<TraduccionDTO> {

	@Autowired
	private TraduccionRepository repo;
	@Autowired
	private ModelMapper mapper;

	public TraduccionService() {

	}

	@Override
	public int crear(TraduccionDTO dato) {
		guardar(dato);
		return 0;
	}

	/**
	 * Guarda la traducción con el código original y el traducido cifrados con
	 * {@link AESUtil}. Devuelve el DTO con el texto normal.
	 */
	public TraduccionDTO guardar(TraduccionDTO dato) {
		Traduccion entity = mapper.map(dato, Traduccion.class);
		entity.setCodigoOriginal(cifrar(dato.getCodigoOriginal()));
		entity.setCodigoTraducido(cifrar(dato.getCodigoTraducido()));
		return aDtoDescifrado(repo.save(entity));
	}

	/** Traducciones de un usuario, de la más reciente a la más antigua. */
	public List<TraduccionDTO> historial(Long userId) {
		List<Traduccion> entityList = repo.findByUsuarioIdOrderByFechaDesc(userId);
		List<TraduccionDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			dtoList.add(aDtoDescifrado(entity));
		});
		return dtoList;
	}

	@Override
	public List<TraduccionDTO> getAll() {
		List<Traduccion> entityList = repo.findAll();
		List<TraduccionDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			dtoList.add(aDtoDescifrado(entity));
		});
		return dtoList;
	}

	@Override
	public int eliminarPorId(Long id) {
		Optional<Traduccion> found = repo.findById(id);
		if (found.isPresent()) {
			repo.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int actualizarPorId(Long id, TraduccionDTO nuevoDato) {
		Optional<Traduccion> found = repo.findById(id);
		if (found.isPresent()) {
			Traduccion entity = found.get();
			entity.setCodigoOriginal(cifrar(nuevoDato.getCodigoOriginal()));
			entity.setCodigoTraducido(cifrar(nuevoDato.getCodigoTraducido()));
			entity.setLenguajeOrigen(nuevoDato.getLenguajeOrigen());
			entity.setLenguajeDestino(nuevoDato.getLenguajeDestino());
			entity.setMejoras(mejorasATexto(nuevoDato.getMejoras()));
			entity.setModelo(nuevoDato.getModelo());
			repo.save(entity);
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public boolean exist(Long id) {
		return repo.existsById(id) ? true : false;
	}

	private String mejorasATexto(List<String> mejoras) {
		return mejoras == null ? "" : String.join("\n", mejoras);
	}

	/**
	 * Convierte la entidad en DTO y descifra el DTO, no la entidad: si se
	 * modificara la entidad dentro de una transacción, Hibernate guardaría el
	 * texto plano en la base de datos.
	 */
	private TraduccionDTO aDtoDescifrado(Traduccion entity) {
		TraduccionDTO dto = mapper.map(entity, TraduccionDTO.class);
		dto.setCodigoOriginal(descifrar(entity.getCodigoOriginal()));
		dto.setCodigoTraducido(descifrar(entity.getCodigoTraducido()));
		return dto;
	}

	private static String cifrar(String texto) {
		return texto == null ? null : AESUtil.encrypt(texto);
	}

	private static String descifrar(String cifrado) {
		return cifrado == null ? null : AESUtil.decrypt(cifrado);
	}
}
