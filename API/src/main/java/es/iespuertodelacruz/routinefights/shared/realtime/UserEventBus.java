package es.iespuertodelacruz.routinefights.shared.realtime;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * In-memory fan-out of {@link UserEvent}s to the open connections of each user
 * (a user may have the app open on several devices). Events for users that are
 * not connected are dropped: they get a push notification instead.
 */
@Component
public class UserEventBus {

    private static final class Channel {
        final Sinks.Many<UserEvent> sink = Sinks.many().multicast().directBestEffort();
        final AtomicInteger listeners = new AtomicInteger();
    }

    private final Map<String, Channel> channels = new ConcurrentHashMap<>();

    /** Events for {@code email} from now on, until the subscriber cancels. */
    public Flux<UserEvent> listen(String email) {
        return Flux.defer(() -> {
            Channel channel = channels.compute(email, (k, existing) -> {
                Channel c = existing == null ? new Channel() : existing;
                c.listeners.incrementAndGet();
                return c;
            });
            return channel.sink.asFlux().doFinally(signal -> release(email, channel));
        });
    }

    private void release(String email, Channel channel) {
        channels.computeIfPresent(email, (k, c) -> {
            if (c != channel) {
                return c;
            }
            return c.listeners.decrementAndGet() <= 0 ? null : c;
        });
    }

    public void publish(String email, UserEvent event) {
        if (email == null) {
            return;
        }
        Channel channel = channels.get(email);
        if (channel != null) {
            // Several threads may publish at once; retry briefly instead of failing.
            channel.sink.emitNext(event, Sinks.EmitFailureHandler.busyLooping(Duration.ofMillis(50)));
        }
    }

    public boolean isOnline(String email) {
        return email != null && channels.containsKey(email);
    }
}
