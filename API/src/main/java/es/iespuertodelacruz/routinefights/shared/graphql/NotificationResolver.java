package es.iespuertodelacruz.routinefights.shared.graphql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService.PushText;

/** Admin-only push notifications (replaces the unauthenticated POST /notifications/**). */
@Controller
public class NotificationResolver {

    public record KeyValue(String key, String value) {
    }

    /** Hand-written text for one language ("es"); "default" for the rest. */
    public record PushTextInput(String language, String title, String body) {
    }

    public record LanguageCount(String language, long devices) {
    }

    static final int MAX_TITLE = 120;
    static final int MAX_BODY = 1000;
    static final String DEFAULT = "default";

    private final NotificationsService notificationsService;
    private IDeviceTokenService deviceTokenService;

    public NotificationResolver(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @Autowired
    public NotificationResolver(NotificationsService notificationsService, IDeviceTokenService deviceTokenService) {
        this(notificationsService);
        this.deviceTokenService = deviceTokenService;
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("broadcastNotification")
    public String broadcastNotification(@Argument List<PushTextInput> texts) {
        Map<String, PushText> byLanguage = new HashMap<>();
        PushText fallback = null;
        for (PushTextInput text : texts == null ? List.<PushTextInput>of() : texts) {
            PushText checked = validate(text.title(), text.body());
            if (checked == null) {
                continue;
            }
            String language = text.language() == null ? DEFAULT : text.language();
            if (DEFAULT.equals(language)) {
                fallback = checked;
            } else {
                byLanguage.put(NotificationsService.languagePrefix(language), checked);
            }
        }
        if (byLanguage.isEmpty() && fallback == null) {
            throw new IllegalArgumentException("Write at least a title");
        }
        String result = notificationsService.broadcast(byLanguage, fallback);
        if (result == null) {
            // Shown in the panel: the usual cause is the server's Firebase key.
            throw new IllegalArgumentException("Firebase rejected the notification, check the server logs");
        }
        return result;
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("notifyUser")
    public int notifyUser(@Argument String email, @Argument String title, @Argument String body) {
        PushText text = validate(title, body);
        if (text == null) {
            throw new IllegalArgumentException("Write at least a title");
        }
        List<DeviceToken> devices = deviceTokenService.findByEmail(email);
        if (devices.isEmpty()) {
            throw new IllegalArgumentException("That user has no device registered for notifications");
        }
        return notificationsService.pushText(devices, text.title(), text.body());
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("pushDevices")
    public List<LanguageCount> pushDevices() {
        return deviceTokenService.countByLanguage().entrySet().stream()
                .map(e -> new LanguageCount(e.getKey(), e.getValue()))
                .toList();
    }

    @Secured("ROLE_ADMIN")
    @QueryMapping("pushLanguages")
    public List<String> pushLanguages() {
        return notificationsService.supportedPrefixes();
    }

    static PushText validate(String title, String body) {
        if (title == null || title.isBlank()) {
            return null;
        }
        if (title.length() > MAX_TITLE || (body != null && body.length() > MAX_BODY)) {
            throw new IllegalArgumentException("The notification is too long");
        }
        return new PushText(title.strip(), body == null ? "" : body.strip());
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("sendNotificationToAll")
    public String sendNotificationToAll(@Argument String titleKey, @Argument String bodyKey,
            @Argument List<KeyValue> args) {
        return notificationsService.sendToAllUsers(titleKey, bodyKey, toMap(args));
    }

    @Secured("ROLE_ADMIN")
    @MutationMapping("sendNotificationToDevice")
    public String sendNotificationToDevice(@Argument String titleKey, @Argument String bodyKey,
            @Argument String deviceToken, @Argument String language, @Argument List<KeyValue> args) {
        return notificationsService.sendTo(titleKey, bodyKey, new DeviceToken(deviceToken, null, language),
                toMap(args));
    }

    static Map<String, String> toMap(List<KeyValue> args) {
        Map<String, String> map = new HashMap<>();
        if (args != null) {
            args.forEach(kv -> map.put(kv.key(), kv.value()));
        }
        return map;
    }
}
