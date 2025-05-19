package es.iespuertodelacruz.routinefights.shared.security;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import org.springframework.security.core.userdetails.UserDetails;
import es.iespuertodelacruz.routinefights.shared.utils.JwtAuthenticationHelper;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CxfAuthInterceptor extends AbstractPhaseInterceptor<Message> {

    private final JwtService jwtTokenManager;
    private final JwtAuthenticationHelper jwtAuthenticationHelper;

    public CxfAuthInterceptor(JwtService jwtTokenManager,
            JwtAuthenticationHelper jwtAuthenticationHelper) {
        super(Phase.PRE_INVOKE);
        this.jwtTokenManager = jwtTokenManager;
        this.jwtAuthenticationHelper = jwtAuthenticationHelper;
    }

    public static final String AUTH_PREFIX = "Bearer ";

    @Override
    public void handleMessage(Message message) throws Fault {
        log.info("Mensaje de entrada:" + message);
        Map<String, List<String>> protocolHeaders = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);

        if (protocolHeaders != null && protocolHeaders.containsKey("Authorization")) {
            try {
                List<String> authorizationHeaders = protocolHeaders.get("Authorization");

                if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                    String header = authorizationHeaders.get(0);
                    String token = jwtAuthenticationHelper.extractTokenFromHeader(header, AUTH_PREFIX);
                    Map<String, String> mapInfoToken = jwtTokenManager.validateAndGetClaims(token);
                    final String correo = mapInfoToken.get("mail");
                    final String rol = mapInfoToken.get("role");

                    UserDetails userDetails = jwtAuthenticationHelper.buildUserDetails(correo, rol);
                    jwtAuthenticationHelper.setAuthentication(userDetails, null, null, null);
                }
            } catch (IOException | ServletException e) {
                log.error("Error in JWT validation: " + e.getMessage());
                throw new Fault(e);

            }
        } else {
            log.info("Authorization Header not found");
        }
    }
}
