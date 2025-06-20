package top.offsetmonkey538.offsetconfig538.api.config;

import blue.endless.jankson.Jankson;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.file.Path;

public interface Config {


    @Contract
    default void configureJankson(final @NotNull Jankson.Builder builder) {
        // no-op
    }

    @Contract(pure = true)
    default @NotNull Datafixer[] getDatafixers() {
        return new Datafixer[] {};
    }

    default @Range(from = 0, to = Integer.MAX_VALUE) int getConfigVersion() {
        return 0;
    }

    default @NotNull Path getFilePath() {
        return getConfigDir().resolve(getName() + ".json");
    }


    /**
     * Used for building the config file path.
     * <p>
     *     <strong>Deprecated because this method should not be called! Please call {@link Path#getFileName()} on the {@link Path} returned from {@link #getFilePath()} instead!</strong>
     * </p>
     * <p>
     *     <strong>It is fine (recommended even) to implement this method! Only deprecated because it's fine to just override {@link #getFilePath()} and not use this method.</strong>
     * </p>
     *
     * @deprecated because this shouldn't be called. It is fine to implement.
     * @return The name of the config file.
     * @see #getFilePath()
     * @see #getConfigDir()
     */
    @Deprecated
    default @NotNull String getName() {
        throw new UnsupportedOperationException("The 'getName' method of config not implemented! Either override 'getFilePath' and use a custom Path or implement both 'getName' and 'getConfigDir'");
    }

    /**
     * Used for building the config file path.
     * <p>
     *     <strong>Deprecated because this method should not be called! Please call {@link Path#getParent()} on the {@link Path} returned from {@link #getFilePath()} instead!</strong>
     * </p>
     * <p>
     *     <strong>It is fine (recommended even) to implement this method! Only deprecated because it's fine to just override {@link #getFilePath()} and not use this method.</strong>
     * </p>
     *
     * @deprecated because this shouldn't be called. It is fine to implement.
     * @return The directory the config will be in.
     * @see #getFilePath()
     * @see #getName()
     */
    @Deprecated
    default @NotNull Path getConfigDir() {
        throw new UnsupportedOperationException("The 'getConfigDir' method of config not implemented! Either override 'getFilePath' and use a custom Path or implement both 'getName' and 'getConfigDir'");
    }
}
