package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

class MailServiceTest {

    private static final String HTML_CONTAINS = "Hello";

    private static final String SENDING_ERROR = "Sending error";

    private static final String HTML = "<h1>Hello</h1>";

    private static final String TEST_SUBJECT = "Test Subject";

    private static final String TEST_EMAIL = "test@example.com";

    private static final String RECIPIENT_EMAIL = "recipient@example.com";

    @Mock
    private JavaMailSender javaMailSender;

    private MailService mailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mailService = new MailService(javaMailSender);
        mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        Field mailFromField = MailService.class.getDeclaredField("mailfrom");
        mailFromField.setAccessible(true);
        mailFromField.set(mailService, TEST_EMAIL);
    }

    @Test
    void testSendSuccess() throws Exception {
        String[] destinatarios = { RECIPIENT_EMAIL };
        String asunto = TEST_SUBJECT;
        String contenidoHtml = HTML;

        mailService.send(destinatarios, asunto, contenidoHtml);

        verify(javaMailSender, times(1)).send(mimeMessage);

        assertEquals(asunto, mimeMessage.getSubject());

        Address[] fromAddresses = mimeMessage.getFrom();
        assertNotNull(fromAddresses);
        assertTrue(fromAddresses.length > 0);
        assertEquals(TEST_EMAIL, fromAddresses[0].toString());

        Address[] toAddresses = mimeMessage.getRecipients(Message.RecipientType.TO);
        assertNotNull(toAddresses);
        assertEquals(RECIPIENT_EMAIL, toAddresses[0].toString());

        Object content = mimeMessage.getContent();
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            String body = ((Multipart) multipart.getBodyPart(0).getContent()).getBodyPart(0).getContent().toString();
            assertTrue(body.contains(HTML_CONTAINS));
        } else {
            String body = (String) content;
            assertTrue(body.contains(HTML_CONTAINS));
        }
    }

    @Test
    void testSendThrowsMailException() {
        doThrow(new MailSendException(SENDING_ERROR)).when(javaMailSender).send(mimeMessage);

        String[] destinatarios = { RECIPIENT_EMAIL };
        String asunto = TEST_SUBJECT;
        String contenidoHtml = HTML;

        Exception exception = assertThrows(MailSendException.class, () -> {
            mailService.send(destinatarios, asunto, contenidoHtml);
        });

        String expectedMessage = SENDING_ERROR;
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testSentVerifyToken() {
        MailService spyMailService = spy(mailService);

        String destinatario = "verify@example.com";
        String asunto = "Verification";
        String token = "123456";

        spyMailService.sentVerifyToken(destinatario, asunto, token);

        ArgumentCaptor<String[]> captorDestinatarios = ArgumentCaptor.forClass(String[].class);
        ArgumentCaptor<String> captorAsunto = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> captorContenidoHtml = ArgumentCaptor.forClass(String.class);

        verify(spyMailService, times(1))
                .send(captorDestinatarios.capture(), captorAsunto.capture(), captorContenidoHtml.capture());

        String[] capturedDestinatarios = captorDestinatarios.getValue();
        String capturedAsunto = captorAsunto.getValue();
        String capturedContenidoHtml = captorContenidoHtml.getValue();

        assertArrayEquals(new String[] { destinatario }, capturedDestinatarios);
        assertEquals(asunto, capturedAsunto);
        assertTrue(capturedContenidoHtml.contains(token));
    }
}
