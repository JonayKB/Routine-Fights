package es.iespuertodelacruz.routinefights.shared.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

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

	/** Public base URL of the site, used to build the link in the verification e-mail. */
	@Value("${app.public-url:https://routinefights.jonaykb.com}")
	private String publicUrl = "https://routinefights.jonaykb.com";

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

		String html = String.format(HTMLTemplates.VERIFICATION_EMAIL, verifyBaseUrl(),
				UriUtils.encodePathSegment(destinatario, StandardCharsets.UTF_8),
				UriUtils.encodePathSegment(token, StandardCharsets.UTF_8));

		dispatchEmail(destinatario, asunto, html);
	}

	String verifyBaseUrl() {
		return publicUrl.endsWith("/") ? publicUrl.substring(0, publicUrl.length() - 1) : publicUrl;
	}

}
