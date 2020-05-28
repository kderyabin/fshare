package com.kderyabin.util;

public class Notification {
    /**
     * Identifier for an error notification requiring user action.
     * The error message is a resource property key.
     */
    public final static String INFO_DISMISS = "INFO_DISMISS";
    /**
     * Identifier for a simple error notification.
     * The error message is a resource property key.
     */
    public final static String INFO = "INFO";
    /**
     * Identifier for an error notification requiring user action.
     * The difference with INFO_DISMISS is that any error message can be passed with this notification.
     */
    public final static String INFO_RAW_DISMISS = "INFO_RAW_DISMISS";
}
