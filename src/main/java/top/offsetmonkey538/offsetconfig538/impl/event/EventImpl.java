package top.offsetmonkey538.offsetconfig538.impl.event;

import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.offsetconfig538.api.event.Event;

import java.lang.reflect.Array;
import java.util.function.Function;

/**
 * An {@link Event} implementation storing handlers in an array.
 *
 * @param <T> the handler type
 */
@ApiStatus.Internal
public class EventImpl<T> implements Event<T> {
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
    public EventImpl(final Class<T> type, Function<T[], T> invokerFactory) {
        this.invokerFactory = invokerFactory;
        this.handlers = (T[]) Array.newInstance(type, 0);
    }

    @Override
    public void listen(T listener) {
        @SuppressWarnings("unchecked")
        final T[] newArray = (T[]) Array.newInstance(handlers.getClass().getComponentType(), handlers.length + 1);

        System.arraycopy(handlers, 0, newArray, 0, handlers.length);
        newArray[handlers.length] = listener;

        this.handlers = newArray;
    }

    @Override
    public T getInvoker() {
        return invokerFactory.apply(handlers);
    }
}
