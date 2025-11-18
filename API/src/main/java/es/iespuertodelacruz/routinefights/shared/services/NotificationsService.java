package es.iespuertodelacruz.routinefights.shared.services;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import lombok.extern.java.Log;

@Service
@Log
public class NotificationsService {
    private static final String USERS = " users: ";
    private FirebaseMessaging fcm;
    private TranslationService translationService;

    public NotificationsService(FirebaseMessaging fcm, TranslationService translationService) {
        this.fcm = fcm;
        this.translationService = translationService;
    }

    public String sendToAllUsers(String titleKey, String bodyKey, Map<String, ?> args) {
        List<String> languages = translationService.getSupportedLanguages();
        StringBuilder results = new StringBuilder();

        for (String lang : languages) {
            String title = translationService.translate(titleKey, lang, args);
            String body = translationService.translate(bodyKey, lang, args);

            log.info("Preparing to send to language: general-" + lang);

            Message message = Message.builder()
                    .setTopic("general-" + lang)
                    .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                    .build();

            try {
                String sent = fcm.send(message);
                log.info("Sent message to all " + lang + USERS + sent);
                log.info("Notification Title: " + title);
                log.info("Notification Body: " + body);
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

    public String sendTo(String titleKey, String bodyKey, DeviceToken userToken, Map<String, Object> args) {
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
        }
        return null;
    }



    public String sendTo(String titleKey, String bodyKey, List<DeviceToken> usersTokens, Map<String, ?> args) {
        if (usersTokens == null || usersTokens.isEmpty()) {
            log.info("No user tokens provided for multicast message.");
            return null;
        }
        String title = null;
        String body = null;

        Map<String, List<DeviceToken>> grouped = usersTokens.stream()
                .collect(Collectors.groupingBy(DeviceToken::getLanguage));

        if (grouped.size() > 1) {
            for (Entry<String, List<DeviceToken>> entry : grouped.entrySet()) {
                String lang = entry.getKey();
                List<DeviceToken> tokensForLang = entry.getValue();
                if (tokensForLang == null || tokensForLang.isEmpty()) {
                    continue;
                }

                title = translationService.translate(titleKey, lang, args);
                body = translationService.translate(bodyKey, lang, args);

                MulticastMessage messageLang = MulticastMessage.builder()
                        .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                        .addAllTokens(tokensForLang.stream().map(DeviceToken::getToken).toList())
                        .build();
                try {
                    BatchResponse sent = fcm.sendMulticast(messageLang);
                    log.info("Sent message to " + lang + " users: Success:" + sent.getSuccessCount() + " Failures:"
                            + sent.getFailureCount());
                } catch (FirebaseMessagingException e) {
                    log.severe("Failed to send multicast to " + lang + USERS + e.getMessage());
                }
            }
            return null;
        } 
        return null;
    }
}
