package es.iespuertodelacruz.routinefights.shared.services;

import org.springframework.stereotype.Service;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;

import es.iespuertodelacruz.routinefights.shared.exceptions.DeeplException;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@Log
public class DeeplService {
    private final DeepLClient deeplClient;

    public DeeplService() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("keystore/deeplKey");
        if (is == null) {
            throw new IllegalStateException("deeplKey resource not found in classpath");
        }
        String key;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            key = br.readLine();
        } catch (IOException e) {
            throw new DeeplException("Failed to read deeplKey resource", e);
        }
        deeplClient = new DeepLClient(key);
    }

    public String translateText(String text, String targetLang) {
        try {
            return deeplClient.translateText(text, null, targetLang).getText();
        } catch (DeepLException e) {
            log.severe("Translation failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.severe("Translation interrupted: " + e.getMessage());
        }
        return null;
    }

}
