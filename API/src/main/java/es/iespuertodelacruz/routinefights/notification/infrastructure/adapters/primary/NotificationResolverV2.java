package es.iespuertodelacruz.routinefights.notification.infrastructure.adapters.primary;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.notification.domain.Notification;
import es.iespuertodelacruz.routinefights.notification.domain.ports.primary.INotificationService;

/** The user's notification inbox and the devices that receive their pushes. */
@Controller
public class NotificationResolverV2 {

    private final INotificationService notificationService;
    private final IDeviceTokenService deviceTokenService;

    public NotificationResolverV2(INotificationService notificationService,
            IDeviceTokenService deviceTokenService) {
        this.notificationService = notificationService;
        this.deviceTokenService = deviceTokenService;
    }

    static String me() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("myNotifications")
    public List<Notification> myNotifications(@Argument Integer page, @Argument Integer perPage) {
        return notificationService.findMine(me(), page == null ? 0 : page, perPage == null ? 20 : perPage);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("unreadNotificationsCount")
    public int unreadNotificationsCount() {
        return notificationService.countUnread(me());
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("markNotificationsRead")
    public boolean markNotificationsRead(@Argument String id) {
        if (id == null) {
            notificationService.markAllRead(me());
        } else {
            notificationService.markRead(me(), id);
        }
        return true;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("registerDevice")
    public boolean registerDevice(@Argument String token, @Argument String language) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid device token");
        }
        deviceTokenService.save(me(), token, language == null ? "en-US" : language);
        return true;
    }

    /** Public on purpose: it runs on logout, when the session may already be gone. */
    @MutationMapping("unregisterDevice")
    public boolean unregisterDevice(@Argument String token) {
        deviceTokenService.delete(token);
        return true;
    }
}
