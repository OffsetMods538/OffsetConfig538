package top.offsetmonkey538.offsetconfig538.impl.config;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.offsetmonkey538.offsetconfig538.api.config.Config;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetconfig538.api.config.ErrorHandler;

import java.util.function.Supplier;

/**
 * Implementation of {@link ConfigHolder}
 *
 * @param <T> your {@link Config} class
 */
@ApiStatus.Internal
public final class ConfigHolderImpl<T extends Config> implements ConfigHolder<T> {
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
    public ConfigHolderImpl(@NotNull final Supplier<T> defaultConstructor, @NotNull final ErrorHandler errorHandler) {
        this.defaultConstructor = defaultConstructor;
        this.errorHandler = errorHandler;

        this.config = defaultConstructor.get();
        //noinspection unchecked: This should always cast correctly, right???
        this.configClass = (Class<T>) config.getClass();
    }

    @Contract(pure = true)
    public @NotNull T get() {
        return config;
    }

    public @NotNull String getId() {
        return config.getFilePath().toString();
    }

    public void set(@Nullable T newConfig) {
        if (newConfig == null) config = defaultConstructor.get();
        else config = newConfig;
    }
}
