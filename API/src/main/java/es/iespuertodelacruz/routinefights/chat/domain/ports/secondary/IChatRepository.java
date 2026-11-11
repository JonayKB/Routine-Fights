package es.iespuertodelacruz.routinefights.chat.domain.ports.secondary;

import java.time.LocalDateTime;
import java.util.List;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.chat.domain.Conversation;

public interface IChatRepository {

    /** The chat between both users, created if they never talked. Null if a user doesn't exist. */
    String findOrCreateConversation(String email, String otherEmail, LocalDateTime at);

    /** The chat between both users, or null. */
    String findConversation(String email, String otherEmail);

    boolean isMember(String conversationId, String email);

    /** E-mail of the other member of the chat. */
    String otherMember(String conversationId, String email);

    ChatMessage saveMessage(String conversationId, String senderEmail, String text, LocalDateTime at);

    List<Conversation> findConversations(String email, int skip, int limit);

    Conversation findConversationView(String conversationId, String email);

    /** Messages older than {@code before}, newest first. */
    List<ChatMessage> findMessages(String conversationId, LocalDateTime before, int limit);

    void markRead(String conversationId, String email, LocalDateTime at);

    int countUnreadMessages(String conversationId, String email);

    int countUnreadConversations(String email);
}
