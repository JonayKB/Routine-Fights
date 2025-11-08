
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
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;

class NotificationsServiceTest {

    private static final String TOKEN1 = "token1";

    private static final String TOKEN2 = "token2";

    private static final String TOKEN = "token123";

    private static final String EN_MSG_EN = "en=msgEn";

    private static final String MSG_ES = "msgEs";

    private static final String MUNDO = "Mundo";

    private static final String HOLA = "Hola";

    private static final String WORLD = "World";

    private static final String HELLO = "Hello";

    private static final String ES = "es";

    private static final String MSG_EN = "msgEn";

    private static final String EN = "en";

    private static final String BODY_KEY = "body.key";

    private static final String TITLE_KEY = "title.key";

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

        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(List.of(EN));
        when(translationService.translate(TITLE_KEY, EN, args)).thenReturn(HELLO);
        when(translationService.translate(BODY_KEY, EN, args)).thenReturn(WORLD);
        when(fcm.send(any(Message.class))).thenReturn(MSG_EN);

        String result = notificationsService.sendToAllUsers(TITLE_KEY, BODY_KEY, args);

        assertEquals(EN_MSG_EN, result);
        verify(fcm, times(1)).send(any(Message.class));
        verify(translationService).getSupportedLanguages();
        verify(translationService).translate(TITLE_KEY, EN, args);
        verify(translationService).translate(BODY_KEY, EN, args);
    }

    @Test
    void testSendToAllUsersMultipleLanguagesSuccessConcatenated() throws Exception {
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Arrays.asList(EN, ES));
        when(translationService.translate(TITLE_KEY, EN, args)).thenReturn(HELLO);
        when(translationService.translate(BODY_KEY, EN, args)).thenReturn(WORLD);
        when(translationService.translate(TITLE_KEY, ES, args)).thenReturn(HOLA);
        when(translationService.translate(BODY_KEY, ES, args)).thenReturn(MUNDO);

        when(fcm.send(any(Message.class))).thenReturn(MSG_EN, MSG_ES);

        String result = notificationsService.sendToAllUsers(TITLE_KEY, BODY_KEY, args);

        assertEquals("en=msgEn; es=msgEs", result);
        verify(fcm, times(2)).send(any(Message.class));
    }

    @Test
    void testSendToAllUsersPartialFailure() throws Exception {
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Arrays.asList(EN, ES));
        when(translationService.translate(TITLE_KEY, EN, args)).thenReturn(HELLO);
        when(translationService.translate(BODY_KEY, EN, args)).thenReturn(WORLD);
        when(translationService.translate(TITLE_KEY, ES, args)).thenReturn(HOLA);
        when(translationService.translate(BODY_KEY, ES, args)).thenReturn(MUNDO);

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
                return MSG_EN;
            } else if ("general-es".equals(topic)) {
                throw fakeException;
            }
            return null;
        });

        String result = notificationsService.sendToAllUsers(TITLE_KEY, BODY_KEY, args);

        assertEquals(EN_MSG_EN, result);
        verify(fcm, times(2)).send(any(Message.class));
    }

    @Test
    void testSendToAllUsersAllFailuresReturnsNull() throws Exception {
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Arrays.asList(EN, ES));
        when(translationService.translate(anyString(), anyString(), any())).thenReturn("t", "b");

        FirebaseMessagingException fakeException = mock(FirebaseMessagingException.class);
        when(fakeException.getMessage()).thenReturn("failed");
        when(fakeException.getErrorCode()).thenReturn(ErrorCode.UNKNOWN);

        when(fcm.send(any(Message.class))).thenAnswer(invocation -> {
            throw fakeException;
        });

        String result = notificationsService.sendToAllUsers(TITLE_KEY, BODY_KEY, args);

        assertNull(result);
        verify(fcm, times(2)).send(any(Message.class));
    }

    @Test
    void testSendToAllUsersNoLanguagesReturnsNullAndNoSend() {
        String titleKey = TITLE_KEY;
        String bodyKey = BODY_KEY;
        Map<String, ?> args = Map.of();

        when(translationService.getSupportedLanguages()).thenReturn(Collections.emptyList());

        String result = notificationsService.sendToAllUsers(titleKey, bodyKey, args);

        assertNull(result);
        verifyNoInteractions(fcm);
    }

    @Test
    void testSendToUserSuccess() throws Exception {
        Map<String, ?> args = Map.of();
        DeviceToken userToken = new DeviceToken(TOKEN, null, EN);

        when(translationService.translate(TITLE_KEY, EN, args)).thenReturn(HELLO);
        when(translationService.translate(BODY_KEY, EN, args)).thenReturn(WORLD);
        when(fcm.send(any(Message.class))).thenReturn("msgToUser");
        String result = notificationsService.sendTo(TITLE_KEY, BODY_KEY, userToken, args);
        assertNull(result);
        verify(fcm, times(1)).send(any(Message.class));
    }

    @Test
    void testSendToUserFailureReturnsNull() throws Exception {
        Map<String, ?> args = Map.of();
        DeviceToken userToken = new DeviceToken(TOKEN, null, EN);

        when(translationService.translate(TITLE_KEY, EN, args)).thenReturn(HELLO);
        when(translationService.translate(BODY_KEY, EN, args)).thenReturn(WORLD);
        FirebaseMessagingException fakeException = mock(FirebaseMessagingException.class);
        when(fakeException.getMessage()).thenReturn("failed");
        when(fakeException.getErrorCode()).thenReturn(ErrorCode.UNKNOWN);
        when(fcm.send(any(Message.class))).thenThrow(fakeException);

        String result = notificationsService.sendTo(TITLE_KEY, BODY_KEY, userToken, args);

        assertNull(result);
        verify(fcm, times(1)).send(any(Message.class));
    }

    @Test
    void testSendToMultipleUsersSuccess() throws Exception {
        Map<String, ?> args = Map.of();
        DeviceToken userToken1 = new DeviceToken(TOKEN1, null, EN);
        DeviceToken userToken2 = new DeviceToken(TOKEN2, null, ES);
        List<DeviceToken> usersTokens = Arrays.asList(userToken1, userToken2);
        
        when(translationService.translate(TITLE_KEY, EN, args)).thenReturn(HELLO);
        when(translationService.translate(BODY_KEY, EN, args)).thenReturn(WORLD);
        when(translationService.translate(TITLE_KEY, ES, args)).thenReturn(HOLA);
        when(translationService.translate(BODY_KEY, ES, args)).thenReturn(MUNDO);

        BatchResponse mockBatchResponse = mock(BatchResponse.class);
        when(fcm.sendMulticast(any(MulticastMessage.class))).thenReturn(mockBatchResponse);
        when(mockBatchResponse.getSuccessCount()).thenReturn(2);
        when(mockBatchResponse.getFailureCount()).thenReturn(0);

        String result = notificationsService.sendTo(TITLE_KEY, BODY_KEY, usersTokens, args);
        assertNull(result);
        verify(fcm, times(2)).sendMulticast(any(MulticastMessage.class));
        verify(translationService, times(4)).translate(anyString(), anyString(), any());
    }
}