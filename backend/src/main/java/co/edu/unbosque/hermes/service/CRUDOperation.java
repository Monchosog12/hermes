package co.edu.unbosque.hermes.service;

import java.util.List;

/**
 * Interfaz genérica que define las operaciones básicas de operaciones CRUD 
 * (Crear, Leer, Actualizar, Eliminar y Verificación) para los servicios del sistema.
 * 
 * @param <T> Tipo de Objeto de Transferencia de Datos (DTO) sobre el cual operará el servicio.
 * 
 */
public interface CRUDOperation<T> {
	
	/**
	 * Crea y almacena un nuevo registro en el sistema.
	 * 
	 * @param dato Objeto con la información a registrar.
	 * @return {@code 0} si la operación fue exitosa, o un código de error en caso contrario.
	 */
	public int crear(T dato);
	
	/**
	 * Elimina un registro del sistema basándose en su identificador único.
	 * 
	 * @param id Identificador único del registro a eliminar.
	 * @return {@code 0} si fue eliminado con éxito, o {@code 1} si no se encontró.
	 */
	public int eliminarPorId(Long id);
		
	/**
	 * Obtiene una lista con todos los registros existentes.
	 * 
	 * @return Lista de objetos de tipo {@link T}.
	 */
	public List<T> getAll();
	
	/**
	 * Actualiza un registro existente identificado por su ID.
	 * 
	 * @param id        Identificador único del registro a actualizar.
	 * @param nuevoDato Objeto con la nueva información a aplicar.
	 * @return {@code 0} si se actualizó correctamente, o un código de error en caso contrario.
	 */
	public int actualizarPorId(Long id, T nuevoDato);
	
	/**
	 * Comprueba si existe un registro asociado al identificador proporcionado.
	 * 
	 * @param id Identificador único a verificar.
	 * @return {@code true} si el registro existe; {@code false} en caso contrario.
	 */
	public boolean exist(Long id);

}
