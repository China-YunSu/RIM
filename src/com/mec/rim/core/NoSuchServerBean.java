package com.mec.rim.core;

public class NoSuchServerBean extends Exception{
    public NoSuchServerBean() {
        super();
    }

    public NoSuchServerBean(String message) {
        super(message);
    }

    public NoSuchServerBean(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchServerBean(Throwable cause) {
        super(cause);
    }

    protected NoSuchServerBean(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
