package es.iespuertodelacruz.routinefights.shared.services;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import lombok.extern.java.Log;

@Service
@Log
public class NotificationsService {
    private static final String USERS = " users: ";
    /** Topic every app subscribes to, one per language prefix ("general-es"). */
    public static final String GENERAL_TOPIC = "general-";
    /** Android notification channel created by the app. */
    public static final String ANDROID_CHANNEL = "activity";
    /** Accent of the app: tints the small icon of Android notifications. */
    public static final String ANDROID_COLOR = "#C2410C";

    /**
     * A push for one user. {@code tag} groups the notifications of the same thing
     * (a post, a chat): a newer one replaces the older instead of piling up.
     */
    public record Push(String titleKey, String bodyKey, Map<String, ?> args, Map<String, String> data, String tag,
            Integer badge) {
    }

    private final FirebaseMessaging fcm;
    private final TranslationService translationService;
    private IDeviceTokenService deviceTokenService;

    public NotificationsService(FirebaseMessaging fcm, TranslationService translationService) {
        this.fcm = fcm;
        this.translationService = translationService;
    }

    @Autowired
    public NotificationsService(FirebaseMessaging fcm, TranslationService translationService,
            IDeviceTokenService deviceTokenService) {
        this(fcm, translationService);
        this.deviceTokenService = deviceTokenService;
    }

    /** "es-ES" → "es": the part of a language tag used for topics. */
    public static String languagePrefix(String language) {
        if (language == null || language.isBlank()) {
            return "en";
        }
        return language.split("[-_]")[0].toLowerCase();
    }

    public String sendToAllUsers(String titleKey, String bodyKey, Map<String, ?> args) {
        List<String> languages = translationService.getSupportedLanguages();
        StringBuilder results = new StringBuilder();

        for (String lang : languages) {
            String title = translationService.translate(titleKey, lang, args);
            String body = translationService.translate(bodyKey, lang, args);
            String topic = GENERAL_TOPIC + languagePrefix(lang);

            log.info("Preparing to send to topic: " + topic);

            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                    .build();

            try {
                String sent = fcm.send(message);
                log.info("Sent message to all " + lang + USERS + sent);
                if (!results.isEmpty()) {
                    results.append("; ");
                }
                results.append(lang).append("=").append(sent);
            } catch (FirebaseMessagingException e) {
                log.severe("Failed to send message to " + lang + USERS + e.getMessage());
            }
        }

        return !results.isEmpty() ? results.toString() : null;
    }

    public String sendTo(String titleKey, String bodyKey, DeviceToken userToken, Map<String, ?> args) {
        String title = translationService.translate(titleKey, userToken.getLanguage(), args);
        String body = translationService.translate(bodyKey, userToken.getLanguage(), args);

        Message message = Message.builder()
                .setToken(userToken.getToken())
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .build();

        try {
            String sent = fcm.send(message);
            log.info("Sent message to user: " + sent);
        } catch (FirebaseMessagingException e) {
            log.severe("Failed to send message to user: " + e.getMessage());
            forgetIfDead(userToken.getToken(), e.getMessagingErrorCode());
        }
        return null;
    }

    public String sendTo(String titleKey, String bodyKey, List<DeviceToken> usersTokens, Map<String, ?> args) {
        push(usersTokens, new Push(titleKey, bodyKey, args, Map.of(), null, null));
        return null;
    }

    /**
     * Sends a push to every device given, each in its own language. Devices FCM
     * reports as gone are forgotten so they are not tried again.
     *
     * @return number of devices that accepted it
     */
    public int push(List<DeviceToken> tokens, Push push) {
        if (tokens == null || tokens.isEmpty()) {
            log.info("No user tokens provided for multicast message.");
            return 0;
        }
        Map<String, List<DeviceToken>> grouped = tokens.stream()
                .collect(Collectors.groupingBy(t -> t.getLanguage() == null ? "en-US" : t.getLanguage()));

        int delivered = 0;
        for (Entry<String, List<DeviceToken>> entry : grouped.entrySet()) {
            String lang = entry.getKey();
            List<String> ids = entry.getValue().stream().map(DeviceToken::getToken).toList();
            MulticastMessage message = buildMessage(push, lang, ids);
            try {
                BatchResponse sent = fcm.sendEachForMulticast(message);
                log.info("Sent message to " + lang + " users: Success:" + sent.getSuccessCount() + " Failures:"
                        + sent.getFailureCount());
                delivered += sent.getSuccessCount();
                forgetDead(ids, sent);
            } catch (FirebaseMessagingException e) {
                log.severe("Failed to send multicast to " + lang + USERS + e.getMessage());
            }
        }
        return delivered;
    }

    private MulticastMessage buildMessage(Push push, String lang, List<String> ids) {
        String title = translationService.translate(push.titleKey(), lang, push.args());
        String body = translationService.translate(push.bodyKey(), lang, push.args());

        AndroidNotification.Builder android = AndroidNotification.builder().setChannelId(ANDROID_CHANNEL)
                .setColor(ANDROID_COLOR);
        Aps.Builder aps = Aps.builder().setSound("default");
        ApnsConfig.Builder apns = ApnsConfig.builder();
        if (push.tag() != null) {
            android.setTag(push.tag());
            aps.setThreadId(push.tag());
            // apns-collapse-id may not exceed 64 bytes.
            apns.putHeader("apns-collapse-id", push.tag().length() > 64 ? push.tag().substring(0, 64) : push.tag());
        }
        if (push.badge() != null) {
            aps.setBadge(push.badge());
        }

        MulticastMessage.Builder builder = MulticastMessage.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(android.build())
                        .build())
                .setApnsConfig(apns.setAps(aps.build()).build())
                .addAllTokens(ids);
        if (push.data() != null && !push.data().isEmpty()) {
            builder.putAllData(push.data());
        }
        return builder.build();
    }

    private void forgetDead(List<String> ids, BatchResponse sent) {
        List<SendResponse> responses = sent.getResponses();
        if (responses == null) {
            return;
        }
        for (int i = 0; i < responses.size() && i < ids.size(); i++) {
            SendResponse response = responses.get(i);
            if (!response.isSuccessful() && response.getException() != null) {
                forgetIfDead(ids.get(i), response.getException().getMessagingErrorCode());
            }
        }
    }

    private void forgetIfDead(String token, MessagingErrorCode code) {
        if (deviceTokenService != null
                && (code == MessagingErrorCode.UNREGISTERED || code == MessagingErrorCode.INVALID_ARGUMENT)) {
            deviceTokenService.delete(token);
        }
    }
}
