package es.iespuertodelacruz.routinefights.shared.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

class ConditionalSslConfigTest {

    @Test
    void testSslDisabledWhenFilesMissing() {
        ConditionalSslConfig config = new ConditionalSslConfig();

        setField(config, "sslEnabled", true);
        setField(config, "certPath", "/path/inexistente/cert.pem");
        setField(config, "keyPath", "/path/inexistente/key.pem");

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        config.sslCustomizer().customize(factory);

        assertNull(factory.getSsl());
    }

    @Test
    void testSslRemainsEnabledWhenFilesExist() throws IOException {
        File cert = File.createTempFile("cert", ".pem");
        File key = File.createTempFile("key", ".pem");

        ConditionalSslConfig config = new ConditionalSslConfig();
        setField(config, "sslEnabled", true);
        setField(config, "certPath", cert.getAbsolutePath());
        setField(config, "keyPath", key.getAbsolutePath());

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        config.sslCustomizer().customize(factory);

        assertNotNull(factory);
        assertNull(factory.getSsl());

        cert.delete();
        key.delete();
    }

    @Test
    void testSslDisabledWhenPropertyIsFalse() {
        ConditionalSslConfig config = new ConditionalSslConfig();
        setField(config, "sslEnabled", false);

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        config.sslCustomizer().customize(factory);

        assertNull(factory.getSsl(), "SSL debería desactivarse si server.ssl.enabled=false");
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
