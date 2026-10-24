package co.edu.unbosque.hermes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrador")
@DiscriminatorValue("ADMIN")
public class Administrador extends Usuario {

	@Column(nullable = false)
	private String solicitudes[];

	public Administrador(String email, String password, Rol rol, LocalDateTime fechaCreacion) {
		super(email, password, rol, fechaCreacion);
		// TODO Auto-generated constructor stub
	}

	public Administrador(String email, String password, Rol rol, LocalDateTime fechaCreacion, String[] solicitudes) {
		super(email, password, rol, fechaCreacion);
		this.solicitudes = solicitudes;
	}

	public String[] getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(String[] solicitudes) {
		this.solicitudes = solicitudes;
	}
	
	

}
