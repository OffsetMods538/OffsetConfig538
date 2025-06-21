package top.offsetmonkey538.offsetconfig538.api.event;

import blue.endless.jankson.Jankson;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigManager;

/**
 * Contains events for OffsetConfig538
 */
public final class OffsetConfig538Events {
    private OffsetConfig538Events() {

    }

    /**
     * Event for configuring jankson <i>globally</i>. This event is invoked every time a {@link Jankson.Builder} is created.
     * <p>
     *     This can be used to add global serializers and whatever
     * </p>
     *
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
        /**
         * Configures the provided {@link Jankson.Builder}.
         *
         * @param builder the builder to configure.
         */
        void configureBuilder(final @NotNull Jankson.Builder builder);
    }
}
