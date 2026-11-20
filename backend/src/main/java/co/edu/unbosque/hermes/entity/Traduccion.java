package co.edu.unbosque.hermes.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una traducción o refactorización de código almacenada en la base de datos (tabla "traducciones").
 * <p>
 * Almacena el código original, el código traducido, los lenguajes involucrados, las mejoras sugeridas por la IA,
 * el modelo utilizado, la fecha de registro (generada automáticamente) y la relación con el usuario propietario.
 * </p>
 * 
 */
@Entity
@Table(name="traducciones")
public class Traduccion {

	/** Identificador único de la traducción (Generado automáticamente por autoincremento). */
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	
	/** Código fuente original enviado por el usuario. */
	@Column(columnDefinition = "TEXT", nullable = false)
	private String codigoOriginal;
	
	/** Código resultante tras el proceso de traducción o refactorización. */
	@Column(columnDefinition = "TEXT")
	private String codigoTraducido;
	
	/** Lenguaje de programación o formato de origen del código. */
	private String lenguajeOrigen;
	
	/** Lenguaje de programación o formato de destino al cual se tradujo. */
	@Column(nullable = false)
	private String lenguajeDestino;
	
	/** Lista de mejoras sugeridas por la IA, almacenadas como texto (una por línea). */
	@Column(columnDefinition = "TEXT")
	private String mejoras;
	
	/** Nombre o identificador del modelo de IA que realizó la traducción. */
	private String modelo;
	
	/** Fecha y hora en la que se creó el registro, generada automáticamente por Hibernate. */
	@CreationTimestamp
	private LocalDateTime fecha;
	
	/** Usuario propietario que realizó la solicitud de traducción (relación Muchos a Uno). */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	/**
	 * Constructor por defecto.
	 */
	public Traduccion() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con los atributos principales (sin ID).
	 * 
	 * @param codigoOriginal   Código fuente original.
	 * @param codigoTraducido  Código resultante de la traducción.
	 * @param lenguajeOrigen   Lenguaje original.
	 * @param lenguajeDestino  Lenguaje de destino.
	 * @param modelo           Modelo de IA utilizado.
	 * @param fecha            Fecha y hora de la traducción.
	 * @param usuario          Objeto {@link Usuario} asociado.
	 */
	public Traduccion(String codigoOriginal, String codigoTraducido, String lenguajeOrigen, String lenguajeDestino,
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
	 * @return ID único.
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
	 * Obtiene el nombre del modelo de IA utilizado.
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
	 * Obtiene la fecha y hora de creación del registro.
	 * 
	 * @return Objeto {@link LocalDateTime} con la fecha.
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}

	/**
	 * Establece la fecha y hora de creación del registro.
	 * 
	 * @param fecha Fecha a asignar.
	 */
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	/**
	 * Obtiene la cadena con las mejoras sugeridas por la IA.
	 * 
	 * @return Cadena con las mejoras.
	 */
	public String getMejoras() {
		return mejoras;
	}

	/**
	 * Establece la cadena con las mejoras sugeridas por la IA.
	 * 
	 * @param mejoras Cadena de mejoras a asignar.
	 */
	public void setMejoras(String mejoras) {
		this.mejoras = mejoras;
	}

	/**
	 * Obtiene el usuario asociado a la traducción.
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
	 * Calcula el código hash basado en los atributos de la entidad.
	 * 
	 * @return Valor entero del hash.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(codigoOriginal, codigoTraducido, fecha, id, lenguajeDestino, lenguajeOrigen, modelo);
	}

	/**
	 * Compara la igualdad de esta entidad con otra pasada por parámetro.
	 * 
	 * @param obj Objeto a comparar.
	 * @return {@code true} si los objetos coinciden en sus atributos principales; {@code false} en caso contrario.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Traduccion other = (Traduccion) obj;
		return Objects.equals(codigoOriginal, other.codigoOriginal)
				&& Objects.equals(codigoTraducido, other.codigoTraducido) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(lenguajeDestino, other.lenguajeDestino)
				&& Objects.equals(lenguajeOrigen, other.lenguajeOrigen) && Objects.equals(modelo, other.modelo);
	}

	/**
	 * Retorna una representación en cadena del objeto Traduccion.
	 * 
	 * @return Cadena con los valores actuales de los atributos.
	 */
	@Override
	public String toString() {
		return "Traduccion [id=" + id + ", codigoOriginal=" + codigoOriginal + ", codigoTraducido=" + codigoTraducido
				+ ", lenguajeOrigen=" + lenguajeOrigen + ", lenguajeDestino=" + lenguajeDestino + ", modelo=" + modelo
				+ ", fecha=" + fecha + "]";
	}

}
