package es.iespuertodelacruz.routinefights.shared.realtime;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;

/**
 * Something that happened for a user while the app is open, pushed through the
 * {@code userEvents} subscription.
 *
 * @param kind            {@link #MESSAGE}, {@link #READ}, {@link #TYPING} or
 *                        {@link #NOTIFICATION}
 * @param conversationId  chat it refers to, if any
 * @param message         the new message ({@link #MESSAGE})
 * @param readAt          when the other member read the chat ({@link #READ})
 * @param unreadNotifications unread notifications after the change
 * @param unreadMessages  chats with unread messages after the change
 */
public record UserEvent(String kind, String conversationId, ChatMessage message, String readAt,
        Integer unreadNotifications, Integer unreadMessages) {

    public static final String MESSAGE = "MESSAGE";
    public static final String READ = "READ";
    public static final String TYPING = "TYPING";
    public static final String NOTIFICATION = "NOTIFICATION";

    public static UserEvent message(ChatMessage message, int unreadMessages) {
        return new UserEvent(MESSAGE, message.conversationId(), message, null, null, unreadMessages);
    }

    public static UserEvent read(String conversationId, String readAt) {
        return new UserEvent(READ, conversationId, null, readAt, null, null);
    }

    public static UserEvent typing(String conversationId) {
        return new UserEvent(TYPING, conversationId, null, null, null, null);
    }

    public static UserEvent notification(int unreadNotifications) {
        return new UserEvent(NOTIFICATION, null, null, null, unreadNotifications, null);
    }
}
