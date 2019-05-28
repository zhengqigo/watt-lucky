package org.fuelteam.watt.lucky.exception;

public class CloneableRuntimeException extends RuntimeException implements Cloneable {

    private static final long serialVersionUID = 3984796576627959400L;

    protected String message; // NOSONAR

    public CloneableRuntimeException() {
        super((Throwable) null);
    }

    public CloneableRuntimeException(String message) {
        super((Throwable) null);
        this.message = message;
    }

    public CloneableRuntimeException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public CloneableRuntimeException clone() { // NOSONAR
        try {
            return (CloneableRuntimeException) super.clone();
        } catch (CloneNotSupportedException e) { // NOSONAR
            return null;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    public CloneableRuntimeException setStackTrace(Class<?> throwClazz, String throwMethod) {
        ExceptionUtil.setStackTrace(this, throwClazz, throwMethod);
        return this;
    }

    public CloneableRuntimeException clone(String message) {
        CloneableRuntimeException newException = this.clone();
        newException.setMessage(message);
        return newException;
    }

    public CloneableRuntimeException setMessage(String message) {
        this.message = message;
        return this;
    }
}