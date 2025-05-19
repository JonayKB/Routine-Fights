package es.iespuertodelacruz.routinefights.shared.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import es.iespuertodelacruz.routinefights.shared.utils.JwtAuthenticationHelper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

class JwtFilterTest {

    private static final String USER_ROLE = "ROLE_USER";

    private static final String USER_MAIL = "user@example.com";

    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        JwtAuthenticationHelper jwtAuthenticationHelper = new JwtAuthenticationHelper();
        jwtFilter = new JwtFilter(jwtService, jwtAuthenticationHelper);

        SecurityContextHolder.clearContext();
    }

    @Test
    void testAllowedRoute() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/swagger-ui.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testGraphQLWithNullHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/graphql/someQuery");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testMissingAuthHeaderNonGraphQL() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        String content = response.getContentAsString();
        assertTrue(content.contains("You are not authenticated"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testValidToken() throws ServletException, IOException {
        String token = "validToken";
        String header = "Bearer " + token;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", header);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Map<String, String> claims = new HashMap<>();
        claims.put("mail", USER_MAIL);
        claims.put("role", USER_ROLE);
        when(jwtService.validateAndGetClaims(token)).thenReturn(claims);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(USER_MAIL, auth.getName());
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        assertFalse(authorities.isEmpty());
        assertEquals(USER_ROLE, authorities.iterator().next().getAuthority());
    }

    @Test
    void testInvalidToken() throws ServletException, IOException {
        String token = "invalidToken";
        String header = "Bearer " + token;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", header);
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.validateAndGetClaims(token))
                .thenThrow(new JWTVerificationException("Invalid token"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        String content = response.getContentAsString();
        assertTrue(content.contains("Invalid token."));
    }
}
