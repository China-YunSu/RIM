package com.mec.rim.core;

public class MethodInvokException extends RuntimeException{
    public MethodInvokException() {
        super();
    }

    public MethodInvokException(String message) {
        super(message);
    }

    public MethodInvokException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodInvokException(Throwable cause) {
        super(cause);
    }

    protected MethodInvokException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private static final long serialVersionUID = -8319409609764640463L;
}
