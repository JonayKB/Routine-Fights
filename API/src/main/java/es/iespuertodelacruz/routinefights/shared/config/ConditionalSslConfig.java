package es.iespuertodelacruz.routinefights.shared.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.java.Log;

@Configuration
@Log
public class ConditionalSslConfig {

    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${server.ssl.certificate:}")
    private String certPath;

    @Value("${server.ssl.certificate-private-key:}")
    private String keyPath;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sslCustomizer() {
        return factory -> {
            if (!sslEnabled) {
                factory.setSsl(null);
                return;
            }

            File certFile = new File(certPath);
            File keyFile = new File(keyPath);

            if (!certFile.exists() || !keyFile.exists()) {
                log.warning("SSL is enabled but certificate or key file not found. Disabling SSL.");
                factory.setSsl(null); // Desactiva SSL si no existen los archivos
            }
        };
    }
}
