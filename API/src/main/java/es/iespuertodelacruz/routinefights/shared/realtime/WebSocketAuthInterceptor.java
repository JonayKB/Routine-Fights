package es.iespuertodelacruz.routinefights.shared.realtime;

import java.util.Map;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketGraphQlRequest;
import org.springframework.graphql.server.WebSocketSessionInfo;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.auth0.jwt.exceptions.JWTVerificationException;

import es.iespuertodelacruz.routinefights.shared.security.JwtService;
import reactor.core.publisher.Mono;

/**
 * Authenticates GraphQL WebSocket connections. The app sends its token in the
 * {@code connection_init} payload ({@code {"Authorization": "Bearer ..."}});
 * the e-mail in it is kept for the session and handed to every operation as
 * the {@value #EMAIL} context value.
 */
@Component
public class WebSocketAuthInterceptor implements WebSocketGraphQlInterceptor {

    public static final String EMAIL = "email";
    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;

    public WebSocketAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    @NonNull
    public Mono<Object> handleConnectionInitialization(@NonNull WebSocketSessionInfo info,
            @NonNull Map<String, Object> payload) {
        String email = emailFrom(payload);
        if (email == null) {
            return Mono.error(new IllegalStateException("Unauthorized"));
        }
        info.getAttributes().put(EMAIL, email);
        return Mono.empty();
    }

    @Override
    @NonNull
    public Mono<WebGraphQlResponse> intercept(@NonNull WebGraphQlRequest request,
            @NonNull WebGraphQlInterceptor.Chain chain) {
        if (request instanceof WebSocketGraphQlRequest ws) {
            Object email = ws.getSessionInfo().getAttributes().get(EMAIL);
            if (email != null) {
                request.configureExecutionInput((input, builder) -> builder
                        .graphQLContext(Map.of(EMAIL, email))
                        .build());
            }
        }
        return chain.next(request);
    }

    String emailFrom(Map<String, Object> payload) {
        if (payload == null) {
            return null;
        }
        Object raw = payload.getOrDefault("Authorization", payload.get("authorization"));
        if (!(raw instanceof String header) || header.isBlank()) {
            return null;
        }
        String token = header.startsWith(BEARER) ? header.substring(BEARER.length()) : header;
        try {
            return jwtService.validateAndGetClaims(token).get("mail");
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
