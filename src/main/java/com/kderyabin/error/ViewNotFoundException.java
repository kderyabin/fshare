package com.kderyabin.error;

public class ViewNotFoundException extends Exception{
    public ViewNotFoundException() {
        super();
    }

    public ViewNotFoundException(String message) {
        super(message);
    }

    public ViewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ViewNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
