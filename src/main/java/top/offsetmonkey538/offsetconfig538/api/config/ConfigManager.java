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
     * Initializes the provided {@link ConfigHolder}. No other methods should be called before this.
     *
     * @param configHolder the {@link ConfigHolder} to initialize.
     * @return the provided {@link ConfigHolder}, in case you want to inline initialization and creating the holder.
     * @param <T> your {@link Config} class
     * @see #load(ConfigHolder)
     * @see #save(ConfigHolder)
     */
    <T extends Config> @NotNull ConfigHolder<T> init(@NotNull ConfigHolder<T> configHolder);

    /**
     * Loads a config from disk into the provided {@link ConfigHolder} and applies any required {@link Datafixer}s.
     *
     * @param configHolder the {@link ConfigHolder} to load
     * @param <T> your {@link Config} class
     * @see #init(ConfigHolder)
     * @see #save(ConfigHolder)
     */
    @Contract // pure=false is default
    <T extends Config> void load(@NotNull ConfigHolder<T> configHolder);

    /**
     * Saves the currently held config to disk from the provided {@link ConfigHolder} also writes the config version for datafixing.
     *
     * @param configHolder the {@link ConfigHolder} to load
     * @param <T> your {@link Config} class
     * @see #init(ConfigHolder)
     * @see #load(ConfigHolder)
     */
    @Contract // pure=false is default
    <T extends Config> void save(@NotNull ConfigHolder<T> configHolder);

    /**
     * Gets a {@link ConfigHolder} by its id, or {@code null} if it hasn't been initialized with {@link #init(ConfigHolder)}
     * <br>
     * Unlike {@link #get(String, Class)}, this method returns a {@link ConfigHolder} for a generic {@link Config} class.
     *
     * @param id the id of the {@link ConfigHolder}
     * @return a {@link ConfigHolder} corresponding to the provided id.
     * @see #get(String, Class)
     */
    @Contract(pure = true)
    @Nullable ConfigHolder<? extends Config> get(@NotNull String id);

    /**
     * Gets a {@link ConfigHolder} by its id, or {@code null} if it hasn't been initialized with {@link #init(ConfigHolder)} or doesn't match the provided {@link Class}.
     * <br>
     * Unlike {@link #get(String)}, this method validates a {@link ConfigHolder} against the provided {@link Class}.
     *
     * @param id the id of the {@link ConfigHolder}
     * @param configClass the class of your {@link Config}
     * @return a {@link ConfigHolder} corresponding to the provided id and holding a {@link Config} of the provided {@link Class}.
     * @param <T> your {@link Config} class
     * @see #get(String)
     */
    @Contract(pure = true)
    <T extends Config> @Nullable ConfigHolder<T> get(@NotNull String id, @NotNull Class<T> configClass);

    /**
     * Returns a collection of all {@link ConfigHolder}s initialized with {@link #init(ConfigHolder)}
     * @return a collection of all {@link ConfigHolder}s initialized with {@link #init(ConfigHolder)}
     */
    @NotNull @UnmodifiableView
    Collection<ConfigHolder<? extends Config>> getConfigHolders();
}
