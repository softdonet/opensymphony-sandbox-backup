package com.opensymphony.able.service;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class ServiceException extends Exception {
    public ServiceException(String string) {
        super(string);
    }

    public ServiceException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
