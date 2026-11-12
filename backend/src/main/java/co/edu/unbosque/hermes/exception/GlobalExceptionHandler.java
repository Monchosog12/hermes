package co.edu.unbosque.hermes.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Convierte las excepciones de la aplicación en respuestas JSON limpias, sin
 * stack trace.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IAServiceException.class)
	public ResponseEntity<Map<String, Object>> handleIAService(IAServiceException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", ex.getStatus().value());
		body.put("error", ex.getStatus().getReasonPhrase());
		body.put("message", ex.getMessage());

		ResponseEntity.BodyBuilder response = ResponseEntity.status(ex.getStatus());
		if (ex.getRetryAfter() != null) {
			response.header(HttpHeaders.RETRY_AFTER, ex.getRetryAfter());
		}
		return response.body(body);
	}
}
