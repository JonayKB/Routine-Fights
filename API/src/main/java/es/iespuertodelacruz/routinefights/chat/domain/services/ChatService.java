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
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        String body = text.strip();
        if (body.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Message is too long");
        }
        LocalDateTime now = LocalDateTime.now();

        String chatId;
        if (conversationId != null && !conversationId.isBlank()) {
            if (!repository.isMember(conversationId, senderEmail)) {
                throw new IllegalArgumentException(NOT_FOUND);
            }
            chatId = conversationId;
        } else {
            if (toEmail == null || toEmail.isBlank() || toEmail.equalsIgnoreCase(senderEmail)) {
                throw new IllegalArgumentException("You cannot message yourself");
            }
            chatId = repository.findOrCreateConversation(senderEmail, toEmail, now);
            if (chatId == null) {
                throw new IllegalArgumentException("User not found");
            }
        }

        ChatMessage saved = repository.saveMessage(chatId, senderEmail, body, now);
        // Sending a message means the sender has seen everything before it.
        repository.markRead(chatId, senderEmail, now);

        String recipient = repository.otherMember(chatId, senderEmail);
        if (recipient != null) {
            int unreadChats = repository.countUnreadConversations(recipient);
            bus.publish(recipient, UserEvent.message(saved.forReader(recipient), unreadChats));
            Conversation view = repository.findConversationView(chatId, recipient);
            UserSummary sender = view == null ? new UserSummary(senderEmail, null, null) : view.other();
            notificationService.messageReceived(recipient, senderEmail, displayName(sender), chatId, body,
                    view == null ? 1 : view.unread(), unreadChats);
        }
        // The sender's other devices show it too.
        bus.publish(senderEmail, UserEvent.message(saved.forReader(senderEmail),
                repository.countUnreadConversations(senderEmail)));
        return saved.forReader(senderEmail);
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
        bus.publish(other, UserEvent.read(conversationId, now.toString()));
    }

    @Override
    public void typing(String email, String conversationId) {
        requireMember(conversationId, email);
        bus.publish(repository.otherMember(conversationId, email), UserEvent.typing(conversationId));
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
