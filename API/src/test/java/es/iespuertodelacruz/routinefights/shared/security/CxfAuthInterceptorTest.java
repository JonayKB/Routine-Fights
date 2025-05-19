package es.iespuertodelacruz.routinefights.shared.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cxf.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import es.iespuertodelacruz.routinefights.shared.utils.JwtAuthenticationHelper;

import org.springframework.security.core.context.SecurityContextHolder;

class CxfAuthInterceptorTest {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String VALID_TOKEN = "validToken";
    private static final String INVALID_TOKEN = "invalidToken";
    private static final String MAIL = "test@example.com";
    private static final String ROLE = "ROLE_USER";

    @Mock
    private JwtService jwtService;

    @Mock
    private Message message;

    private CxfAuthInterceptor cxfAuthInterceptor;

    private JwtAuthenticationHelper jwtAuthenticationHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationHelper = new JwtAuthenticationHelper();
        cxfAuthInterceptor = new CxfAuthInterceptor(jwtService, jwtAuthenticationHelper);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testHandleMessageWithValidAuthorizationHeader() {
        Map<String, String> claims = new HashMap<>();
        claims.put("mail", MAIL);
        claims.put("role", ROLE);

        Map<String, List<String>> protocolHeaders = new HashMap<>();
        protocolHeaders.put(AUTHORIZATION, Collections.singletonList(BEARER_PREFIX + VALID_TOKEN));

        when(message.get(Message.PROTOCOL_HEADERS)).thenReturn(protocolHeaders);
        when(jwtService.validateAndGetClaims(VALID_TOKEN)).thenReturn(claims);

        cxfAuthInterceptor.handleMessage(message);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(MAIL, SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(ROLE)));
    }

    @Test
    void testHandleMessageWithoutAuthorizationHeader() {
        when(message.get(Message.PROTOCOL_HEADERS)).thenReturn(new HashMap<>());

        cxfAuthInterceptor.handleMessage(message);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testHandleMessageWithInvalidToken() {
        Map<String, List<String>> protocolHeaders = new HashMap<>();
        protocolHeaders.put(AUTHORIZATION, Collections.singletonList(BEARER_PREFIX + INVALID_TOKEN));

        when(message.get(Message.PROTOCOL_HEADERS)).thenReturn(protocolHeaders);
        when(jwtService.validateAndGetClaims(INVALID_TOKEN)).thenThrow(new RuntimeException("Invalid token"));

        assertThrows(RuntimeException.class, () -> cxfAuthInterceptor.handleMessage(message));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
