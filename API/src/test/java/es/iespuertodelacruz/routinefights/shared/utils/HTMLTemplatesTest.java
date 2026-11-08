package es.iespuertodelacruz.routinefights.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.shared.utils.HTMLTemplates.VerifyResult;

class HTMLTemplatesTest {

    @Test
    void resolvesSupportedLanguages() {
        assertEquals("es", HTMLTemplates.resolveLanguage("es-ES"));
        assertEquals("fr", HTMLTemplates.resolveLanguage("fr_FR"));
        assertEquals("de", HTMLTemplates.resolveLanguage("it-IT,de;q=0.8,en;q=0.5"));
        assertEquals("en", HTMLTemplates.resolveLanguage("ja-JP"));
        assertEquals("en", HTMLTemplates.resolveLanguage(null));
        assertEquals("en", HTMLTemplates.resolveLanguage(""));
    }

    @Test
    void verifyPageIsLocalizedAndSupportsDarkMode() {
        String page = HTMLTemplates.verifyPage("fr-FR", VerifyResult.VERIFIED);
        assertTrue(page.contains("lang=\"fr\""));
        assertTrue(page.contains("E-mail confirmé"));
        assertTrue(page.contains("prefers-color-scheme: dark"));
    }

    @Test
    void emailSubjectDependsOnLanguageAndCase() {
        assertEquals("Confirm your email for Routine Fights", HTMLTemplates.verificationSubject(null, false));
        assertEquals("Bestätige deine neue E-Mail für Routine Fights", HTMLTemplates.verificationSubject("de", true));
    }
}
