package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;

class TranslationsServiceTest {

    private TranslationService translationService;

    @Mock
    private ResourceLoader resourceLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            Path tempDir = Files.createTempDirectory("translations_test");
            File enFile = tempDir.resolve("en_US.json").toFile();
            File esFile = tempDir.resolve("es_ES.json").toFile();
            Files.write(enFile.toPath(), "{\"greeting\":\"Hello\",\"test\":\"Test {testArg}\"}".getBytes(StandardCharsets.UTF_8));
            Files.write(esFile.toPath(), "{\"greeting\":\"Hola\",\"test\":\"Prueba {testArg}\"}".getBytes(StandardCharsets.UTF_8));
            when(resourceLoader.getResource("classpath:translations"))
                    .thenReturn(new FileSystemResource(tempDir.toFile()));
            translationService = new TranslationService(resourceLoader);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getTranslationsDirectorySuccess() {
        File directory = translationService.getTranslationsDirectory();
        assertNotNull(directory);
        assertTrue(directory.listFiles().length > 0);
    }

    @Test
    void setTranslationsDirectorySuccess() {
        File newDirectory = new File("src/main/resources/translations/");
        translationService.setTranslationsDirectory(newDirectory);
        File directory = translationService.getTranslationsDirectory();
        assertNotNull(directory);

        assertEquals(directory, newDirectory);
    }

    @Test
    void translateSuccessNotArgs() {
        String translated = translationService.translate("greeting", "en_US", null);
        assertEquals("Hello", translated);
    }

    @Test
    void translateSuccessEmptyArgs() {
        String translated = translationService.translate("greeting", "es_ES", Collections.emptyMap());
        assertEquals("Hola", translated);
    }

    @Test
    void translateSuccessWithArgs() {
        String translatedEs = translationService.translate("test", "es_ES", Map.of("testArg", "mundo"));
        assertEquals("Prueba mundo", translatedEs);
        String translatedEn = translationService.translate("test", "en_US", Map.of("testArg", "world"));
        assertEquals("Test world", translatedEn);
    }

    @Test
    void getSupportedLanguagesSuccess() {
        var languages = translationService.getSupportedLanguages();
        assertNotNull(languages);
        assertTrue(languages.size() > 0);
    }
}
