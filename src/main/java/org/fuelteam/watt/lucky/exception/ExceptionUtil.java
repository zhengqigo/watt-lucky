package org.fuelteam.watt.lucky.exception;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fuelteam.watt.lucky.annotation.NotNull;
import org.fuelteam.watt.lucky.annotation.Nullable;
import org.fuelteam.watt.lucky.io.type.StringBuilderWriter;

import com.google.common.base.Throwables;

public class ExceptionUtil {

    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];

    /**
     * 转换为RuntimeException重新抛出，RuntimeException和Error不会被转变。举例:
     * <pre>
     * try{ ... }catch(Exception throwable){ throw unchecked(throwable); }
     * </pre>
     * 
     * @see ExceptionUtils#wrapAndThrow(Throwable)
     */
    public static RuntimeException unchecked(@Nullable Throwable throwable) {
        if (throwable instanceof RuntimeException) throw (RuntimeException) throwable;
        if (throwable instanceof Error) throw (Error) throwable;
        throw new UncheckedException(throwable);
    }

    public static Throwable unwrap(@Nullable Throwable throwable) {
        if (throwable instanceof UncheckedException || throwable instanceof ExecutionException
                || throwable instanceof InvocationTargetException || throwable instanceof UndeclaredThrowableException) {
            return throwable.getCause();
        }
        return throwable;
    }

    public static RuntimeException unwrapAndUnchecked(@Nullable Throwable throwable) {
        throw unchecked(unwrap(throwable));
    }

    /**
     * 将StackTrace[]转换为String，供logger或printStackTrace()外的其它用途
     */
    public static String stackTraceText(@NotNull Throwable throwable) {
        StringBuilderWriter stringWriter = new StringBuilderWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter)); // NOSONAR
        return stringWriter.toString();
    }

    /**
     * @see ExceptionUtils#getMessage(Throwable)
     */
    public static String toStringWithShortName(@Nullable Throwable throwable) {
        return ExceptionUtils.getMessage(throwable);
    }

    public static String toStringWithRootCause(@Nullable Throwable throwable) {
        if (throwable == null) return StringUtils.EMPTY;

        final String className = ClassUtils.getShortClassName(throwable, null);
        final String message = StringUtils.defaultString(throwable.getMessage());
        Throwable cause = getRootCause(throwable);

        StringBuilder sb = new StringBuilder(128).append(className).append(": ").append(message);
        if (cause != throwable) sb.append("; ").append(toStringWithShortName(cause));
        return sb.toString();
    }

    public static Throwable getRootCause(@NotNull Throwable throwable) {
        return Throwables.getRootCause(throwable);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T findCause(@NotNull Throwable throwable, Class<T> cause) {
        while (throwable != null) {
            if (throwable.getClass().equals(cause)) return (T) throwable;
            throwable = throwable.getCause();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static boolean isCausedBy(@Nullable Throwable throwable, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = throwable;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 对已知且常抛出的异常，不需要每次创建异常类并很消耗性能的生成完整的StackTrace，此时可使用静态声明的异常
     * <pre>
     * private static RuntimeException TIMEOUT_EXCEPTION = ExceptionUtil.setStackTrace(new RuntimeException("Timeout"), Service.class, "method");
     * </pre>
     */
    public static <T extends Throwable> T setStackTrace(@NotNull T throwable, Class<?> throwClass, String throwClazz) {
        throwable.setStackTrace(new StackTraceElement[] { new StackTraceElement(throwClass.getName(), throwClazz, null, -1) });
        return throwable;
    }

    public static <T extends Throwable> T clearStackTrace(@NotNull T throwable) {
        Throwable cause = throwable;
        while (cause != null) {
            cause.setStackTrace(EMPTY_STACK_TRACE);
            cause = cause.getCause();
        }
        return throwable;
    }
}