package top.offsetmonkey538.offsetconfig538.api.event;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.offsetconfig538.impl.event.EventImpl;

import java.util.function.Function;

/**
 * An event that storing handlers of type {@link T} that can be invoked.
 *
 * @param <T> the handler type
 */
@ApiStatus.NonExtendable
public interface Event<T> {

    /**
     * Creates a new event
     *
     * @param type the handler type
     * @param invokerFactory a function that turns the list of handlers into a handler. Should loop over all handlers and run them one by one. May have custom logic
     * @return a new event
     * @param <T> the handler type
     */
    static <T> @NotNull Event<T> createEvent(final Class<T> type, Function<T[], T> invokerFactory) {
        return new EventImpl<>(type, invokerFactory);
    }

    /**
     * Register a listener to this event.
     *
     * @param listener the listener to register
     */
    void listen(T listener);

    /**
     * Creates an invoker. This is used to actually invoke the event.
     *
     * @return an invoker to invoke this event.
     */
    T getInvoker();
}
