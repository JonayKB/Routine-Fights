package es.iespuertodelacruz.routinefights.notification.domain.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.activity.commons.TimeRates;
import es.iespuertodelacruz.routinefights.chat.domain.ports.secondary.IChatRepository;
import es.iespuertodelacruz.routinefights.device_token.domain.DeviceToken;
import es.iespuertodelacruz.routinefights.device_token.domain.ports.primary.IDeviceTokenService;
import es.iespuertodelacruz.routinefights.notification.domain.Notification;
import es.iespuertodelacruz.routinefights.notification.domain.NotificationType;
import es.iespuertodelacruz.routinefights.notification.domain.ports.primary.INotificationService;
import es.iespuertodelacruz.routinefights.notification.domain.ports.secondary.INotificationRepository;
import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEvent;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEventBus;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService;
import es.iespuertodelacruz.routinefights.shared.services.NotificationsService.Push;
import lombok.extern.slf4j.Slf4j;

/**
 * Turns what happens in the app into inbox entries, live updates for open apps
 * and push notifications. Every public event method runs in the background and
 * never fails the action that caused it.
 */
@Service
@Slf4j
public class NotificationService implements INotificationService {

    static final int PREVIEW_LENGTH = 90;
    static final int MAX_PAGE = 50;

    private static final String ACTOR = "actor";
    private static final String OTHERS = "others";
    private static final String PREVIEW = "preview";
    private static final String TYPE = "type";

    private final INotificationRepository repository;
    private final IDeviceTokenService deviceTokenService;
    private final NotificationsService push;
    private final UserEventBus bus;
    private final IChatRepository chatRepository;

    public NotificationService(INotificationRepository repository, IDeviceTokenService deviceTokenService,
            NotificationsService push, UserEventBus bus, IChatRepository chatRepository) {
        this.repository = repository;
        this.deviceTokenService = deviceTokenService;
        this.push = push;
        this.bus = bus;
        this.chatRepository = chatRepository;
    }

    @Async
    @Override
    public void postLiked(String actorEmail, String postId) {
        safely(() -> {
            String owner = repository.postOwner(postId);
            if (owner == null || owner.equals(actorEmail)) {
                return;
            }
            Notification n = repository.accumulate(owner, NotificationType.LIKE, postId, actorEmail, null, null,
                    LocalDateTime.now());
            deliver(owner, n, "notifLikeTitle", many(n) ? "notifLikeMany" : "notifLikeOne", args(n));
        });
    }

    @Async
    @Override
    public void postUnliked(String actorEmail, String postId) {
        safely(() -> {
            String owner = repository.postOwner(postId);
            if (owner == null || owner.equals(actorEmail)) {
                return;
            }
            repository.retract(owner, NotificationType.LIKE, postId, actorEmail);
            bus.publish(owner, UserEvent.notification(repository.countUnread(owner)));
        });
    }

    @Async
    @Override
    public void postCommented(String actorEmail, String postId, String comment) {
        safely(() -> {
            String owner = repository.postOwner(postId);
            if (owner == null || owner.equals(actorEmail)) {
                return;
            }
            Notification n = repository.accumulate(owner, NotificationType.COMMENT, postId, actorEmail,
                    shorten(comment), null, LocalDateTime.now());
            deliver(owner, n, "notifCommentTitle", many(n) ? "notifCommentMany" : "notifCommentOne", args(n));
        });
    }

    @Async
    @Override
    public void userFollowed(String actorEmail, String followedEmail) {
        safely(() -> {
            if (followedEmail == null || followedEmail.equals(actorEmail)) {
                return;
            }
            Notification n = repository.accumulate(followedEmail, NotificationType.FOLLOW, followedEmail,
                    actorEmail, null, null, LocalDateTime.now());
            deliver(followedEmail, n, "notifFollowTitle", many(n) ? "notifFollowMany" : "notifFollowOne",
                    args(n));
        });
    }

    @Async
    @Override
    public void userUnfollowed(String actorEmail, String followedEmail) {
        safely(() -> {
            repository.retract(followedEmail, NotificationType.FOLLOW, followedEmail, actorEmail);
            bus.publish(followedEmail, UserEvent.notification(repository.countUnread(followedEmail)));
        });
    }

