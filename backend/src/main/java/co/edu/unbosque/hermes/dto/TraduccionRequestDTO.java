package co.edu.unbosque.hermes.dto;

import java.util.Objects;

public class TraduccionRequestDTO {

	private String codigo;
	private String lenguajeOrigen;
	private String lenguajeDestino;

	public TraduccionRequestDTO() {
		// TODO Auto-generated constructor stub
	}

	public TraduccionRequestDTO(String codigo, String lenguajeOrigen, String lenguajeDestino) {
		this.codigo = codigo;
		this.lenguajeOrigen = lenguajeOrigen;
		this.lenguajeDestino = lenguajeDestino;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	@Override
	public int hashCode() {
		return Objects.hash(codigo, lenguajeDestino, lenguajeOrigen);
	}

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

	@Override
	public String toString() {
		return "TraduccionRequestDTO [codigo=" + codigo + ", lenguajeOrigen=" + lenguajeOrigen + ", lenguajeDestino="
				+ lenguajeDestino + "]";
	}

}
