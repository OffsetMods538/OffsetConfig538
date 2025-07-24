package top.offsetmonkey538.offsetconfig538.api.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.offsetconfig538.impl.config.ConfigHolderImpl;

import java.util.function.Supplier;

/**
 * Holds a {@link Config} and associated info
 *
 * @param <T> your {@link Config} class
 */
@ApiStatus.NonExtendable
public interface ConfigHolder<T extends Config> {

    /**
     * Initializes a new config holder. Sets held config from the provided constructor.
     *
     * @param defaultConstructor supplier used to create new instances of your {@link Config}. For example {@code MyConfig::new}
     * @param errorHandler the {@link ErrorHandler} to use. For example {@code LOGGER::error} or {@link ErrorHandler#SYSTEM_ERR}
     * @return a new config holder
     * @param <T> your {@link Config} class
     */
    static <T extends Config> ConfigHolder<T> create(@NotNull final Supplier<T> defaultConstructor, @NotNull final ErrorHandler errorHandler) {
        return new ConfigHolderImpl<>(defaultConstructor, errorHandler);
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
    @NotNull T get();

    /**
     * Returns the class of the held config.
     *
     * @return the class of the held config.
     */
    @NotNull Class<T> getConfigClass();

    /**
     * The {@link ErrorHandler} used for logging errors during handling of the config.
     *
     * @return the {@link ErrorHandler} used for logging errors during handling of the config.
     */
    @NotNull ErrorHandler getErrorHandler();

    /**
     * Returns the path to the config file.
     * <br>
     * Always equal to {@link T#getFilePath()}.
     *
     * @return the path to the held config file.
     */
    @NotNull String toString();

    /**
     * Sets the held config to a new value.
     * <p>
     *     When the new value is {@code null}, the held config will be reset to default values.
     * </p>
     *
     * @param newConfig the new config to hold. If null, the config will be reset to default values.
     */
    void set(@Nullable T newConfig);
}
