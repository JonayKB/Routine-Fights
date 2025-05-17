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

        assertDoesNotThrow(() -> mailService.sentVerifyToken(emailId, subject, body));

        verify(sendGrid, times(1)).api(any(Request.class));
    }
}

