package top.offsetmonkey538.offsetconfig538.api.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A functional interface used to update a config file to newer formats
 */
@FunctionalInterface
public interface Datafixer {

    /**
     * Modify the provided {@link JsonObject} to comply with the correct version of your config
     *
     * @param json The json data in the format of the last config version.
     * @param jankson Used for (de)serializing json data.
     */
    @Contract()
    void apply(final @NotNull JsonObject json, final @NotNull Jankson jankson);
}
