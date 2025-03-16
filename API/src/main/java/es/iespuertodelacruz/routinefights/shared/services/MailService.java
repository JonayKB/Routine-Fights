package es.iespuertodelacruz.routinefights.shared.services;


import org.springframework.stereotype.Service;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import es.iespuertodelacruz.routinefights.shared.exceptions.MailException;
import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates;

@Service
public class MailService {
	private static final String EMAIL_ENDPOINT = "mail/send";

	private final SendGrid sendGrid;
	private final Email fromEmail;

	public MailService(SendGrid sendGrid, Email fromEmail) {
		this.sendGrid = sendGrid;
		this.fromEmail = fromEmail;
	}

	public void dispatchEmail(String emailId, String subject, String body) {
		Email toEmail = new Email(emailId);
		Content content = new Content("text/html", body);
		Mail mail = new Mail(fromEmail, subject, toEmail, content);
		

		Request request = new Request();
		request.setMethod(Method.POST);
		request.setEndpoint(EMAIL_ENDPOINT);
		try {
			request.setBody(mail.build());

			Response response = sendGrid.api(request);
			if (response.getStatusCode() != 202) {
				throw new MailException("Error sending email: " + response.getBody());
			}
		} catch (Exception e) {
			throw new MailException("Error sending email: " + e);
		}
	}

	public void sentVerifyToken(String destinatario, String asunto, String token) {

		String html = String.format(HTMLTemplates.VERIFICATION_EMAIL, destinatario, token);

		dispatchEmail(destinatario, asunto, html);
	}

}
