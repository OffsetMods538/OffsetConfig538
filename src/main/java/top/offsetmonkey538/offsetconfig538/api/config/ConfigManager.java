package top.offsetmonkey538.offsetconfig538.api.config;

import org.jetbrains.annotations.*;
import top.offsetmonkey538.offsetconfig538.impl.config.ConfigManagerImpl;

import java.util.Collection;

/**
 * Has methods for loading and saving configs
 */
@ApiStatus.NonExtendable
public interface ConfigManager {
    /**
     * Instance to access all them methods through
     */
    ConfigManager INSTANCE = new ConfigManagerImpl();

    /**
     * Initializes the provided {@link ConfigHolder}.
     * <p>
     *     Initializing means first trying to {@link #load(ConfigHolder) load} the config if possible, and then {@link #save(ConfigHolder) saving} it.
     * </p>
     * <p>
     *     Uses the {@link ErrorHandler} from the config holder.
     * </p>
     *
     * @param configHolder the {@link ConfigHolder} to initialize.
     * @return the provided {@link ConfigHolder}, in case you want to inline initialization and creating the holder.
     * @param <T> your {@link Config} class
     */
    default <T extends Config> @NotNull ConfigHolder<T> init(@NotNull ConfigHolder<T> configHolder) {
        return init(configHolder, configHolder.getErrorHandler());
    }

    /**
     * Initializes the provided {@link ConfigHolder}.
     * <p>
     *     Initializing means first trying to {@link #load(ConfigHolder, ErrorHandler) load} the config if possible, and then {@link #save(ConfigHolder, ErrorHandler) saving} it.
     * </p>
     *
     * @param configHolder the {@link ConfigHolder} to initialize.
     * @param errorHandler the {@link ErrorHandler} to use
     * @return the provided {@link ConfigHolder}, in case you want to inline initialization and creating the holder.
     * @param <T> your {@link Config} class
     */
    <T extends Config> @NotNull ConfigHolder<T> init(@NotNull ConfigHolder<T> configHolder, @NotNull ErrorHandler errorHandler);

    /**
     * Loads a config from disk into the provided {@link ConfigHolder} and applies any required {@link Datafixer}s.
     * <p>
     *     Uses the {@link ErrorHandler} from the config holder.
     * </p>
     *
     * @param configHolder the {@link ConfigHolder} to load
     * @param <T> your {@link Config} class
     */
    @Contract // pure=false is default
    default <T extends Config> void load(@NotNull ConfigHolder<T> configHolder) {
        load(configHolder, configHolder.getErrorHandler());
    }

    /**
     * Loads a config from disk into the provided {@link ConfigHolder} and applies any required {@link Datafixer}s.
     *
     * @param configHolder the {@link ConfigHolder} to load
     * @param errorHandler the {@link ErrorHandler} to use
     * @param <T> your {@link Config} class
     */
    <T extends Config> void load(@NotNull ConfigHolder<T> configHolder, @NotNull ErrorHandler errorHandler);

    /**
     * Saves the currently held config to disk from the provided {@link ConfigHolder} also writes the config version for datafixing.
     * <p>
     *     Uses the {@link ErrorHandler} from the config holder.
     * </p>
     *
     * @param configHolder the {@link ConfigHolder} to load
     * @param <T> your {@link Config} class
     */
    @Contract // pure=false is default
    default <T extends Config> void save(@NotNull ConfigHolder<T> configHolder) {
        save(configHolder, configHolder.getErrorHandler());
    }

    /**
     * Saves the currently held config to disk from the provided {@link ConfigHolder} also writes the config version for datafixing.
     *
     * @param configHolder the {@link ConfigHolder} to load
     * @param errorHandler the {@link ErrorHandler} to use
     * @param <T> your {@link Config} class
     */
    <T extends Config> void save(@NotNull ConfigHolder<T> configHolder, @NotNull ErrorHandler errorHandler);
}
