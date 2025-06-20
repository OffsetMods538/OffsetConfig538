package top.offsetmonkey538.offsetconfig538;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import top.offsetmonkey538.offsetconfig538.api.config.Config;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigHolder;
import top.offsetmonkey538.offsetconfig538.api.config.ConfigManager;
import top.offsetmonkey538.offsetconfig538.api.config.ErrorHandler;

import java.nio.file.Path;

public class MainTest {

    @Test
    public void testMain() {
        final ConfigHolder<TestConfig> holder = ConfigManager.INSTANCE.init(new ConfigHolder<>(TestConfig::new, ErrorHandler.SYSTEM_ERR));

        System.out.println(holder.get().hi);
        System.out.println(holder.get().nice);
    }

    private static class TestConfig implements Config {
        String hi = "Hello, World!";
        int nice = 69;

        @Override
        public @NotNull String getName() {
            return "test";
        }

        @Override
        public @NotNull Path getConfigDir() {
            return Path.of(".", "test");
        }
    }

}
