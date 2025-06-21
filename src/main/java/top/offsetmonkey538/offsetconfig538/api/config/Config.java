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

    @NotNull Path getFilePath();
}
