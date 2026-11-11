package es.iespuertodelacruz.routinefights.notification.domain.ports.primary;

import java.util.List;

import es.iespuertodelacruz.routinefights.notification.domain.Notification;

public interface INotificationService {

    void postLiked(String actorEmail, String postId);

    void postUnliked(String actorEmail, String postId);

    void postCommented(String actorEmail, String postId, String comment);

    void userFollowed(String actorEmail, String followedEmail);

    void userUnfollowed(String actorEmail, String followedEmail);

    /**
     * Warns that a streak goes out at the end of the current period.
     *
     * @param lastCall true for the last reminder of the day
     */
    void streakAtRisk(String email, String activityId, String activityName, long streak, long remaining,
            String timeRate, boolean lastCall);

    /** Push for a new chat message; chats have their own unread counter. */
    void messageReceived(String recipientEmail, String senderEmail, String senderName, String conversationId,
            String text, int unreadInChat, int unreadChats);

    List<Notification> findMine(String email, int page, int perPage);

    int countUnread(String email);

    void markAllRead(String email);

    void markRead(String email, String id);
}
