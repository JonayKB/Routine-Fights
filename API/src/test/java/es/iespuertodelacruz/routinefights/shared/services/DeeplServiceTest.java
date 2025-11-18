package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;

class DeeplServiceTest {
    private DeeplService deeplService;

    @Mock
    private DeepLClient deeplClientMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deeplService = new DeeplService(deeplClientMock);
    }

    @Test
    void testTranslateText() throws DeepLException, InterruptedException {
        String text = "Hello, world!";
        String targetLang = "es-ES";
        String expectedTranslation = "¡Hola, mundo!";

        when(deeplClientMock.translateText(text, null, targetLang)).thenReturn(
            new TextResult(expectedTranslation, "en-US", 0, null)
        );

        String actualTranslation = deeplService.translateText(text, targetLang);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    void testTranslateText_throwsDeepLException() throws DeepLException, InterruptedException {
        String text = "Hello, world!";
        String targetLang = "es-ES";

        when(deeplClientMock.translateText(text, null, targetLang)).thenThrow(new DeepLException("Translation error"));

        String actualTranslation = deeplService.translateText(text, targetLang);

        assertNull(actualTranslation);
    }

    @Test
    void testTranslateText_throwsInterruptedException() throws DeepLException, InterruptedException {
        String text = "Hello, world!";
        String targetLang = "es-ES";

        when(deeplClientMock.translateText(text, null, targetLang)).thenThrow(new InterruptedException("Translation interrupted"));

        String actualTranslation = deeplService.translateText(text, targetLang);

        assertNull(actualTranslation);
    }

}
