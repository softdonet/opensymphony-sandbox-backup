package com.opensymphony.able.service;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class MailException extends ServiceException {
    public MailException(String string) {
        super(string);
    }

    public MailException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
