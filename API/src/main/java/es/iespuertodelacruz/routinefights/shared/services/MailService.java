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

	/**
	 * Sends the verification e-mail in the user's language (falls back to English).
	 *
	 * @param newEmail true when the user changed their address rather than signed up
	 */
	public void sendVerificationEmail(String to, String username, String token, String language,
			boolean newEmail) {
		String lang = HTMLTemplates.resolveLanguage(language);
		String link = verifyBaseUrl() + "/auth/verify/"
				+ UriUtils.encodePathSegment(to, StandardCharsets.UTF_8) + "/"
				+ UriUtils.encodePathSegment(token, StandardCharsets.UTF_8) + "?lang=" + lang;

		dispatchEmail(to, HTMLTemplates.verificationSubject(lang, newEmail),
				HTMLTemplates.verificationEmail(lang, username, link, newEmail));
	}

	String verifyBaseUrl() {
		return publicUrl.endsWith("/") ? publicUrl.substring(0, publicUrl.length() - 1) : publicUrl;
	}

}
