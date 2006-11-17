package com.opensymphony.able.upgrade;

public class UpgradeException extends Exception {
    public UpgradeException(String message) {
        super(message);
    }

    public UpgradeException(String message, Throwable cause) {
        super(message, cause);
    }
}
