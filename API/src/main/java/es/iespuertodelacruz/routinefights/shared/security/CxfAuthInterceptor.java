package es.iespuertodelacruz.routinefights.shared.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CxfAuthInterceptor extends AbstractPhaseInterceptor<Message> {

    private final JwtService jwtTokenManager;

    public CxfAuthInterceptor(JwtService jwtTokenManager) {
        super(Phase.PRE_INVOKE);
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        log.info("Mensaje de entrada:" + message);
        Map<String, List<String>> protocolHeaders = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);

        if (protocolHeaders != null && protocolHeaders.containsKey("Authorization")) {
            List<String> authorizationHeaders = protocolHeaders.get("Authorization");

            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String authorizationHeader = authorizationHeaders.get(0);
                log.info("Authorization Header: " + authorizationHeader);
                String token = authorizationHeader.replace("Bearer ", "").trim();
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

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } else {
            log.info("Authorization Header not found");
        }
    }
}
