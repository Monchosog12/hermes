package co.edu.unbosque.hermes.dto;

import java.util.List;

/**
 * Respuesta de POST /chat/completions de OpenRouter. Solo se mapean los campos
 * que se usan: choices[].message.content. Además guarda el código HTTP y la
 * cabecera Retry-After, que se rellenan a mano en
 * ExternalHTTPRequestHandler.doPostAndConvertToDto().
 */
public class ChatResponseDTO {

	private List<Choice> choices;
	private int statuscode;
	private String retryAfter;

	/** Constructor vacio */
	public ChatResponseDTO() {
	}

	/**
	 * Constructor con todos los campos.
	 * 
	 * @param choices    Lista de opciones de respuesta obtenidas del modelo.
	 * @param statuscode Código de estado HTTP de la respuesta.
	 * @param retryAfter Valor de la cabecera Retry-After en caso de limitación de
	 *                   tasa (rate limit).
	 */

	public ChatResponseDTO(List<Choice> choices, int statuscode, String retryAfter) {
		this.choices = choices;
		this.statuscode = statuscode;
		this.retryAfter = retryAfter;
	}

	/**
	 * Devuelve el texto de la primera respuesta generada, o {@code null} si no se
	 * encuentra ninguna.
	 * 
	 * @return El contenido en texto plano del primer mensaje de la lista de
	 *         opciones, o {@code null} si la lista está vacía o el mensaje es nulo.
	 */
	public String getPrimerContenido() {
		if (choices == null || choices.isEmpty() || choices.get(0).getMessage() == null) {
			return null;
		}
		return choices.get(0).getMessage().getContent();
	}

	/**
	 * Obtiene la lista de opciones de respuesta.
	 * 
	 * @return Lista de objetos {@link Choice}.
	 */

	public List<Choice> getChoices() {
		return choices;
	}

	/**
	 * Establece la lista de opciones de respuesta.
	 * 
	 * @param choices Lista de objetos {@link Choice} a asignar.
	 */

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	/**
	 * Obtiene el código de estado HTTP de la respuesta.
	 * 
	 * @return Código de estado numérico.
	 */
	public int getStatuscode() {
		return statuscode;
	}

	/**
	 * Establece el código de estado HTTP de la respuesta.
	 * 
	 * @param statuscode Código de estado a asignar.
	 */
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}

	/**
	 * Obtiene el valor de la cabecera Retry-After.
	 * 
	 * @return Tiempo de espera sugerido en formato de cadena, o {@code null}.
	 */
	public String getRetryAfter() {
		return retryAfter;
	}

	/**
	 * Establece el valor de la cabecera Retry-After.
	 * 
	 * @param retryAfter Valor de la cabecera a asignar.
	 */
	public void setRetryAfter(String retryAfter) {
		this.retryAfter = retryAfter;
	}

	/**
	 * Retorna una representación en cadena del objeto ChatResponseDTO.
	 * 
	 * @return Cadena con los valores actuales de los atributos.
	 */
	@Override
	public String toString() {
		return "ChatResponseDTO [choices=" + choices + ", statuscode=" + statuscode + ", retryAfter=" + retryAfter
				+ "]";
	}

	/**
	 * Clase estática anidada que representa una opción de respuesta dentro del
	 * arreglo de opciones.
	 */
	public static class Choice {

		private Message message;

		/**
		 * Constructor por defecto.
		 */
		public Choice() {
		}

		/**
		 * Obtiene el mensaje contenido en la opción.
		 * 
		 * @return Objeto {@link Message}.
		 */
		public Message getMessage() {
			return message;
		}

		/**
		 * Establece el mensaje contenido en la opción.
		 * 
		 * @param message Objeto {@link Message} a asignar.
		 */
		public void setMessage(Message message) {
			this.message = message;
		}

		/**
		 * Retorna una representación en cadena del objeto Choice.
		 * 
		 * @return Cadena con el mensaje asociado.
		 */
		@Override
		public String toString() {
			return "Choice [message=" + message + "]";
		}
	}

	/**
	 * Clase estática anidada que representa el mensaje de chat (rol y contenido).
	 */
	public static class Message {

		private String role;
		private String content;

		/**
		 * Constructor por defecto.
		 */
		public Message() {
		}

		/**
		 * Obtiene el rol del emisor del mensaje (ej. "user", "assistant", "system").
		 * 
		 * @return Cadena que representa el rol.
		 */
		public String getRole() {
			return role;
		}

		/**
		 * Establece el rol del emisor del mensaje.
		 * 
		 * @param role Rol a asignar.
		 */
		public void setRole(String role) {
			this.role = role;
		}

		/**
		 * Obtiene el contenido textual del mensaje.
		 * 
		 * @return Cadena con el texto del contenido.
		 */
		public String getContent() {
			return content;
		}

		/**
		 * Establece el contenido textual del mensaje.
		 * 
		 * @param content Texto del contenido a asignar.
		 */
		public void setContent(String content) {
			this.content = content;
		}

		/**
		 * Retorna una representación en cadena del objeto Message.
		 * 
		 * @return Cadena con los campos role y content.
		 */
		@Override
		public String toString() {
			return "Message [role=" + role + ", content=" + content + "]";
		}
	}
}
