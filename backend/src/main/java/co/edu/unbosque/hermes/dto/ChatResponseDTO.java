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

	public ChatResponseDTO() {
	}

	public ChatResponseDTO(List<Choice> choices, int statuscode, String retryAfter) {
		this.choices = choices;
		this.statuscode = statuscode;
		this.retryAfter = retryAfter;
	}

	/** Devuelve el texto de la primera respuesta, o null si no vino ninguna. */
	public String getPrimerContenido() {
		if (choices == null || choices.isEmpty() || choices.get(0).getMessage() == null) {
			return null;
		}
		return choices.get(0).getMessage().getContent();
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	public int getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}

	public String getRetryAfter() {
		return retryAfter;
	}

	public void setRetryAfter(String retryAfter) {
		this.retryAfter = retryAfter;
	}

	@Override
	public String toString() {
		return "ChatResponseDTO [choices=" + choices + ", statuscode=" + statuscode + ", retryAfter=" + retryAfter
				+ "]";
	}

	public static class Choice {

		private Message message;

		public Choice() {
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return "Choice [message=" + message + "]";
		}
	}

	public static class Message {

		private String role;
		private String content;

		public Message() {
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@Override
		public String toString() {
			return "Message [role=" + role + ", content=" + content + "]";
		}
	}
}
