package co.edu.unbosque.hermes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;


@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {
	
	@Column(nullable = false)
	private String peticion;
	
	public Cliente(String email, String password, Rol rol, LocalDateTime fechaCreacion) {
		super(email, password, rol, fechaCreacion);
		// TODO Auto-generated constructor stub
	}
	
	

}