    @Override
    public void streakAtRisk(String email, String activityId, String activityName, long streak, long remaining,
            String timeRate, boolean lastCall) {
        safely(() -> {
            Notification n = repository.accumulate(email, NotificationType.STREAK_RISK, activityId, null,
                    activityName, String.valueOf(streak), LocalDateTime.now());
            Map<String, Object> args = new HashMap<>();
            args.put("activity", activityName);
            args.put("streak", streak);
            args.put("remaining", remaining);
            String title = lastCall ? "streakLastCallTitle" : "streakRiskTitle";
            deliver(email, n, title, streakBodyKey(timeRate, remaining), args);
        });
    }

    static String streakBodyKey(String timeRate, long remaining) {
        String period = switch (timeRate == null ? "" : timeRate) {
            case TimeRates.WEEKLY -> "Weekly";
            case TimeRates.MONTHLY -> "Monthly";
            case TimeRates.YEARLY -> "Yearly";
            default -> "Daily";
        };
        return "streakRiskBody" + period + (remaining > 1 ? "Many" : "");
    }

    @Async
    @Override
    public void messageReceived(String recipientEmail, String senderEmail, String senderName, String conversationId,
            String text, int unreadInChat, int unreadChats) {
        safely(() -> {
            List<DeviceToken> tokens = deviceTokenService.findByEmail(recipientEmail);
            if (tokens.isEmpty()) {
                return;
            }
            Map<String, Object> args = new HashMap<>();
            args.put("sender", senderName);
            args.put("text", shorten(text));
            args.put("count", unreadInChat);
            Map<String, String> data = new HashMap<>();
            data.put(TYPE, NotificationType.MESSAGE);
            data.put("conversationId", conversationId);
            data.put("email", senderEmail);
            data.put("name", senderName);
            int badge = repository.countUnread(recipientEmail) + unreadChats;
            push.push(tokens, new Push("notifMessageTitle",
                    unreadInChat > 1 ? "notifMessageMany" : "notifMessageOne", args, data,
                    NotificationType.MESSAGE + ":" + conversationId, badge));
        });
    }

    @Override
    public List<Notification> findMine(String email, int page, int perPage) {
        int size = Math.max(1, Math.min(perPage, MAX_PAGE));
        return repository.findByRecipient(email, Math.max(0, page) * size, size);
    }

    @Override
    public int countUnread(String email) {
        return repository.countUnread(email);
    }

    @Override
    public void markAllRead(String email) {
        repository.markAllRead(email);
        bus.publish(email, UserEvent.notification(0));
    }

    @Override
    public void markRead(String email, String id) {
        repository.markRead(email, id);
        bus.publish(email, UserEvent.notification(repository.countUnread(email)));
    }

    /** Tells the open apps and pushes to the user's devices. */
    private void deliver(String email, Notification n, String titleKey, String bodyKey, Map<String, ?> args) {
        int unread = repository.countUnread(email);
        bus.publish(email, UserEvent.notification(unread));

        List<DeviceToken> tokens = deviceTokenService.findByEmail(email);
        if (tokens.isEmpty()) {
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put(TYPE, n.type());
        data.put("targetId", n.targetId());
        data.put("notificationId", n.id());
        int badge = unread + chatRepository.countUnreadConversations(email);
        push.push(tokens, new Push(titleKey, bodyKey, args, data, n.type() + ":" + n.targetId(), badge));
    }

    static boolean many(Notification n) {
        return n.actorCount() > 1;
    }

    static Map<String, Object> args(Notification n) {
        Map<String, Object> args = new HashMap<>();
        UserSummary first = n.actors() == null || n.actors().isEmpty() ? null : n.actors().get(0);
        args.put(ACTOR, first == null ? "" : displayName(first));
        args.put(OTHERS, Math.max(0, n.actorCount() - 1));
        args.put(PREVIEW, n.preview() == null ? "" : n.preview());
        return args;
    }

    static String displayName(UserSummary user) {
        if (user.username() != null && !user.username().isBlank()) {
            return user.username();
        }
        return user.email() == null ? "" : user.email().split("@")[0];
    }

    static String shorten(String text) {
        if (text == null) {
            return "";
        }
        String flat = text.replaceAll("\\s+", " ").trim();
        return flat.length() <= PREVIEW_LENGTH ? flat : flat.substring(0, PREVIEW_LENGTH - 1) + "…";
    }

    private void safely(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException e) {
            log.warn("Notification failed: {}", e.getMessage());
        }
    }
}
