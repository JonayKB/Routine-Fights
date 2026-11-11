package es.iespuertodelacruz.routinefights.shared.graphql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;

/** Admin-only push notifications (replaces the unauthenticated POST /notifications/**). */
@Controller
public class NotificationResolver {

    public record KeyValue(String key, String value) {
    }

    private final NotificationsService notificationsService;

    public NotificationResolver(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
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
