package es.iespuertodelacruz.routinefights.shared.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.shared.exceptions.MailException;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	private final JavaMailSender sender;

	public MailService(JavaMailSender sender) {
		this.sender = sender;
	}

	@Value("${mail.from}")
	private String mailfrom;

	public void send(String[] destinatarios, String asunto, String contenidoHtml) {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom(mailfrom);
			helper.setTo(destinatarios);
			helper.setSubject(asunto);
			helper.setText(contenidoHtml, true);

			sender.send(message);

		} catch (MessagingException e) {
			throw new MailException("Error al enviar el correo electrónico" + e);
		}
	}

	public void sentVerifyToken(String destinatario, String asunto, String token) {

		String html = String.format(HTMLTemplates.VERIFICATION_EMAIL, destinatario, token);

		send(new String[] { destinatario }, asunto, html);
	}

}
