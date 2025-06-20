package top.offsetmonkey538.offsetconfig538.api.config;

import blue.endless.jankson.Jankson;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface Config {


    @Contract(value = "_ -> param1", pure = false)
    default void configureJankson(final @NotNull Jankson.Builder builder) {
        // no-op
    }

    default @NotNull Datafixer[] getDatafixers() {
        return new Datafixer[] {};
    }

    default int getConfigVersion() {
        return 0;
    }

    default @NotNull Path getFilePath() {
        return getConfigDir().resolve(getName() + ".json");
    }

    @NotNull String getName();
    @NotNull Path getConfigDir();
}
