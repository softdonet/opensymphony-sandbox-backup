package com.opensymphony.able.service;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.io.UnsupportedEncodingException;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class MailServiceImpl implements MailService {
    public void send(String to, String subject, String body) throws MailException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            try {
                msg.setFrom(new InternetAddress("support@able.com", "Juice"));
            } catch (UnsupportedEncodingException e) {
                throw new MailException("Unexpected encoding problem while constructing From address", e);
            }
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText(body);

            Transport.send(msg);
        } catch (MessagingException e) {
            throw new MailException("Unable to send message with subject '" + subject + "' to '" + to + "'", e);
        }
    }
}
