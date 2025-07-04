package top.offsetmonkey538.offsetconfig538;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import top.offsetmonkey538.offsetconfig538.api.config.*;
import top.offsetmonkey538.offsetconfig538.api.event.OffsetConfig538Events;

import java.nio.file.Path;

public class MainTest {

    @Test
    public void datafixingTest() {

    }



    @Test
    public void testMain() {
        OffsetConfig538Events.JANKSON_CONFIGURATION_EVENT.listen(System.out::println);

        final ConfigHolder<TestConfig> holder = ConfigManager.INSTANCE.init(ConfigHolder.create(TestConfig::new, ErrorHandler.SYSTEM_ERR));

        System.out.println(holder.get().goodbye);
        System.out.println(holder.get().veryNice);
    }

    private static class TestConfig implements Config {
        String goodbye = "Goodbye, Cruel World!";
        float veryNice = 1233;

        @Override
        public @NotNull Path getFilePath() {
            return Path.of(".", "test", "test.json");
        }

        @Override
        public @NotNull Datafixer[] getDatafixers() {
            return new Datafixer[] {
                    (json, jankson) -> {
                        // From version 0 to 1
                        json.put("goodbye", json.get("hi"));
                        json.put("veryNice", jankson.toJson((float) jankson.getMarshaller().marshall(int.class, json.get("nice"))));
                    }
            };
        }

        @Override
        public int getConfigVersion() {
            return 1;
        }
    }

}
