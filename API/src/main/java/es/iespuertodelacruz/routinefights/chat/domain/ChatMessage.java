package es.iespuertodelacruz.routinefights.chat.domain;

import java.time.LocalDateTime;

/** A private message. {@code mine} is filled for whoever reads it. */
public record ChatMessage(String id, String conversationId, String text, String senderEmail, LocalDateTime createdAt,
        boolean mine) {

    public ChatMessage forReader(String readerEmail) {
        return new ChatMessage(id, conversationId, text, senderEmail, createdAt,
                senderEmail != null && senderEmail.equals(readerEmail));
    }
}
