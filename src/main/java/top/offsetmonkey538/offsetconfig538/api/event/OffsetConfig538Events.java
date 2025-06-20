package top.offsetmonkey538.offsetconfig538.api.event;

import blue.endless.jankson.Jankson;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigManager;

import java.lang.reflect.Array;
import java.util.function.Function;

public final class OffsetConfig538Events {
    private OffsetConfig538Events() {

    }

    /**
     * Event for configuring jankson <i>globally</i>. This event is invoked every time a {@link Jankson.Builder} is created.
     * @see ConfigManager#load(ConfigHolder)
     * @see ConfigManager#save(ConfigHolder)
     */
    public static final Event<JanksonConfigurationEvent> JANKSON_CONFIGURATION_EVENT = new Event<>(JanksonConfigurationEvent.class, handlers -> builder -> {
        for (final JanksonConfigurationEvent handler : handlers) handler.configureBuilder(builder);
    });


    /**
     * Handler for {@link #JANKSON_CONFIGURATION_EVENT}.
     */
    @FunctionalInterface
    public interface JanksonConfigurationEvent {
        void configureBuilder(final @NotNull Jankson.Builder builder);
    }


    public static class Event<T> {
        private final Function<T[], T> invokerFactory;
        private T[] handlers;

        @SuppressWarnings("unchecked")
        public Event(final Class<T> type, Function<T[], T> invokerFactory) {
            this.invokerFactory = invokerFactory;
            this.handlers = (T[]) Array.newInstance(type, 0);
        }

        public void listen(T listener) {
            @SuppressWarnings("unchecked")
            final T[] newArray = (T[]) Array.newInstance(handlers.getClass().getComponentType(), handlers.length + 1);

            System.arraycopy(handlers, 0, newArray, 0, handlers.length);
            newArray[handlers.length] = listener;

            this.handlers = newArray;
        }

        public T getInvoker() {
            return invokerFactory.apply(handlers);
        }
    }
}
