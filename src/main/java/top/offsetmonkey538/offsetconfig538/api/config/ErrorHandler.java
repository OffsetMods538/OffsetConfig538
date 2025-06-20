package top.offsetmonkey538.offsetconfig538.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ErrorHandler extends BiConsumer<String, Throwable> {
    ErrorHandler SYSTEM_OUT = fromPrintStream(System.out);
    ErrorHandler SYSTEM_ERR = fromPrintStream(System.err);

    @Override
    void accept(@NotNull String error, @Nullable Throwable throwable);

    default void log(@NotNull String error, @NotNull Throwable throwable, @Nullable Object... args) {
        accept(error.formatted(args), throwable);
    }

    default void log(@NotNull String error, @Nullable  Object... args) {
        accept(error.formatted(), null);
    }

    static @NotNull ErrorHandler fromPrintStream(final @NotNull PrintStream stream) {
        return (error, throwable) -> {
            stream.println(error);
            if (throwable != null) throwable.printStackTrace(stream);
        };
    }
}
