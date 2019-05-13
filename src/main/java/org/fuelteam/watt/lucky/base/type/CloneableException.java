package org.fuelteam.watt.lucky.base.type;

import org.fuelteam.watt.lucky.base.ExceptionUtil;

/**
 * 避免获得StackTrace的性能损耗：定义静态异常->克隆并设定新的异常信息。举例：
 * <pre>
 * // 定义静态异常TIMEOUT_EXCEPTION RootCause为Timeout My.class类hello方法
 * private static CloneableException TIMEOUT_EXCEPTION = new CloneableException("Timeout").setStackTrace(My.class, "hello");
 * 
 * // 抛出异常并设定异常信息为Timeout for 40ms
 * throw TIMEOUT_EXCEPTION.clone("Timeout for 40ms");
 */
public class CloneableException extends Exception implements Cloneable {

    private static final long serialVersionUID = -6270471689928560417L;

    protected String message; // NOSONAR

    public CloneableException() {
        super((Throwable) null);
    }

    public CloneableException(String message) {
        super((Throwable) null);
        this.message = message;
    }

    public CloneableException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public CloneableException clone() { // NOSONAR
        try {
            return (CloneableException) super.clone();
        } catch (CloneNotSupportedException e) { // NOSONAR
            return null;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    public CloneableException setStackTrace(Class<?> throwClazz, String throwMethod) {
        ExceptionUtil.setStackTrace(this, throwClazz, throwMethod);
        return this;
    }

    public CloneableException clone(String message) {
        CloneableException newException = this.clone();
        newException.setMessage(message);
        return newException;
    }

    public CloneableException setMessage(String message) {
        this.message = message;
        return this;
    }
}