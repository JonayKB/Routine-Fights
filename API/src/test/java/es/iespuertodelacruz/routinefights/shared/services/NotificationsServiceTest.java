
package es.iespuertodelacruz.routinefights.shared.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

class NotificationsServiceTest {

    @Mock
    private FirebaseMessaging fcm;

    @Mock
    private TranslationService translationService;

    private NotificationsService notificationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationsService = new NotificationsService(fcm, translationService);
    }

    @Test
    void testSendToAllUsersSingleLanguageSuccess() throws Exception {
        String titleKey = "title.key";
        String bodyKey = "body.key";
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(List.of("en"));
        when(translationService.translate(titleKey, "en", args)).thenReturn("Hello");
        when(translationService.translate(bodyKey, "en", args)).thenReturn("World");
        when(fcm.send(any(Message.class))).thenReturn("msgEn");

        String result = notificationsService.sendToAllUsers(titleKey, bodyKey, args);

        assertEquals("en=msgEn", result);
        verify(fcm, times(1)).send(any(Message.class));
        verify(translationService).getSupportedLanguages();
        verify(translationService).translate(titleKey, "en", args);
        verify(translationService).translate(bodyKey, "en", args);
    }

    @Test
    void testSendToAllUsersMultipleLanguagesSuccessConcatenated() throws Exception {
        String titleKey = "title.key";
        String bodyKey = "body.key";
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Arrays.asList("en", "es"));
        when(translationService.translate(titleKey, "en", args)).thenReturn("Hello");
        when(translationService.translate(bodyKey, "en", args)).thenReturn("World");
        when(translationService.translate(titleKey, "es", args)).thenReturn("Hola");
        when(translationService.translate(bodyKey, "es", args)).thenReturn("Mundo");

        when(fcm.send(any(Message.class))).thenReturn("msgEn", "msgEs");

        String result = notificationsService.sendToAllUsers(titleKey, bodyKey, args);

        assertEquals("en=msgEn; es=msgEs", result);
        verify(fcm, times(2)).send(any(Message.class));
    }

    @Test
    void testSendToAllUsersPartialFailure() throws Exception {
        String titleKey = "title.key";
        String bodyKey = "body.key";
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Arrays.asList("en", "es"));
        when(translationService.translate(titleKey, "en", args)).thenReturn("Hello");
        when(translationService.translate(bodyKey, "en", args)).thenReturn("World");
        when(translationService.translate(titleKey, "es", args)).thenReturn("Hola");
        when(translationService.translate(bodyKey, "es", args)).thenReturn("Mundo");

        FirebaseMessagingException fakeException = mock(FirebaseMessagingException.class);
        when(fakeException.getErrorCode()).thenReturn(ErrorCode.INVALID_ARGUMENT);
        when(fakeException.getMessage()).thenReturn("failed es");
        when(fcm.send(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            String topic = null;
            try {
                java.lang.reflect.Field f = msg.getClass().getDeclaredField("topic");
                f.setAccessible(true);
                topic = (String) f.get(msg);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }

            if ("general-en".equals(topic)) {
                return "msgEn";
            } else if ("general-es".equals(topic)) {
                throw fakeException;
            }
            return null;
        });

        String result = notificationsService.sendToAllUsers(titleKey, bodyKey, args);

        assertEquals("en=msgEn", result);
        verify(fcm, times(2)).send(any(Message.class));
    }

    @Test
    void testSendToAllUsersAllFailuresReturnsNull() throws Exception {
        String titleKey = "title.key";
        String bodyKey = "body.key";
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Arrays.asList("en", "es"));
        when(translationService.translate(anyString(), anyString(), any())).thenReturn("t", "b");

        FirebaseMessagingException fakeException = mock(FirebaseMessagingException.class);
        when(fakeException.getMessage()).thenReturn("failed");
        when(fakeException.getErrorCode()).thenReturn(ErrorCode.UNKNOWN);

        when(fcm.send(any(Message.class))).thenAnswer(invocation -> {
            throw fakeException;
        });

        String result = notificationsService.sendToAllUsers(titleKey, bodyKey, args);

        assertNull(result);
        verify(fcm, times(2)).send(any(Message.class));
    }

    @Test
    void testSendToAllUsersNoLanguagesReturnsNullAndNoSend() {
        String titleKey = "title.key";
        String bodyKey = "body.key";
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Collections.emptyList());

        String result = notificationsService.sendToAllUsers(titleKey, bodyKey, args);

        assertNull(result);
        verifyNoInteractions(fcm);
    }
}
