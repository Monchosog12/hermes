package co.edu.unbosque.hermes.exception;

import org.springframework.http.HttpStatus;

/**
 * Error al comunicarse con el proveedor de IA (OpenRouter). Lleva el código
 * HTTP que se devolverá al cliente y, si el proveedor lo indicó, los segundos
 * que conviene esperar antes de reintentar.
 */
public class IAServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
	private final String retryAfter;

	public IAServiceException(HttpStatus status, String message, String retryAfter, Throwable cause) {
		super(message, cause);
		this.status = status;
		this.retryAfter = retryAfter;
	}

	public HttpStatus getStatus() {
		return status;
	}

	/** Valor de la cabecera Retry-After del proveedor, o null si no la envió. */
	public String getRetryAfter() {
		return retryAfter;
	}
}
