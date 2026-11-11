package es.iespuertodelacruz.routinefights.chat.infrastructure.adapters.primary;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.chat.domain.ChatMessage;
import es.iespuertodelacruz.routinefights.chat.domain.Conversation;
import es.iespuertodelacruz.routinefights.chat.domain.ports.primary.IChatService;

/** Private messages between two users. */
@Controller
public class ChatResolver {

    private final IChatService chatService;

    public ChatResolver(IChatService chatService) {
        this.chatService = chatService;
    }

    static String me() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("conversations")
    public List<Conversation> conversations(@Argument Integer page, @Argument Integer perPage) {
        return chatService.conversations(me(), page == null ? 0 : page, perPage == null ? 30 : perPage);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("conversation")
    public Conversation conversation(@Argument String id) {
        return chatService.conversation(me(), id);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("conversationWith")
    public Conversation conversationWith(@Argument String email) {
        return chatService.conversationWith(me(), email);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("messages")
    public List<ChatMessage> messages(@Argument String conversationId, @Argument String before,
            @Argument Integer limit) {
        return chatService.messages(me(), conversationId, before, limit == null ? 40 : limit);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("unreadConversationsCount")
    public int unreadConversationsCount() {
        return chatService.unreadConversations(me());
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("sendMessage")
    public ChatMessage sendMessage(@Argument String conversationId, @Argument String toEmail,
            @Argument String text) {
        return chatService.send(me(), conversationId, toEmail, text);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("markConversationRead")
    public boolean markConversationRead(@Argument String conversationId) {
        chatService.markRead(me(), conversationId);
        return true;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("sendTyping")
    public boolean sendTyping(@Argument String conversationId) {
        chatService.typing(me(), conversationId);
        return true;
    }
}
