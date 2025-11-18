package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TranslationsServiceTest {

    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        translationService = new TranslationService();
    }

    @Test
    void getTranslationsDirectorySuccess() {
        assertNull(translationService.getTranslationsDirectory());
    }

    @Test
    void translateSuccessNotArgs() {
        String translated = translationService.translate("greeting", "en-US", null);
        assertEquals("Hello", translated);
    }

    @Test
    void translateSuccessEmptyArgs() {
        String translated = translationService.translate("greeting", "es-ES", Collections.emptyMap());
        assertEquals("Hola", translated);
    }

    @Test
    void translateSuccessWithArgs() {
        String translatedEs = translationService.translate("test", "es-ES", Map.of("testArg", "mundo"));
        assertEquals("Prueba mundo", translatedEs);
        String translatedEn = translationService.translate("test", "en-US", Map.of("testArg", "world"));
        assertEquals("Test world", translatedEn);
    }

    @Test
    void getSupportedLanguagesSuccess() {
        var languages = translationService.getSupportedLanguages();
        assertNotNull(languages);
        assertTrue(languages.size() > 0);
    }
}
