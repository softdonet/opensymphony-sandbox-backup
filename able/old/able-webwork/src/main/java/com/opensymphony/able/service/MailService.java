package com.opensymphony.able.service;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public interface MailService {
    void send(String to, String subject, String body) throws MailException;
}
