package top.offsetmonkey538.offsetconfig538.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * A functional interface wrapping some logging method.
 * <p>
 * Used for logging errors arising during config management.
 */
@FunctionalInterface
public interface ErrorHandler extends BiConsumer<String, Throwable> {
    /**
     * {@link ErrorHandler} wrapping the {@link System#out} stream.
     * @see #fromPrintStream(PrintStream)
     * @see #SYSTEM_ERR
     */
    ErrorHandler SYSTEM_OUT = fromPrintStream(System.out);
    /**
     * {@link ErrorHandler} wrapping the {@link System#err} stream.
     * @see #fromPrintStream(PrintStream)
     * @see #SYSTEM_OUT
     */
    ErrorHandler SYSTEM_ERR = fromPrintStream(System.err);

    /**
     * The functional method.
     * <p>
     *     Logs the provided error.
     * </p>
     *
     * @param error the error to log
     * @param throwable the throwable to log, may be null
     */
    @Override
    void accept(@NotNull String error, @Nullable Throwable throwable);

    @Override
    @NotNull
    default ErrorHandler andThen(@NotNull BiConsumer<? super String, ? super Throwable> after) {
        Objects.requireNonNull(after);

        return (error, throwable) -> {
            accept(error, throwable);
            after.accept(error, throwable);
        };
    }

    /**
     * Logs the provided error.
     * <p>
     *     Has a throwable and may include args to format the error with.
     *     <br>
     *     Error is formatted using {@link String#formatted(Object...)}
     * </p>
     *
     * @param error the error to log
     * @param throwable the throwable to log, may not be null
     * @param args args to format the string with, may be null
     * @see #log(String, Object...)
     */
    default void log(@NotNull String error, @NotNull Throwable throwable, @Nullable Object... args) {
        accept(error.formatted(args), throwable);
    }

    /**
     * Logs the provided error.
     * <p>
     *     May include args to format the error with.
     *     <br>
     *     Error is formatted using {@link String#formatted(Object...)}
     * </p>
     *
     * @param error the error to log
     * @param args args to format the string with, may be null
     */
    default void log(@NotNull String error, @Nullable  Object... args) {
        accept(error.formatted(args), null);
    }

    /**
     * Wraps a {@link PrintStream} in an {@link ErrorHandler}.
     *
     * @param stream the stream to wrap
     * @return an {@link ErrorHandler} wrapping the provided {@link PrintStream}
     * @see #SYSTEM_OUT
     * @see #SYSTEM_ERR
     */
    static @NotNull ErrorHandler fromPrintStream(final @NotNull PrintStream stream) {
        return (error, throwable) -> {
            stream.println(error);
            if (throwable != null) throwable.printStackTrace(stream);
        };
    }
}
