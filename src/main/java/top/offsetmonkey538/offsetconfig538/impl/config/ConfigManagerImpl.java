package top.offsetmonkey538.offsetconfig538.impl.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.DeserializationException;
import blue.endless.jankson.api.SyntaxError;
import org.jetbrains.annotations.*;
import top.offsetmonkey538.offsetconfig538.api.config.*;
import top.offsetmonkey538.offsetconfig538.api.event.OffsetConfig538Events;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Implementation of the {@link ConfigManager}
 */
@ApiStatus.Internal
public final class ConfigManagerImpl implements ConfigManager {
    private static final String VERSION_KEY = "!!!version";
    private static final String VERSION_COMMENT = "!!!!! DO NOT MODIFY THIS VALUE !!!!";

    /**
     * Constructs the {@link ConfigManager} implementation.
     * <p>
     * Should only be called when initializing {@link ConfigManager#INSTANCE}
     */
    @ApiStatus.Internal
    public ConfigManagerImpl() {

    }

    @Override
    public @NotNull <T extends Config> ConfigHolder<T> init(@NotNull ConfigHolder<T> configHolder, @NotNull ErrorHandler errorHandler) {
        if (Files.exists(configHolder.get().getFilePath())) load(configHolder, errorHandler);

        save(configHolder, errorHandler);
        return configHolder;
    }

    @Override
    public <T extends Config> void load(@NotNull ConfigHolder<T> configHolder, @NotNull ErrorHandler errorHandler) {
        final ConfigHolderImpl<T> configHolderImpl = (ConfigHolderImpl<T>) configHolder;
        final Jankson jankson = configureJankson(configHolderImpl);
        final File configFile = configHolderImpl.get().getFilePath().toFile();

        // Load it from disk
        final JsonObject json;
        try {
            json = jankson.load(configFile);
        } catch (IOException e) {
            errorHandler.log("Config file '%s' could not be read!", e, configHolderImpl.getId());
            return;
        } catch (SyntaxError e) {
            errorHandler.log("Config file '%s' is formatted incorrectly!", e, configHolderImpl.getId());
            return;
        }

        final boolean modified = applyDatafixers(configHolderImpl, json, jankson, errorHandler);

        try {
            // Remove version first, otherwise 'fromJsonCarefully' will throw because it's not in the config object
            json.remove(VERSION_KEY);

            configHolderImpl.set(jankson.fromJsonCarefully(json, configHolderImpl.configClass));
        } catch (DeserializationException e) {
            errorHandler.log("Failed to create config class '%s' from json!", e, configHolderImpl.configClass.getName());
            configHolderImpl.set(jankson.fromJson(json, configHolderImpl.configClass));
        }

        if (modified) save(configHolderImpl);
    }

    @Override
    public <T extends Config> void save(@NotNull ConfigHolder<T> configHolder, @NotNull ErrorHandler errorHandler) {
        final ConfigHolderImpl<T> configHolderImpl = (ConfigHolderImpl<T>) configHolder;
        final Jankson jankson = configureJankson(configHolderImpl);

        // Convert to json
        final JsonElement jsonAsElement = jankson.toJson(configHolderImpl.get());
        if (!(jsonAsElement instanceof final JsonObject json)) {
            errorHandler.log("Config '%s' could not be serialized to a 'JsonObject', got '%s' instead! Config will not be saved.", configHolderImpl.getId(), jsonAsElement.getClass().getName());
            return;
        }

        // Write config version
        json.put(VERSION_KEY, new JsonPrimitive(configHolderImpl.get().getConfigVersion()), VERSION_COMMENT);

        // Convert to string
        final String result = json.toJson(true, true);

        // Save
        try {
            Files.createDirectories(configHolderImpl.get().getFilePath().getParent());
            Files.writeString(configHolderImpl.get().getFilePath(), result);
        } catch (IOException e) {
            errorHandler.log("Config file '%s' could not be saved!", e, configHolderImpl.getId());
        }
    }

    @Contract
    private <T extends Config> boolean applyDatafixers(final @NotNull ConfigHolderImpl<T> configHolder, final @NotNull JsonObject json, final @NotNull Jankson jankson, @NotNull ErrorHandler errorHandler) {
        final int loadedVersion = json.getInt(VERSION_KEY, 0);
        final int currentVersion = configHolder.get().getConfigVersion();

        if (loadedVersion == currentVersion) return false;
        if (loadedVersion > currentVersion) errorHandler.log("Config file '%s' is for a newer version! Expected config version to be '%s', got '%s'! (Did the mod get downgraded or are you just messing with the value that literally says to not modify it?)", configHolder, currentVersion, loadedVersion);

        final Path backupPath = configHolder.get().getFilePath().resolveSibling("backup-%s-%s".formatted(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss")), configHolder.get().getFilePath().getFileName()));
        try {
            Files.copy(configHolder.get().getFilePath(), backupPath);
        } catch (IOException e) {
            errorHandler.log("Unable to create backup of config file '%s'! Continuing anyway cause I don't care 'bout your config.", e, configHolder);
        }

        for (Datafixer datafixer : Arrays.copyOfRange(configHolder.get().getDatafixers(), loadedVersion, currentVersion)) {
            datafixer.apply(json, jankson);
        }
        return true;
    }

    private @NotNull Jankson configureJankson(final @NotNull ConfigHolder<?> configHolder) {
        final Jankson.Builder builder = Jankson.builder();

        OffsetConfig538Events.JANKSON_CONFIGURATION_EVENT.getInvoker().configureBuilder(builder);
        configHolder.get().configureJankson(builder);

        return builder.build();
    }
}
