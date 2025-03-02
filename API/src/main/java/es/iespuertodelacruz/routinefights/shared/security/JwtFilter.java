package es.iespuertodelacruz.routinefights.shared.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_PREFIX = "Bearer ";

    private final JwtService jwtTokenManager;

    public JwtFilter(JwtService jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTH_HEADER);

        String[] allowedRoutes = { "/swagger-ui.html",
                "/swagger-ui/", "/v2/",
                "configuration/", "/swagger",
                "/graphiql",
                "/webjars/", "/auth/",
                "/api/register", "/v3/",
                "/graphiql", "/graphql/schema",
                "/websocket", "/index.html", "/h2-console"
        };
        String path = request.getRequestURI();

        for (String ruta : allowedRoutes) {
            if (path.startsWith(ruta)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        if (header == null && path.startsWith("/graphql")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (header != null && header.startsWith(AUTH_PREFIX)) {

            String token = header.substring(AUTH_PREFIX.length());
            try {
                Map<String, String> mapInfoToken = jwtTokenManager.validateAndGetClaims(token);
                final String correo = mapInfoToken.get("mail");
                final String rol = mapInfoToken.get("role");
                UserDetails userDetails = new UserDetails() {

                    String username = correo;

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        List<GrantedAuthority> authorities = new ArrayList<>();

                        authorities.add(new SimpleGrantedAuthority(rol));
                        return authorities;
                    }

                    @Override
                    public String getPassword() {
                        return null;
                    }

                    @Override
                    public String getUsername() {
                        return username;
                    }

                };

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);

            } catch (JWTVerificationException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid token.\"}");

            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"You are not authenticated. Login on host/api/login.\"}");
        }

    }
}
