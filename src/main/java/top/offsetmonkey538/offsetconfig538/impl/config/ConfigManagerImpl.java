package top.offsetmonkey538.offsetconfig538.impl.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import top.offsetmonkey538.offsetconfig538.api.config.Config;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class ConfigManagerImpl implements ConfigManager {
    private static final String VERSION_KEY = "!!!version";

    private static final Map<String, ConfigHolder<?>> CONFIG_HOLDERS = new HashMap<>();

    @Override
    public @NotNull <T extends Config> ConfigHolder<T> init(@NotNull ConfigHolder<T> configHolder) {
        CONFIG_HOLDERS.put(configHolder.getId(), configHolder);

        if (Files.exists(configHolder.get().getFilePath())) load(configHolder);

        save(configHolder);
        return configHolder;
    }

    @Override
    public <T extends Config> void load(@NotNull ConfigHolder<T> configHolder) {
        if (!CONFIG_HOLDERS.containsKey(configHolder.getId()) || !CONFIG_HOLDERS.containsValue(configHolder)) {
            configHolder.errorHandler.log("Trying to load uninitialized config with id '%s'!", configHolder.getId());
            return;
        }

        final Jankson jankson = configHolder.get().configureJankson(Jankson.builder()).build(); // TODO: global jankson builder configuration event thingy
        final File configFile = configHolder.get().getFilePath().toFile();

        // Load it from disk
        final JsonObject json;
        try {
            json = jankson.load(configFile);
        } catch (IOException e) {
            configHolder.errorHandler.log("Config file '%s' could not be read!", e, configHolder);
            return;
        } catch (SyntaxError e) {
            configHolder.errorHandler.log("Config file '%s' is formatted incorrectly!", e, configHolder);
            return;
        }

        // TODO: Run datafixers if needed
        //  applyDatafixers(...);

        configHolder.set(jankson.fromJson(json, configHolder.configClass));
    }

    @Override
    public <T extends Config> void save(@NotNull ConfigHolder<T> configHolder) {
        if (!CONFIG_HOLDERS.containsKey(configHolder.getId()) || !CONFIG_HOLDERS.containsValue(configHolder)) {
            configHolder.errorHandler.log("Trying to save uninitialized config with id '%s'!", configHolder.getId());
            return;
        }

        final Jankson jankson = configHolder.get().configureJankson(Jankson.builder()).build(); // TODO: global jankson builder configuration event thingy

        // Convert to json
        final JsonElement jsonAsElement = jankson.toJson(configHolder.get());
        if (!(jsonAsElement instanceof final JsonObject json)) {
            configHolder.errorHandler.log("Config '%s' could not be serialized to a 'JsonObject', got '%s' instead! Config will not be saved.", configHolder, jsonAsElement.getClass().getName());
            return;
        }

        // TODO: Add config version (datafixer stuff)
        //  json.put(VERSION_KEY, ...);

        // Convert to string
        final String result = json.toJson(true, true);

        // Save
        try {
            Files.createDirectories(configHolder.get().getFilePath().getParent());
            Files.writeString(configHolder.get().getFilePath(), result);
        } catch (IOException e) {
            configHolder.errorHandler.log("Config file '%s' could not be saved!", e, configHolder);
        }
    }

    @Override
    public @Nullable ConfigHolder<? extends Config> get(@NotNull String id) {
        return CONFIG_HOLDERS.get(id);
    }

    @Override
    public @Nullable <T extends Config> ConfigHolder<T> get(@NotNull String id, @NotNull Class<T> configClass) {
        final ConfigHolder<? extends Config> configHolder = CONFIG_HOLDERS.get(id);
        if (configHolder == null) return null;

        // todo: does this equal thing right here work?
        if (configHolder.configClass == configClass) {
            //noinspection unchecked
            return (ConfigHolder<T>) configHolder;
        }

        configHolder.errorHandler.log("Config with id '%s' is supposed to be of class '%s', but was '%s' instead!", id, configClass, configHolder.configClass);
        return null;
    }

    @Override
    public @NotNull @UnmodifiableView Collection<ConfigHolder<? extends Config>> getConfigHolders() {
        return Collections.unmodifiableCollection(CONFIG_HOLDERS.values());
    }
}
