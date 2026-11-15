package co.edu.unbosque.hermes.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.hermes.repository.UsuarioRepository;
import co.edu.unbosque.hermes.entity.Usuario;
import co.edu.unbosque.hermes.util.AESUtil;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	/**
	   * Repositorio de usuarios utilizado para buscar información de usuarios.
	   */
	  private final UsuarioRepository userRepository;

	  /**
	   * Constructor que inicializa el repositorio de usuarios.
	   * 
	   * @param userRepository El repositorio de usuarios a utilizar para las consultas
	   */
	  public UserDetailsServiceImpl(UsuarioRepository userRepository) {
	    this.userRepository = userRepository;
	  }

	  /**
	   * Carga los detalles del usuario por su nombre de usuario.
	   * 
	   * @param username El nombre de usuario para buscar
	   * @return Los detalles del usuario encontrado
	   * @throws UsernameNotFoundException Si no se encuentra el usuario con el nombre de usuario proporcionado
	   */
	  @Override
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    // El username está cifrado en la base de datos: se cifra el parámetro para
	    // buscarlo
	    Usuario usuario = userRepository
	        .findByUsername(AESUtil.encrypt(username))
	        .orElseThrow(
	            () -> new UsernameNotFoundException("User not found with username: " + username));
	    // Se descifra para que el JWT lleve el username en texto normal. Si quedara
	    // cifrado, el filtro JWT lo volvería a cifrar al buscarlo y no lo encontraría.
	    usuario.setUsername(AESUtil.decrypt(usuario.getUsername()));
	    usuario.setEmail(AESUtil.decrypt(usuario.getEmail()));
	    return usuario;
	  }

}
