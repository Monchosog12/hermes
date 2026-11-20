package co.edu.unbosque.hermes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de gestionar el envío de correos electrónicos a como el
 * envío de los resultados de traduccion y mejoras del código
 */

@Service
public class CorreoService {
	
	@Autowired
	private JavaMailSender mail;
	
	
	 /**
     * Envía un correo electrónico de texto plano.
     * 
     * @param destinatario Correo electrónico de la persona que recibe.
     * @param asunto       Asunto del correo.
     * @param contenido    Cuerpo del mensaje (por ejemplo, el código traducido).
     */
	public void enviarCorreoRespuesta(String destinatario,String asunto,String contenido) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo(destinatario);
		mensaje.setSubject(asunto);
		mensaje.setText(contenido);
		
		mail.send(mensaje);
	}


}
