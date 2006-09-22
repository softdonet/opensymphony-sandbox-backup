package com.opensymphony.able.mail;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class MockMailService implements MailService {
    public void send(String to, String subject, String body) throws MailException {
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println(body);
    }
}
