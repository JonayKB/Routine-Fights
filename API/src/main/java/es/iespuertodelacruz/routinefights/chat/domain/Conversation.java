package es.iespuertodelacruz.routinefights.chat.domain;

import java.time.LocalDateTime;

import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;

/**
 * A one-to-one chat as seen by one of its two members.
 *
 * @param other       the other member
 * @param unread      messages from the other member the reader hasn't seen
 * @param otherReadAt last time the other member opened the chat (read receipts)
 */
public record Conversation(String id, UserSummary other, ChatMessage lastMessage, int unread,
        LocalDateTime updatedAt, LocalDateTime otherReadAt) {
}
