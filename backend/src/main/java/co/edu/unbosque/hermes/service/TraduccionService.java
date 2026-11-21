package co.edu.unbosque.hermes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.hermes.dto.HistorialDTO;
import co.edu.unbosque.hermes.dto.TraduccionDTO;
import co.edu.unbosque.hermes.entity.Traduccion;
import co.edu.unbosque.hermes.repository.TraduccionRepository;
import co.edu.unbosque.hermes.util.AESUtil;
import jakarta.annotation.PostConstruct;

/**
 * Servicio encargado de gestionar la lógica de negocio para al traduccion de
 * código. Implementa la interfaz{@link CRUDOperation} para operaciones sobre
 * {@link TraduccionDTO} y maneja el cifrado/descifrado seguro del codigo
 * medinte {@link AESUtil
 **/
@Service
public class TraduccionService implements CRUDOperation<TraduccionDTO> {

	@Autowired
	private TraduccionRepository repo;
	@Autowired
	private ModelMapper mapper;

	/**
	 * Constructor vacio de la clase TraduccionService.
	 */
	public TraduccionService() {

	}

	/**
	 * {@inheritDoc} Permite crear una nueva traduccion guardandola a travez del
	 * método de persistencia.
	 * 
	 * @param dato Objeto DTO que contien la informacion de la traducción a
	 *             registrar.
	 * @return Un valor entero indicador de éxito (0 por defecto).
	 */
	@Override
	public int crear(TraduccionDTO dato) {
		guardar(dato);
		return 0;
	}

	/**
	 * Guarda la traducción con el código original y el traducido cifrados con
	 * {@link AESUtil}. Devuelve el DTO con el texto normal.
	 * 
	 * @param dato el DTO con los datos de la traduccion en texto plano
	 * @return el {@link TreduccionDTO} procesado y listo.
	 */
	public TraduccionDTO guardar(TraduccionDTO dato) {
		Traduccion entity = mapper.map(dato, Traduccion.class);
		entity.setCodigoOriginal(cifrar(dato.getCodigoOriginal()));
		entity.setCodigoTraducido(cifrar(dato.getCodigoTraducido()));
		return aDtoDescifrado(repo.save(entity));
	}

