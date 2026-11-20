package co.edu.unbosque.hermes.dto;

import java.util.Objects;

/**
 * Representa el objeti de transferencia de datos para las solicitudes de
 * traducción o conversacin de código enviadas al sistema
 * 
 * Contine el código fuente que se desea traducir, asi como el lenguaje de
 * origen y el lenguaje al cual quiero transformar.
 */
public class TraduccionRequestDTO {

	private String codigo;
	private String lenguajeOrigen;
	private String lenguajeDestino;

	public TraduccionRequestDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con todos los campos.
	 * 
	 * @param codigo          Código fuente que se va a traducir o refactorizar.
	 * @param lenguajeOrigen  Lenguaje de programación o formato original del
	 *                        código.
	 * @param lenguajeDestino Lenguaje de programación o formato al que se desea
	 *                        traducir.
	 */
	public TraduccionRequestDTO(String codigo, String lenguajeOrigen, String lenguajeDestino) {
		this.codigo = codigo;
		this.lenguajeOrigen = lenguajeOrigen;
		this.lenguajeDestino = lenguajeDestino;
	}

	/**
	 * Obtiene el código fuente de la solicitud.
	 * 
	 * @return Cadena con el código fuente.
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Establece el código fuente de la solicitud.
	 * 
	 * @param codigo Código a asignar.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	 * Calcula el código hash basado en los atributos del objeto.
	 * 
	 * @return Valor entero del hash.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(codigo, lenguajeDestino, lenguajeOrigen);
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
		TraduccionRequestDTO other = (TraduccionRequestDTO) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(lenguajeDestino, other.lenguajeDestino)
				&& Objects.equals(lenguajeOrigen, other.lenguajeOrigen);
	}

	/**
	 * Retorna una representación en cadena del objeto TraduccionRequestDTO.
	 * 
	 * @return Cadena con los valores actuales de los atributos.
	 */
	@Override
	public String toString() {
		return "TraduccionRequestDTO [codigo=" + codigo + ", lenguajeOrigen=" + lenguajeOrigen + ", lenguajeDestino="
				+ lenguajeDestino + "]";
	}

}
