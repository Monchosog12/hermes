package co.edu.unbosque.hermes.dto;

/**
 * Data Transfer Object (DTO) que encapsula la información necesaria para el
 * envío de correos electrónicos dentro de la aplicación.
 */
public class CorreoDTO {

	/**
	 * Correo electrónico de la persona que recibe el mensaje.
	 */
	private String destinatario;

	/**
	 * Título o asunto principal del correo.
	 */
	private String asunto;

	/**
	 * Cuerpo o contenido del mensaje a enviar.
	 */
	private String mensaje;

	/**
	 * Constructor por defecto.
	 */
	public CorreoDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor con todos los atributos inicializados.
	 * 
	 * @param destinatario Correo electrónico de la persona que recibe el mensaje.
	 * @param asunto       Título o asunto principal del correo.
	 * @param mensaje      Cuerpo o contenido del mensaje a enviar.
	 */
	public CorreoDTO(String destinatario, String asunto, String mensaje) {
		this.destinatario = destinatario;
		this.asunto = asunto;
		this.mensaje = mensaje;
	}

	/**
	 * Obtiene el correo electrónico del destinatario.
	 * 
	 * @return String con la dirección de correo.
	 */
	public String getDestinatario() {
		return destinatario;
	}

	/**
	 * Establece el correo electrónico del destinatario.
	 * 
	 * @param destinatario Nueva dirección de correo del destinatario.
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * Obtiene el asunto del correo.
	 * 
	 * @return String con el asunto.
	 */
	public String getAsunto() {
		return asunto;
	}

	/**
	 * Establece el asunto del correo.
	 * 
	 * @param asunto Nuevo asunto para el mensaje.
	 */
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	/**
	 * Obtiene el cuerpo o mensaje del correo.
	 * 
	 * @return String con el contenido del mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Establece el cuerpo o mensaje del correo.
	 * 
	 * @param mensaje Nuevo contenido para el mensaje.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
