package com.opensymphony.able.service;

import com.opensymphony.able.model.User;
import com.opensymphony.able.util.Blowfish;
import org.springframework.orm.jpa.JpaTemplate;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService extends JpaCrudService<User> {
    protected Blowfish cipher = new Blowfish("secretkey");

    public UserService(JpaTemplate jpaTemplate) {
        super(jpaTemplate);
    }

    public User findByUsername(String username) {
        return (User) findFirst("from User u where u.username = ?1", username);
    }

    public boolean authenticate(String username, String password) {
        String hash = encrypt(password);
        User account = findByUsername(username);

        if (account == null || account.getPasswordHash() == null) {
            return false;
        }

        return account.getPasswordHash().equals(hash);
    }

    public synchronized String encrypt(String plainText) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            String msg = "SHA not found, encryption cannot continue, no recovery possible";
            log.severe(msg, e);
            throw new RuntimeException(msg, e);
        }

        try {
            md.update(plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            String msg = "UTF-8 encoding not found, no recovery possible";
            log.severe(msg, e);
            throw new RuntimeException(msg, e);
        }

        byte[] raw = md.digest();

        return new BASE64Encoder().encode(raw);
    }

    public String[] decryptAuthInfo(String value) {
        // Check that the cookie value isn't null or zero-length
        if (value == null || value.length() <= 0) {
            return null;
        }
        // Decode the cookie value
        value = cipher.decryptString(value);
        if (value == null) {
            return null;
        }

        int pos = value.indexOf('\002');
        String username = (pos < 0) ? "" : value.substring(0, pos);
        String password = (pos < 0) ? "" : value.substring(pos + 1);

        return new String[]{username, password};
    }

    public String encryptAuthInfo(String username, String password) {
        return null;
    }

}
