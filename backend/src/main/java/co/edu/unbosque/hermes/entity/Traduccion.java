package co.edu.unbosque.hermes.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="traducciones")
public class Traduccion {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@Column(columnDefinition = "TEXT", nullable = false)
	private String codigoOriginal;
	@Column(columnDefinition = "TEXT")
	private String codigoTraducido;
	private String lenguajeOrigen;
	@Column(nullable = false)
	private String lenguajeDestino;
	/** Lista de mejoras sugeridas por la IA, una por línea. */
	@Column(columnDefinition = "TEXT")
	private String mejoras;
	private String modelo;
	@CreationTimestamp
	private LocalDateTime fecha;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	public Traduccion() {
		// TODO Auto-generated constructor stub
	}

	public Traduccion(String codigoOriginal, String codigoTraducido, String lenguajeOrigen, String lenguajeDestino,
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

	public String getMejoras() {
		return mejoras;
	}

	public void setMejoras(String mejoras) {
		this.mejoras = mejoras;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoOriginal, codigoTraducido, fecha, id, lenguajeDestino, lenguajeOrigen, modelo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Traduccion other = (Traduccion) obj;
		return Objects.equals(codigoOriginal, other.codigoOriginal)
				&& Objects.equals(codigoTraducido, other.codigoTraducido) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id) && Objects.equals(lenguajeDestino, other.lenguajeDestino)
				&& Objects.equals(lenguajeOrigen, other.lenguajeOrigen) && Objects.equals(modelo, other.modelo);
	}

	@Override
	public String toString() {
		return "Traduccion [id=" + id + ", codigoOriginal=" + codigoOriginal + ", codigoTraducido=" + codigoTraducido
				+ ", lenguajeOrigen=" + lenguajeOrigen + ", lenguajeDestino=" + lenguajeDestino + ", modelo=" + modelo
				+ ", fecha=" + fecha + "]";
	}

}