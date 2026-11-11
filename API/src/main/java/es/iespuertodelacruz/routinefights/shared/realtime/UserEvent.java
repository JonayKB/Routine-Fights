package es.iespuertodelacruz.routinefights.shared.realtime;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;

/**
 * Something that happened for a user while the app is open, pushed through the
 * {@code userEvents} subscription.
 *
 * @param kind            {@link #KIND_MESSAGE}, {@link #KIND_READ}, {@link #KIND_TYPING} or
 *                        {@link #KIND_NOTIFICATION}
 * @param conversationId  chat it refers to, if any
 * @param message         the new message ({@link #KIND_MESSAGE})
 * @param readAt          when the other member read the chat ({@link #KIND_READ})
 * @param unreadNotifications unread notifications after the change
 * @param unreadMessages  chats with unread messages after the change
 */
public record UserEvent(String kind, String conversationId, ChatMessage message, String readAt,
        Integer unreadNotifications, Integer unreadMessages) {

    public static final String KIND_MESSAGE = "MESSAGE";
    public static final String KIND_READ = "READ";
    public static final String KIND_TYPING = "TYPING";
    public static final String KIND_NOTIFICATION = "NOTIFICATION";

    public static UserEvent ofMessage(ChatMessage message, int unreadMessages) {
        return new UserEvent(KIND_MESSAGE, message.conversationId(), message, null, null, unreadMessages);
    }

    public static UserEvent ofRead(String conversationId, String readAt) {
        return new UserEvent(KIND_READ, conversationId, null, readAt, null, null);
    }

    public static UserEvent ofTyping(String conversationId) {
        return new UserEvent(KIND_TYPING, conversationId, null, null, null, null);
    }

    public static UserEvent ofNotification(int unreadNotifications) {
        return new UserEvent(KIND_NOTIFICATION, null, null, null, unreadNotifications, null);
    }
}