	/**
	 * Obtiene el historial de traducciones realizadas por un usuario e ordena desde
	 * la más reciente hasta las más antigua.
	 * 
	 * @param userId identificador único del usuario
	 * @return Una lista de {@link TraduccionDTO} con los datos descifrados.
	 */
	public List<TraduccionDTO> historial(Long userId) {
		List<Traduccion> entityList = repo.findByUsuarioIdOrderByFechaDesc(userId);
		List<TraduccionDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			dtoList.add(aDtoDescifrado(entity));
		});
		return dtoList;
	}

	/**
	 * Obtiene el historial de un usuario, de la traducción más reciente a la más
	 * antigua, con los códigos descifrados y sin incluir los datos del usuario.
	 *
	 * @param userId Identificador del usuario autenticado.
	 * @return Lista de {@link HistorialDTO}.
	 */
	public List<HistorialDTO> historialDeUsuario(Long userId) {
		List<HistorialDTO> lista = new ArrayList<>();
		repo.findByUsuarioIdOrderByFechaDesc(userId).forEach((entity) -> lista.add(aHistorialDTO(entity)));
		return lista;
	}

	/**
	 * Modifica una traducción del historial, solo si pertenece al usuario indicado.
	 * Se actualizan los códigos (cifrados de nuevo) y los lenguajes; el modelo, las
	 * mejoras y la fecha original se conservan.
	 *
	 * @param id     Identificador de la traducción.
	 * @param userId Identificador del usuario autenticado.
	 * @param dato   Nuevos valores en texto plano.
	 * @return {@code 0} si se actualizó; {@code 1} si no existe o no pertenece al
	 *         usuario.
	 */
	public int actualizarDeUsuario(Long id, Long userId, HistorialDTO dato) {
		Optional<Traduccion> found = buscarDeUsuario(id, userId);
		if (found.isEmpty()) {
			return 1;
		}
		Traduccion entity = found.get();
		entity.setCodigoOriginal(cifrar(dato.getCodigoOriginal()));
		entity.setCodigoTraducido(cifrar(dato.getCodigoTraducido()));
		entity.setLenguajeOrigen(dato.getLenguajeOrigen());
		entity.setLenguajeDestino(dato.getLenguajeDestino());
		repo.save(entity);
		return 0;
	}

	/**
	 * Elimina una traducción del historial, solo si pertenece al usuario indicado.
	 *
	 * @param id     Identificador de la traducción.
	 * @param userId Identificador del usuario autenticado.
	 * @return {@code 0} si se eliminó; {@code 1} si no existe o no pertenece al
	 *         usuario.
	 */
	public int eliminarDeUsuario(Long id, Long userId) {
		Optional<Traduccion> found = buscarDeUsuario(id, userId);
		if (found.isEmpty()) {
			return 1;
		}
		repo.delete(found.get());
		return 0;
	}

	/**
	 * Busca una traducción por ID y la devuelve solo si su propietario es el
	 * usuario indicado. Se responde igual que si no existiera para no revelar
	 * traducciones de otros usuarios.
	 */
	private Optional<Traduccion> buscarDeUsuario(Long id, Long userId) {
		return repo.findById(id).filter((t) -> t.getUsuario() != null && t.getUsuario().getId().equals(userId));
	}

	/** Convierte la entidad en {@link HistorialDTO} con los códigos descifrados. */
	private HistorialDTO aHistorialDTO(Traduccion entity) {
		return new HistorialDTO(entity.getId(), descifrar(entity.getCodigoOriginal()),
				descifrar(entity.getCodigoTraducido()), entity.getLenguajeOrigen(), entity.getLenguajeDestino(),
				entity.getModelo(), entity.getFecha());
	}

	/**
	 * {@inheritDoc} Retorna una lista con todas las traducciones registradas en el
	 * sistema
	 * 
	 * @return Lista de todos los {@link TraduccionDTO} descifrado
	 */
	@Override
	public List<TraduccionDTO> getAll() {
		List<Traduccion> entityList = repo.findAll();
		List<TraduccionDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			dtoList.add(aDtoDescifrado(entity));
		});
		return dtoList;
	}

	/**
	 * {@inheritDoc} Elimina una traduccion d la base de datos basándose en su
	 * identificadro único
	 * 
	 * @param id Identificador de la traducción a eliminar.
	 * @return 0 si se eliminó cor éxito, q si no se encontro el registro.
	 */

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

	/**
	 * {@inheritDoc} Actualiza los datos de una traducción existente, cifrando
	 * nuevamente los códigos
	 * 
	 * @param id        identificador de la traducción a modificar
	 * @param nuevoDato DTO con los nuevos valores a actualizar.
	 * @return 0 si la actualizacion fue exitosa, 1 si el registro no fue
	 *         encontrado.
	 */

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

	/**
	 * {@inheritDoc} Comprueba si existe una traducción registrada con el ID
	 * suministrado.
	 * 
	 * @param id Identificador a verificar.
	 * @return true si existe, false en caso contrario.
	 */

	@Override
	public boolean exist(Long id) {
		return repo.existsById(id) ? true : false;
	}

	/**
	 * Convierte una lista de mejoras en un único texto separado por saltos de
	 * línea.
	 * 
	 * @param mejoras Lista de strings con las mejoras descritas.
	 * @return Un string con las mejoras unidas o vacío si la lista es nula.
	 */
	private String mejorasATexto(List<String> mejoras) {
		return mejoras == null ? "" : String.join("\n", mejoras);
	}

	/**
	 * Convierte la entidad en DTO y descifra el DTO, no la entidad: si se
	 * modificara la entidad dentro de una transacción, Hibernate guardaría el texto
	 * plano en la base de datos.
	 * 
	 * @param entity La entidad {@link Traduccion} almacenada (con textos cifrados).
	 * @return El {@link TraduccionDTO} con los textos legibles.
	 */
	private TraduccionDTO aDtoDescifrado(Traduccion entity) {
		TraduccionDTO dto = mapper.map(entity, TraduccionDTO.class);
		dto.setCodigoOriginal(descifrar(entity.getCodigoOriginal()));
		dto.setCodigoTraducido(descifrar(entity.getCodigoTraducido()));
		return dto;
	}

	/**
	 * Cifra un texto utilizando el algoritmo AES.
	 * 
	 * @param texto Texto plano a cifrar.
	 * @return Texto cifrado o null si el parámetro de entrada es nulo.
	 */

	private static String cifrar(String texto) {
		return texto == null ? null : AESUtil.encrypt(texto);
	}

	/**
	 * Descifra un texto previamente cifrado utilizando AES.
	 * 
	 * @param cifrado Texto cifrado almacenado.
	 * @return Texto plano descifrado o null si el parámetro es nulo.
	 */

	private static String descifrar(String cifrado) {
		return cifrado == null ? null : AESUtil.decrypt(cifrado);
	}
}
