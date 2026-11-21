package co.edu.unbosque.hermes.dto;

import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos para el historial de traducciones de un
 * usuario.
 * <p>
 * A diferencia de {@link TraduccionDTO}, no incluye el usuario propietario: el
 * historial siempre pertenece al usuario autenticado, y así no se exponen sus
 * datos (ni se intenta serializar la relación perezosa con la entidad).
 * </p>
 */
public class HistorialDTO {

	private Long id;
	private String codigoOriginal;
	private String codigoTraducido;
	private String lenguajeOrigen;
	private String lenguajeDestino;
	private String modelo;
	private LocalDateTime fecha;

	/** Constructor vacío requerido para la deserialización JSON. */
	public HistorialDTO() {
	}

	/**
	 * Constructor con todos los campos.
	 *
	 * @param id              Identificador de la traducción.
	 * @param codigoOriginal  Código fuente original (texto plano).
	 * @param codigoTraducido Código traducido (texto plano).
	 * @param lenguajeOrigen  Lenguaje del código original.
	 * @param lenguajeDestino Lenguaje del código traducido.
	 * @param modelo          Modelo de IA que realizó la traducción.
	 * @param fecha           Fecha y hora de la traducción.
	 */
	public HistorialDTO(Long id, String codigoOriginal, String codigoTraducido, String lenguajeOrigen,
			String lenguajeDestino, String modelo, LocalDateTime fecha) {
		this.id = id;
		this.codigoOriginal = codigoOriginal;
		this.codigoTraducido = codigoTraducido;
		this.lenguajeOrigen = lenguajeOrigen;
		this.lenguajeDestino = lenguajeDestino;
		this.modelo = modelo;
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoOriginal() {
		return codigoOriginal;
	}

	public void setCodigoOriginal(String codigoOriginal) {
		this.codigoOriginal = codigoOriginal;
	}

	public String getCodigoTraducido() {
		return codigoTraducido;
	}

	public void setCodigoTraducido(String codigoTraducido) {
		this.codigoTraducido = codigoTraducido;
	}

	public String getLenguajeOrigen() {
		return lenguajeOrigen;
	}

	public void setLenguajeOrigen(String lenguajeOrigen) {
		this.lenguajeOrigen = lenguajeOrigen;
	}

	public String getLenguajeDestino() {
		return lenguajeDestino;
	}

	public void setLenguajeDestino(String lenguajeDestino) {
		this.lenguajeDestino = lenguajeDestino;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
}
