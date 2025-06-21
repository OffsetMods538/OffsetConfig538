package top.offsetmonkey538.offsetconfig538.api.config;

import blue.endless.jankson.Jankson;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.file.Path;

/**
 * The class to implement in your config
 */
public interface Config {

    /**
     * Configures the provided {@link Jankson.Builder}.
     * <br>
     * Override to add custom serializers and stuff
     * <p>
     *     The {@link top.offsetmonkey538.offsetconfig538.api.event.OffsetConfig538Events#JANKSON_CONFIGURATION_EVENT JANKSON_CONFIGURATION_EVENT} will be invoked before this method.
     * </p>
     *
     * @param builder the builder to configure.
     */
    @Contract
    default void configureJankson(final @NotNull Jankson.Builder builder) {
        // no-op
    }

    /**
     * Provides an array of {@link Datafixer}s for updating this config.
     * <p>
     *     1st entry (index 0) is upgrading from 0 to 1
     *     <br>
     *     2nd entry (index 1) is upgrading from 1 to 2
     *     <br>
     *     3rd entry (index 2) is upgrading from 2 to 3
     *     <br>
     *     ...
     * </p>
     *
     * @return an array of {@link Datafixer}s for updating this config.
     * @see #getConfigVersion()
     * @see Datafixer
     */
    @Contract(pure = true)
    default @NotNull Datafixer[] getDatafixers() {
        return new Datafixer[] {};
    }

    /**
     * Provides the current config version.
     * <br>
     * Used for datafixing
     *
     * @return the current config version.
     * @see #getDatafixers()
     * @see Datafixer
     */
    default @Range(from = 0, to = Integer.MAX_VALUE) int getConfigVersion() {
        return 0;
    }

    /**
     * Provides the {@link Path} to the config file.
     * <br>
     * Should also include extension.
     *
     * @return the {@link Path} to the config file.
     */
    @NotNull Path getFilePath();
}
