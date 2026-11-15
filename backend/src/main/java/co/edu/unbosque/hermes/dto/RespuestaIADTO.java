package co.edu.unbosque.hermes.dto;

import java.util.List;
import java.util.Objects;

public class RespuestaIADTO {

	private String codigo;
	private String lenguajeDetectado;
	private List<String> mejoras;

	public RespuestaIADTO() {
		// TODO Auto-generated constructor stub
	}

	public RespuestaIADTO(String codigo, String lenguajeDetectado, List<String> mejoras) {
		this.codigo = codigo;
		this.lenguajeDetectado = lenguajeDetectado;
		this.mejoras = mejoras;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getLenguajeDetectado() {
		return lenguajeDetectado;
	}

	public void setLenguajeDetectado(String lenguajeDetectado) {
		this.lenguajeDetectado = lenguajeDetectado;
	}

	public List<String> getMejoras() {
		return mejoras;
	}

	public void setMejoras(List<String> mejoras) {
		this.mejoras = mejoras;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, lenguajeDetectado, mejoras);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RespuestaIADTO other = (RespuestaIADTO) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(lenguajeDetectado, other.lenguajeDetectado)
				&& Objects.equals(mejoras, other.mejoras);
	}

	@Override
	public String toString() {
		return "RespuestaIADTO [codigo=" + codigo + ", lenguajeDetectado=" + lenguajeDetectado + ", mejoras="
				+ mejoras + "]";
	}

}
