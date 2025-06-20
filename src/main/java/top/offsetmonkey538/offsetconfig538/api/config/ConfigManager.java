package top.offsetmonkey538.offsetconfig538.api.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import top.offsetmonkey538.offsetconfig538.impl.config.ConfigManagerImpl;

import java.util.Collection;

public interface ConfigManager {
    ConfigManager INSTANCE = new ConfigManagerImpl();

    <T extends Config> @NotNull ConfigHolder<T> init(@NotNull ConfigHolder<T> configHolder);

    @Contract // pure=false is default
    <T extends Config> void load(@NotNull ConfigHolder<T> configHolder);

    @Contract // pure=false is default
    <T extends Config> void save(@NotNull ConfigHolder<T> configHolder);

    @Contract(pure = true)
    @Nullable ConfigHolder<? extends Config> get(@NotNull String id);

    @Contract(pure = true)
    <T extends Config> @Nullable ConfigHolder<T> get(@NotNull String id, @NotNull Class<T> configClass);

    @NotNull @UnmodifiableView
    Collection<ConfigHolder<? extends Config>> getConfigHolders();

    // todo: <T extends Config> void applyDatafixers(@NotNull T fallback, @NotNull ErrorHandler errorHandler);
}
