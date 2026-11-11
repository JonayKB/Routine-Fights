package es.iespuertodelacruz.routinefights.chat.domain.ports.primary;

import java.util.List;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.chat.domain.Conversation;

public interface IChatService {

    /** Sends to an existing chat ({@code conversationId}) or to a user ({@code toEmail}). */
    ChatMessage send(String senderEmail, String conversationId, String toEmail, String text);

    List<Conversation> conversations(String email, int page, int perPage);

    /** The chat with that user, null when they never talked. */
    Conversation conversationWith(String email, String otherEmail);

    Conversation conversation(String email, String conversationId);

    List<ChatMessage> messages(String email, String conversationId, String before, int limit);

    void markRead(String email, String conversationId);

    void typing(String email, String conversationId);

    int unreadConversations(String email);
}
