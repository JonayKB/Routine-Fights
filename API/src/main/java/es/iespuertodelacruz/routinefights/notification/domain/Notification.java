package es.iespuertodelacruz.routinefights.notification.domain;

import java.time.LocalDateTime;
import java.util.List;

import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;

/**
 * An entry of the in-app notification inbox. Events of the same kind about the
 * same thing pile up in one entry while it is unread ("Ana and 3 others liked
 * your post") instead of creating one entry each.
 *
 * @param type        one of {@link NotificationType}
 * @param targetId    what it is about: a post, an activity, the user
 * @param actors      most recent people who caused it, newest first (max 3)
 * @param actorCount  how many different people caused it
 * @param preview     short text (last comment, activity name)
 * @param extra       extra value (streak length...)
 * @param targetImage picture of the target post, when there is one
 */
public record Notification(String id, String type, String targetId, List<UserSummary> actors, int actorCount,
        String preview, String extra, String targetImage, LocalDateTime updatedAt, boolean read) {
}
