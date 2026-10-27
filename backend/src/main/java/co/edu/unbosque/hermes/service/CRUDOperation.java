package co.edu.unbosque.hermes.service;

import java.util.List;


public interface CRUDOperation<T> {
	
	public int crear(T dato);
	
	public int eliminarPorUsername(String username);
	
	public List<T> getAll();
	
	public int actualizarUsuarioPorId(Long id, T nuevoDato);
	
	public boolean exist(Long id);

}
