package co.edu.unbosque.hermes.entity;

import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Traduccion {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String codigoOriginal;
	private String codigoTraducido;
	private String[] lenguages;
	private String modelo;
	private LocalDateTime fecha;

	public Traduccion() {
		// TODO Auto-generated constructor stub
	}

	public Traduccion(String codigoOriginal, String codigoTraducido, String[] lenguages, String modelo,
			LocalDateTime fecha) {
		this.codigoOriginal = codigoOriginal;
		this.codigoTraducido = codigoTraducido;
		this.lenguages = lenguages;
		this.modelo = modelo;
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoOriginal() {
		return codigoOriginal;
	}

	public void setCodigoOriginal(String codigoOriginal) {
		this.codigoOriginal = codigoOriginal;
	}

	public String getCodigoTraducido() {
		return codigoTraducido;
	}

	public void setCodigoTraducido(String codigoTraducido) {
		this.codigoTraducido = codigoTraducido;
	}

	public String[] getLenguages() {
		return lenguages;
	}

	public void setLenguages(String[] lenguages) {
		this.lenguages = lenguages;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Traduccion [id=" + id + ", codigoOriginal=" + codigoOriginal + ", codigoTraducido=" + codigoTraducido
				+ ", lenguages=" + Arrays.toString(lenguages) + ", modelo=" + modelo + ", fecha=" + fecha + "]";
	}
	
	

}
