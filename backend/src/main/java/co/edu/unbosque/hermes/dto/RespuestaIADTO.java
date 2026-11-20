package co.edu.unbosque.hermes.dto;

import java.util.List;
import java.util.Objects;

/**
 * Representa el objeto de transferencia de datos DTO para las respuestas
 * estructuradas generadas por la IA.
 *
 * Contien el codigo fuente procesadi o refactorizado, el lenguaje de
 * programacion detectado y una lista de sugerencias o mejoras aplicadas
 */
public class RespuestaIADTO {

	private String codigo;
	private String lenguajeDetectado;
	private List<String> mejoras;

	public RespuestaIADTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con todos los atributos de la clase
	 * 
	 * @param código   Código fuente ya procesado o generado por la IA.
	 * @param lenguajeDetectado Lenguaje de programación identificado en el
	 *                 análisis.
	 * @param mejoras  Lista de descripciones con las mejoras o recomendaciones que
	 *                 se realizaron o se deben relaizar.
	 */

	public RespuestaIADTO(String codigo, String lenguajeDetectado, List<String> mejoras) {
		this.codigo = codigo;
		this.lenguajeDetectado = lenguajeDetectado;
		this.mejoras = mejoras;
	}

	/**
	 * Obtiene el código fuente procesado.
	 * 
	 * @return Cadena que contiene el código.
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Establece el código fuente procesado.
	 * 
	 * @param codigo Código a asignar.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obtiene el lenguaje de programación detectado.
	 * 
	 * @return Cadena con el nombre del lenguaje.
	 */
	public String getLenguajeDetectado() {
		return lenguajeDetectado;
	}

	/**
	 * Establece el lenguaje de programación detectado.
	 * 
	 * @param lenguajeDetectado Lenguaje a asignar.
	 */
	public void setLenguajeDetectado(String lenguajeDetectado) {
		this.lenguajeDetectado = lenguajeDetectado;
	}

	/**
	 * Obtiene la lista de mejoras sugeridas o aplicadas.
	 * 
	 * @return Lista de cadenas con las mejoras.
	 */
	public List<String> getMejoras() {
		return mejoras;
	}

	/**
	 * Establece la lista de mejoras sugeridas o aplicadas.
	 * 
	 * @param mejoras Lista de mejoras a asignar.
	 */
	public void setMejoras(List<String> mejoras) {
		this.mejoras = mejoras;
	}

	/**
	 * Calcula el código hash basado en los atributos del objeto.
	 * 
	 * @return Valor entero del hash.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(codigo, lenguajeDetectado, mejoras);
	}

	/**
	 * Compara la igualdad de este objeto con otro pasado por parámetro.
	 * 
	 * @param obj Objeto a comparar.
	 * @return {@code true} si los objetos son iguales en sus atributos;
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
		RespuestaIADTO other = (RespuestaIADTO) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(lenguajeDetectado, other.lenguajeDetectado)
				&& Objects.equals(mejoras, other.mejoras);
	}

	/**
	 * Retorna una representación en cadena del objeto RespuestaIADTO.
	 * 
	 * @return Cadena con los valores actuales de los atributos.
	 */
	@Override
	public String toString() {
		return "RespuestaIADTO [codigo=" + codigo + ", lenguajeDetectado=" + lenguajeDetectado + ", mejoras=" + mejoras
				+ "]";
	}
}