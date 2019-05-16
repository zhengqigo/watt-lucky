package org.fuelteam.watt.lucky.base;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fuelteam.watt.lucky.base.annotation.NotNull;
import org.fuelteam.watt.lucky.base.annotation.Nullable;
import org.fuelteam.watt.lucky.base.type.CloneableException;
import org.fuelteam.watt.lucky.base.type.UncheckedException;
import org.fuelteam.watt.lucky.io.type.StringBuilderWriter;

import com.google.common.base.Throwables;

/**
 * 异常工具类
 * @see CloneableException
 * @see ExceptionUtils
 */
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
     * 与Throwable.toString()相比使用了短类名
     * 
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
        if (cause != throwable) {
            sb.append("; ").append(toStringWithShortName(cause));
        }
        return sb.toString();
    }

    /**
     * 获取异常的RootCause，没有则返回自身
     * 
     * @see Throwables#getRootCause(Throwable)
     */
    public static Throwable getRootCause(@NotNull Throwable throwable) {
        return Throwables.getRootCause(throwable);
    }

    /**
     * 获取某种类型的cause，没有则返回空
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T findCause(@NotNull Throwable throwable, Class<T> cause) {
        while (throwable != null) {
            if (throwable.getClass().equals(cause)) return (T) throwable;
            throwable = throwable.getCause();
        }
        return null;
    }

    /**
     * 判断异常是否由某些底层的异常引起
     */
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
     * 对某些已知且经常抛出的异常，不需要每次创建异常类并很消耗性能的并生成完整的StackTrace，此时可使用静态声明的异常。
     * 如果异常可能在多个地方抛出，使用本函数设置抛出的类名和方法名。举例：
     * <pre>
     * private static RuntimeException TIMEOUT_EXCEPTION = ExceptionUtil.setStackTrace(new RuntimeException("Timeout"), My.class, "method");
     * </pre>
     */
    public static <T extends Throwable> T setStackTrace(@NotNull T throwable, Class<?> throwClass, String throwClazz) {
        throwable.setStackTrace(new StackTraceElement[] { new StackTraceElement(throwClass.getName(), throwClazz, null, -1) });
        return throwable;
    }

    /**
     * 暴力清除Cause的StackTrace
     */
    public static <T extends Throwable> T clearStackTrace(@NotNull T throwable) {
        Throwable cause = throwable;
        while (cause != null) {
            cause.setStackTrace(EMPTY_STACK_TRACE);
            cause = cause.getCause();
        }
        return throwable;
    }
}