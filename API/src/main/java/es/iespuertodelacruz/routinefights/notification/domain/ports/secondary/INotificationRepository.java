package es.iespuertodelacruz.routinefights.notification.domain.ports.secondary;

import java.time.LocalDateTime;
import java.util.List;

import es.iespuertodelacruz.routinefights.notification.domain.Notification;

public interface INotificationRepository {

    /**
     * Adds the event to the unread entry of that type and target, creating it if
     * there is none, and returns the entry as it is now.
     */
    Notification accumulate(String recipientEmail, String type, String targetId, String actorEmail, String preview,
            String extra, LocalDateTime at);

    /** Takes the actor back out of the unread entry (unlike, unfollow). */
    void retract(String recipientEmail, String type, String targetId, String actorEmail);

    List<Notification> findByRecipient(String email, int skip, int limit);

    int countUnread(String email);

    void markAllRead(String email);

    void markRead(String email, String id);

    /** E-mail of the author of a post, null when the post doesn't exist. */
    String postOwner(String postId);

    long deleteOlderThan(LocalDateTime limit);
}
