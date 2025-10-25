package top.offsetmonkey538.offsetconfig538.api.config;

import blue.endless.jankson.Jankson;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.file.Path;

/**
 * The class to implement in your config
 * <p>
 *     All config fields must be non-final!
 * </p>
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
     * This method will be called before the loading of this config starts.
     * <br />
     * This may be used to for example migrate configs from previous locations.
     */
    default void beforeLoadStart() {

    }

    /**
     * Provides the {@link Path} to the config file.
     *
     * @return the {@link Path} to the config file.
     */
    default @NotNull Path getFilePath() {
        return getConfigDirPath().resolve(getId() + getFileExtension());
    }

    /**
     * Provides the {@link Path} to the config directory.
     *
     * @return the {@link Path} to the config directory.
     */
    @NotNull Path getConfigDirPath();


    /**
     * Provides an identifier for this config file.
     * <br>
     * Example: {@code github-resourcepack-manager/main}
     *
     * @return an identifier for this config file.
     */
    @NotNull String getId();

    /**
     * Provides the extension for this config file.
     * <br>
     * MUST start with a dot ({@code .})
     */
    default @NotNull String getFileExtension() {
        return ".json";
    }
}
