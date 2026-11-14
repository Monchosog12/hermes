package co.edu.unbosque.hermes.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import co.edu.unbosque.hermes.entity.Usuario;

public class TraduccionDTO {

	private Long id;
	private String codigoOriginal;
	private String codigoTraducido;
	private String lenguajeOrigen;
	private String lenguajeDestino;
	private List<String> mejoras;
	private String modelo;
	private LocalDateTime fecha;
	private Usuario usuario;

	public TraduccionDTO() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getMejoras() {
		return mejoras;
	}

	public void setMejoras(List<String> mejoras) {
		this.mejoras = mejoras;
	}

	public TraduccionDTO(String codigoOriginal, String codigoTraducido, String lenguajeOrigen, String lenguajeDestino,
			String modelo, LocalDateTime fecha, Usuario usuario) {
		this.codigoOriginal = codigoOriginal;
		this.codigoTraducido = codigoTraducido;
		this.lenguajeOrigen = lenguajeOrigen;
		this.lenguajeDestino = lenguajeDestino;
		this.modelo = modelo;
		this.fecha = fecha;
		this.usuario = usuario;
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

	public String getLenguajeOrigen() {
		return lenguajeOrigen;
	}

	public void setLenguajeOrigen(String lenguajeOrigen) {
		this.lenguajeOrigen = lenguajeOrigen;
	}

	public String getLenguajeDestino() {
		return lenguajeDestino;
	}

	public void setLenguajeDestino(String lenguajeDestino) {
		this.lenguajeDestino = lenguajeDestino;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoOriginal, codigoTraducido, fecha, id, lenguajeDestino, lenguajeOrigen, modelo, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TraduccionDTO other = (TraduccionDTO) obj;
		return Objects.equals(codigoOriginal, other.codigoOriginal)
				&& Objects.equals(codigoTraducido, other.codigoTraducido) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(lenguajeDestino, other.lenguajeDestino)
				&& Objects.equals(lenguajeOrigen, other.lenguajeOrigen) && Objects.equals(modelo, other.modelo)
				&& Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "TraduccionDTO [id=" + id + ", codigoOriginal=" + codigoOriginal + ", codigoTraducido=" + codigoTraducido
				+ ", lenguajeOrigen=" + lenguajeOrigen + ", lenguajeDestino=" + lenguajeDestino + ", modelo=" + modelo
				+ ", fecha=" + fecha + ", usuario=" + usuario + "]";
	}



}