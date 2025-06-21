package top.offsetmonkey538.offsetconfig538.api.event;

import java.lang.reflect.Array;
import java.util.function.Function;

/**
 * An event that storing handlers of type {@link T} that can be invoked.
 *
 * @param <T> the handler type
 */
public class Event<T> {
    /**
     * Function that turns the list of handlers into a handler. Should loop over all handlers and run them one by one. May have custom logic
     */
    private final Function<T[], T> invokerFactory;
    /**
     * List of handlers
     */
    private T[] handlers;

    /**
     * Creates a new event
     *
     * @param type the handler type
     * @param invokerFactory a function that turns the list of handlers into a handler. Should loop over all handlers and run them one by one. May have custom logic
     */
    @SuppressWarnings("unchecked")
    public Event(final Class<T> type, Function<T[], T> invokerFactory) {
        this.invokerFactory = invokerFactory;
        this.handlers = (T[]) Array.newInstance(type, 0);
    }

    /**
     * Register a listener to this event.
     *
     * @param listener the listener to register
     */
    public void listen(T listener) {
        @SuppressWarnings("unchecked")
        final T[] newArray = (T[]) Array.newInstance(handlers.getClass().getComponentType(), handlers.length + 1);

        System.arraycopy(handlers, 0, newArray, 0, handlers.length);
        newArray[handlers.length] = listener;

        this.handlers = newArray;
    }

    /**
     * Uses the {@link #invokerFactory} to create an invoker. This is used to actually invoke the event.
     *
     * @return an invoker to invoke this event.
     */
    public T getInvoker() {
        return invokerFactory.apply(handlers);
    }
}
