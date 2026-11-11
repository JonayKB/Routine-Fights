package es.iespuertodelacruz.routinefights.shared.realtime;

import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;

/** Live events for the signed-in user over the GraphQL WebSocket. */
@Controller
public class RealtimeResolver {

    private final UserEventBus bus;

    public RealtimeResolver(UserEventBus bus) {
        this.bus = bus;
    }

    @SubscriptionMapping("userEvents")
    public Flux<UserEvent> userEvents(
            @ContextValue(name = WebSocketAuthInterceptor.EMAIL, required = false) String email) {
        if (email == null) {
            throw new AccessDeniedException("Access Denied");
        }
        // Quiet connections are kept open by spring.graphql.websocket.keep-alive.
        return bus.listen(email);
    }
}
