package co.edu.unbosque.hermes.dto;

import java.util.Map;

public class EstadisticasDTO {

	/** Cantidad total de usuarios registrados en el sistema. */
	private long totalUsuarios;

	/** Cantidad total de registros de traducción almacenados. */
	private long totalTraducciones;

	/**
	 * Mapa con el conteo de traducciones agrupadas por lenguaje (clave: lenguaje,
	 * valor: cantidad).
	 */
	private Map<String, Long> traduccionesPorLenguaje;

	/**
	 * Constructor vacio
	 */
	public EstadisticasDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con parámetros para inicializar las métricas principales.
	 * 
	 * @param totalUsuarios           Cantidad total de usuarios.
	 * @param totalTraducciones       Cantidad total de traducciones.
	 * @param traduccionesPorLenguaje Conteo agrupado por lenguaje.
	 */
	public EstadisticasDTO(long totalUsuarios, long totalTraducciones, Map<String, Long> traduccionesPorLenguaje) {
		this.totalUsuarios = totalUsuarios;
		this.totalTraducciones = totalTraducciones;
		this.traduccionesPorLenguaje = traduccionesPorLenguaje;
	}

	/**
	 * Obtiene el total de usuarios registrados.
	 * 
	 * @return Número total de usuarios.
	 */
	public long getTotalUsuarios() {
		return totalUsuarios;
	}

	/**
	 * Establece el total de usuarios registrados.
	 * 
	 * @param totalUsuarios Número total de usuarios.
	 */
	public void setTotalUsuarios(long totalUsuarios) {
		this.totalUsuarios = totalUsuarios;
	}

	/**
	 * Obtiene el total de traducciones registradas.
	 * 
	 * @return Número total de traducciones.
	 */
	public long getTotalTraducciones() {
		return totalTraducciones;
	}

	/**
	 * Establece el total de traducciones registradas.
	 * 
	 * @param totalTraducciones Número total de traducciones.
	 */
	public void setTotalTraducciones(long totalTraducciones) {
		this.totalTraducciones = totalTraducciones;
	}

	/**
	 * Obtiene el mapa de traducciones agrupadas por lenguaje.
	 * 
	 * @return Mapa con el nombre del lenguaje y su respectivo conteo.
	 */
	public Map<String, Long> getTraduccionesPorLenguaje() {
		return traduccionesPorLenguaje;
	}

	/**
	 * Establece el mapa de traducciones agrupadas por lenguaje.
	 * 
	 * @param traduccionesPorLenguaje Mapa de conteos por lenguaje.
	 */
	public void setTraduccionesPorLenguaje(Map<String, Long> traduccionesPorLenguaje) {
		this.traduccionesPorLenguaje = traduccionesPorLenguaje;
	}
}
