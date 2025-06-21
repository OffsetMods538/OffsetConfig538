package top.offsetmonkey538.offsetconfig538.api.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Holds a {@link Config} and associated info
 *
 * @param <T> your {@link Config} class
 */
public final class ConfigHolder<T extends Config> {
    /**
     * A {@link Supplier<T>} used to create new instances of your {@link Config} containing the default values. Usually the constructor.
     */
    public final @NotNull Supplier<T> defaultConstructor;
    /**
     * An {@link ErrorHandler} used for logging errors during handling of the config.
     */
    public final @NotNull ErrorHandler errorHandler;
    /**
     * The {@link Class<T>} of the {@link Config}.
     * <br>
     * Automatically gotten using {@link #config}.{@link Object#getClass() getClass()}
     */
    public final @NotNull Class<T> configClass;
    /**
     * The currently held config.
     * <p>
     *     This may be changed at any time for whatever reason.
     *     <br>
     *     Please use {@link #get()} for getting the config and do not keep a reference to it.
     * </p>
     */
    private @NotNull T config;

    /**
     * Initializes a new config holder. Sets {@link #config} from the provided constructor.
     *
     * @param defaultConstructor supplier used to create new instances of your {@link Config}. For example {@code MyConfig::new}
     * @param errorHandler the {@link ErrorHandler} to use. For example {@code LOGGER::error} or {@link ErrorHandler#SYSTEM_ERR}
     */
    public ConfigHolder(@NotNull final Supplier<T> defaultConstructor, @NotNull final ErrorHandler errorHandler) {
        this.defaultConstructor = defaultConstructor;
        this.errorHandler = errorHandler;

        this.config = defaultConstructor.get();
        //noinspection unchecked: This should always cast correctly, right???
        this.configClass = (Class<T>) config.getClass();
    }

    /**
     * Returns the config currently held by this holder.
     * <p>
     *     As the held config may be replaced at any time, please:
     *     <br>
     *     <strong>DO NOT keep an instance of the config returned here! Only store the holder itself and always call this method!</strong>
     * </p>
     *
     * @return The config currently held by this holder.
     */
    @Contract(pure = true)
    public @NotNull T get() {
        return config;
    }

    /**
     * Returns the id of the held config.
     * <br>
     * Always equal to the config {@link T#getFilePath() file path}.
     *
     * @return the id of the held config.
     */
    public @NotNull String getId() {
        return config.getFilePath().toString();
    }

    /**
     * Sets the held config to a new value.
     * <p>
     *     When the new value is {@code null}, the held config will be reset using the {@link #defaultConstructor}.
     * </p>
     *
     * @param newConfig the new config to hold. If null, the {@link #defaultConstructor} will be used to create a default one.
     */
    public void set(@Nullable T newConfig) {
        if (newConfig == null) config = defaultConstructor.get();
        else config = newConfig;
    }

    /**
     * Just returns the id.
     *
     * @return the id of the held config, same as {@link #getId()}
     */
    @Override
    public String toString() {
        return getId();
    }
}
