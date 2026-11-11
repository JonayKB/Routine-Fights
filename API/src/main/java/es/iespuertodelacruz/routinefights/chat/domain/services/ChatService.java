package es.iespuertodelacruz.routinefights.chat.domain.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.chat.domain.Conversation;
import es.iespuertodelacruz.routinefights.chat.domain.ports.primary.IChatService;
import es.iespuertodelacruz.routinefights.chat.domain.ports.secondary.IChatRepository;
import es.iespuertodelacruz.routinefights.notification.domain.ports.primary.INotificationService;
import es.iespuertodelacruz.routinefights.shared.dto.UserSummary;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEvent;
import es.iespuertodelacruz.routinefights.shared.realtime.UserEventBus;

/** One-to-one private messages, delivered live to open apps and by push otherwise. */
@Service
public class ChatService implements IChatService {

    public static final int MAX_LENGTH = 2000;
    static final int MAX_PAGE = 100;

    static final String NOT_FOUND = "Conversation not found";

    private final IChatRepository repository;
    private final UserEventBus bus;
    private final INotificationService notificationService;

    public ChatService(IChatRepository repository, UserEventBus bus, INotificationService notificationService) {
        this.repository = repository;
        this.bus = bus;
        this.notificationService = notificationService;
    }

    @Override
    public ChatMessage send(String senderEmail, String conversationId, String toEmail, String text) {
        String body = checkedText(text);
        LocalDateTime now = LocalDateTime.now();
        String chatId = conversationId != null && !conversationId.isBlank()
                ? existingChat(conversationId, senderEmail)
                : chatWith(senderEmail, toEmail, now);

        ChatMessage saved = repository.saveMessage(chatId, senderEmail, body, now);
        // Sending a message means the sender has seen everything before it.
        repository.markRead(chatId, senderEmail, now);

        String recipient = repository.otherMember(chatId, senderEmail);
        if (recipient != null) {
            deliver(saved, recipient, senderEmail, body);
        }
        // The sender's other devices show it too.
        bus.publish(senderEmail, UserEvent.ofMessage(saved.forReader(senderEmail),
                repository.countUnreadConversations(senderEmail)));
        return saved.forReader(senderEmail);
    }

    static String checkedText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        String body = text.strip();
        if (body.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Message is too long");
        }
        return body;
    }

    private String existingChat(String conversationId, String senderEmail) {
        requireMember(conversationId, senderEmail);
        return conversationId;
    }

    /** The chat with that user, started now if they never talked. */
    private String chatWith(String senderEmail, String toEmail, LocalDateTime now) {
        if (toEmail == null || toEmail.isBlank() || toEmail.equalsIgnoreCase(senderEmail)) {
            throw new IllegalArgumentException("You cannot message yourself");
        }
        String chatId = repository.findOrCreateConversation(senderEmail, toEmail, now);
        if (chatId == null) {
            throw new IllegalArgumentException("User not found");
        }
        return chatId;
    }

    /** Live to the recipient's open apps, and as a push. */
    private void deliver(ChatMessage saved, String recipient, String senderEmail, String body) {
        int unreadChats = repository.countUnreadConversations(recipient);
        bus.publish(recipient, UserEvent.ofMessage(saved.forReader(recipient), unreadChats));
        Conversation view = repository.findConversationView(saved.conversationId(), recipient);
        UserSummary sender = view == null ? new UserSummary(senderEmail, null, null) : view.other();
        notificationService.messageReceived(recipient, senderEmail, displayName(sender), saved.conversationId(),
                body, view == null ? 1 : view.unread(), unreadChats);
    }

    @Override
    public List<Conversation> conversations(String email, int page, int perPage) {
        int size = clamp(perPage);
        return repository.findConversations(email, Math.max(0, page) * size, size);
    }

    @Override
    public Conversation conversationWith(String email, String otherEmail) {
        String id = repository.findConversation(email, otherEmail);
        return id == null ? null : repository.findConversationView(id, email);
    }

    @Override
    public Conversation conversation(String email, String conversationId) {
        requireMember(conversationId, email);
        return repository.findConversationView(conversationId, email);
    }

    @Override
    public List<ChatMessage> messages(String email, String conversationId, String before, int limit) {
        requireMember(conversationId, email);
        LocalDateTime until = parse(before);
        return repository.findMessages(conversationId, until, clamp(limit)).stream()
                .map(m -> m.forReader(email))
                .toList();
    }

    @Override
    public void markRead(String email, String conversationId) {
        requireMember(conversationId, email);
        LocalDateTime now = LocalDateTime.now();
        repository.markRead(conversationId, email, now);
        String other = repository.otherMember(conversationId, email);
        bus.publish(other, UserEvent.ofRead(conversationId, now.toString()));
    }

    @Override
    public void typing(String email, String conversationId) {
        requireMember(conversationId, email);
        bus.publish(repository.otherMember(conversationId, email), UserEvent.ofTyping(conversationId));
    }

    @Override
    public int unreadConversations(String email) {
        return repository.countUnreadConversations(email);
    }

    private void requireMember(String conversationId, String email) {
        if (conversationId == null || !repository.isMember(conversationId, email)) {
            throw new IllegalArgumentException(NOT_FOUND);
        }
    }

    static int clamp(int value) {
        return Math.max(1, Math.min(value, MAX_PAGE));
    }

    static LocalDateTime parse(String value) {
        if (value == null || value.isBlank()) {
            return LocalDateTime.now().plusMinutes(1);
        }
        try {
            String plain = value.endsWith("Z") ? value.substring(0, value.length() - 1) : value;
            return LocalDateTime.parse(plain);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date");
        }
    }

    static String displayName(UserSummary user) {
        if (user.username() != null && !user.username().isBlank()) {
            return user.username();
        }
        return user.email() == null ? "" : user.email().split("@")[0];
    }
}
