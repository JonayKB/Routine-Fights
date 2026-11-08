package es.iespuertodelacruz.routinefights.shared.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.objects.Email;
import es.iespuertodelacruz.routinefights.shared.exceptions.MailException;
import org.junit.jupiter.api.*;
import org.mockito.*;

class MailServiceTest {

    @Mock
    private SendGrid sendGrid;

    @Mock
    private Email fromEmail;

    @InjectMocks
    private MailService mailService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testDispatchEmail_Success() throws Exception {
        String emailId = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        Response mockResponse = new Response();
        mockResponse.setStatusCode(202);
        when(sendGrid.api(any(Request.class))).thenReturn(mockResponse);

        assertDoesNotThrow(() -> mailService.dispatchEmail(emailId, subject, body));

        verify(sendGrid, times(1)).api(any(Request.class));
    }

    @Test
    void testDispatchEmail_Failure() throws Exception {
        String emailId = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        Response mockResponse = new Response();
        mockResponse.setStatusCode(400);
        mockResponse.setBody("Bad Request");
        when(sendGrid.api(any(Request.class))).thenReturn(mockResponse);

        MailException exception = assertThrows(MailException.class, () -> 
            mailService.dispatchEmail(emailId, subject, body)
        );
        assertTrue(exception.getMessage().contains("Error sending email: Bad Request"));

        verify(sendGrid, times(1)).api(any(Request.class));
    }

    @Test
    void testSendVerificationEmail_Success()throws Exception {
        String emailId = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        Response mockResponse = new Response();
        mockResponse.setStatusCode(202);
        when(sendGrid.api(any(Request.class))).thenReturn(mockResponse);

        assertDoesNotThrow(() -> mailService.sendVerificationEmail(emailId, "ana", body, "en-EN", false));

        verify(sendGrid, times(1)).api(any(Request.class));
    }

    @Test
    void verificationLinkUsesPublicUrl() throws Exception {
        Response ok = new Response();
        ok.setStatusCode(202);
        ArgumentCaptor<Request> sent = ArgumentCaptor.forClass(Request.class);
        when(sendGrid.api(sent.capture())).thenReturn(ok);

        mailService.sendVerificationEmail("ana+test@example.com", "ana", "tok-123", null, false);

        assertTrue(sent.getValue().getBody()
                .contains("https://routinefights.jonaykb.com/auth/verify/ana+test@example.com/tok-123"),
                sent.getValue().getBody());
        assertFalse(sent.getValue().getBody().contains("64.226.71.234"));
    }

    @Test
    void verificationEmailFollowsUserLanguage() throws Exception {
        Response ok = new Response();
        ok.setStatusCode(202);
        ArgumentCaptor<Request> sent = ArgumentCaptor.forClass(Request.class);
        when(sendGrid.api(sent.capture())).thenReturn(ok);

        mailService.sendVerificationEmail("ana@example.com", "ana", "tok-123", "es-ES", true);

        String body = sent.getValue().getBody();
        assertTrue(body.contains("Confirma tu nuevo correo en Routine Fights"), body);
        assertTrue(body.contains("/auth/verify/ana@example.com/tok-123?lang=es"), body);
    }

    @Test
    void verificationEmailEscapesUsername() throws Exception {
        Response ok = new Response();
        ok.setStatusCode(202);
        ArgumentCaptor<Request> sent = ArgumentCaptor.forClass(Request.class);
        when(sendGrid.api(sent.capture())).thenReturn(ok);

        mailService.sendVerificationEmail("ana@example.com", "<b>ana</b>", "tok-123", "en", false);

        assertFalse(sent.getValue().getBody().contains("<b>ana</b>"));
    }
}
