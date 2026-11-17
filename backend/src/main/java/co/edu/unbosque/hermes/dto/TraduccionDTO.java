package co.edu.unbosque.hermes.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import co.edu.unbosque.hermes.entity.Usuario;

/**
 * Repesenta el objeto de transeferenci de datos DTO para la informaco de
 * traducciones o conversiones de código realizadas en el sistema
 * 
 * 
 * Contien los detalles del codigo fuente original y traducido, los lenguajes de
 * origen y destino, las mejoras aplicadas, el modelo de IA utilizado , fecha de
 * realización y el usuario asociado.
 */

public class TraduccionDTO {

	private Long id;
	private String codigoOriginal;
	private String codigoTraducido;
	private String lenguajeOrigen;
	private String lenguajeDestino;
	private List<String> mejoras;
	private String modelo;
	private LocalDateTime fecha;
	private Usuario usuario;

	public TraduccionDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con los campos principales de la traducción (sin ID).
	 * 
	 * @param codigoOriginal  Código fuente antes de la traducción o
	 *                        refactorización.
	 * @param codigoTraducido Código resultante tras el proceso de traducción o
	 *                        refactorización.
	 * @param lenguajeOrigen  Lenguaje de programación o formato del código
	 *                        original.
	 * @param lenguajeDestino Lenguaje de programación o formato del código
	 *                        traducido.
	 * @param modelo          Nombre o identificador del modelo de IA utilizado.
	 * @param fecha           Fecha y hora en que se llevó a cabo la traducción.
	 * @param usuario         Objeto {@link Usuario} que realizó la solicitud.
	 */
	public TraduccionDTO(String codigoOriginal, String codigoTraducido, String lenguajeOrigen, String lenguajeDestino,
			String modelo, LocalDateTime fecha, Usuario usuario) {
		this.codigoOriginal = codigoOriginal;
		this.codigoTraducido = codigoTraducido;
		this.lenguajeOrigen = lenguajeOrigen;
		this.lenguajeDestino = lenguajeDestino;
		this.modelo = modelo;
		this.fecha = fecha;
		this.usuario = usuario;
	}

	/**
	 * Obtiene el identificador único de la traducción.
	 * 
	 * @return ID único de la entidad.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Establece el identificador único de la traducción.
	 * 
	 * @param id ID a asignar.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtiene el código fuente original.
	 * 
	 * @return Cadena con el código original.
	 */
	public String getCodigoOriginal() {
		return codigoOriginal;
	}

	/**
	 * Establece el código fuente original.
	 * 
	 * @param codigoOriginal Código original a asignar.
	 */
	public void setCodigoOriginal(String codigoOriginal) {
		this.codigoOriginal = codigoOriginal;
	}

	/**
	 * Obtiene el código fuente traducido.
	 * 
	 * @return Cadena con el código traducido.
	 */
	public String getCodigoTraducido() {
		return codigoTraducido;
	}

	/**
	 * Establece el código fuente traducido.
	 * 
	 * @param codigoTraducido Código traducido a asignar.
	 */
	public void setCodigoTraducido(String codigoTraducido) {
		this.codigoTraducido = codigoTraducido;
	}

	/**
	 * Obtiene el lenguaje de programación de origen.
	 * 
	 * @return Cadena con el lenguaje original.
	 */
	public String getLenguajeOrigen() {
		return lenguajeOrigen;
	}

	/**
	 * Establece el lenguaje de programación de origen.
	 * 
	 * @param lenguajeOrigen Lenguaje de origen a asignar.
	 */
	public void setLenguajeOrigen(String lenguajeOrigen) {
		this.lenguajeOrigen = lenguajeOrigen;
	}

	/**
	 * Obtiene el lenguaje de programación de destino.
	 * 
	 * @return Cadena con el lenguaje de destino.
	 */
	public String getLenguajeDestino() {
		return lenguajeDestino;
	}

	/**
	 * Establece el lenguaje de programación de destino.
	 * 
	 * @param lenguajeDestino Lenguaje de destino a asignar.
	 */
	public void setLenguajeDestino(String lenguajeDestino) {
		this.lenguajeDestino = lenguajeDestino;
	}

	/**
	 * Obtiene la lista de mejoras o sugerencias aplicadas durante el proceso.
	 * 
	 * @return Lista de cadenas con las mejoras.
	 */
	public List<String> getMejoras() {
		return mejoras;
	}

	/**
	 * Establece la lista de mejoras o sugerencias aplicadas.
	 * 
	 * @param mejoras Lista de mejoras a asignar.
	 */
	public void setMejoras(List<String> mejoras) {
		this.mejoras = mejoras;
	}

	/**
	 * Obtiene el identificador o nombre del modelo de IA utilizado.
	 * 
	 * @return Cadena con el modelo.
	 */
	public String getModelo() {
		return modelo;
	}

	/**
	 * Establece el modelo de IA utilizado.
	 * 
	 * @param modelo Modelo a asignar.
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	/**
	 * Obtiene la fecha y hora en que se realizó la traducción.
	 * 
	 * @return Objeto {@link LocalDateTime} con la fecha y hora.
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}

	/**
	 * Establece la fecha y hora de la traducción.
	 * 
	 * @param fecha Fecha a asignar.
	 */
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	/**
	 * Obtiene el usuario asociado que realizó la solicitud de traducción.
	 * 
	 * @return Objeto {@link Usuario}.
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Establece el usuario asociado a la traducción.
	 * 
	 * @param usuario Usuario a asignar.
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Calcula el código hash basado en los atributos del objeto.
	 * 
	 * @return Valor entero del hash.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(codigoOriginal, codigoTraducido, fecha, id, lenguajeDestino, lenguajeOrigen, modelo,
				usuario);
	}

	/**
	 * Compara la igualdad de este objeto con otro pasado por parámetro.
	 * 
	 * @param obj Objeto a comparar.
	 * @return {@code true} si los objetos coinciden en todos sus atributos;
	 *         {@code false} en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TraduccionDTO other = (TraduccionDTO) obj;
		return Objects.equals(codigoOriginal, other.codigoOriginal)
				&& Objects.equals(codigoTraducido, other.codigoTraducido) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(lenguajeDestino, other.lenguajeDestino)
				&& Objects.equals(lenguajeOrigen, other.lenguajeOrigen) && Objects.equals(modelo, other.modelo)
				&& Objects.equals(usuario, other.usuario);
	}

	/**
	 * Retorna una representación en cadena del objeto TraduccionDTO.
	 * 
	 * @return Cadena con los valores actuales de los atributos.
	 */
	@Override
	public String toString() {
		return "TraduccionDTO [id=" + id + ", codigoOriginal=" + codigoOriginal + ", codigoTraducido=" + codigoTraducido
				+ ", lenguajeOrigen=" + lenguajeOrigen + ", lenguajeDestino=" + lenguajeDestino + ", modelo=" + modelo
				+ ", fecha=" + fecha + ", usuario=" + usuario + "]";
	}
}