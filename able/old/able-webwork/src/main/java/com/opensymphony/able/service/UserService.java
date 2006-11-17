package com.opensymphony.able.service;

import com.opensymphony.able.model.User;
import com.opensymphony.able.util.Blowfish;
import com.opensymphony.util.GUID;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * @author Nicholas Hill <a href="mailto:nick.hill@gmail.com">nick.hill@gmail.com</a>
 * @author <a href="mailto:jhouse@revolition.net">James House</a>
 */
public class UserService extends DaoService<User> {
    protected Blowfish cipher = new Blowfish("secretkey");
    private MailService mailService;

    public UserService() {
        super(User.class);
    }

    public void init() throws ServiceException {
        super.init();
    }

    public long create(User user, String password) {
        user.setPasswordHash(encrypt(password));
        if (user.getUpdateDate() == null) {
            user.setUpdateDate(user.getCreationDate());
        }

        return (Long) dao.insert(user);
    }

    public User findById(long userId) {
        return dao.selectSingle("byId", userId);
    }

    public User findByUsername(String username) {
        return dao.selectSingle("byUsername", username);
    }

    public User findByUsernameIgnoreCase(String username) {
        return dao.selectSingle("byUsernameIgnoreCase", username);
    }

    public User findByEmail(String email) {
        return dao.selectSingle("byEmail", email);
    }

    public List<User> findByUpdatedAfterDate(Date updatedAfter) {
        return dao.select("byUpdatedAfter", updatedAfter);
    }

    public int getUserCount() {
        return (Integer) dao.selectValue("count");
    }

    public List<User> findAll() {
        return dao.select("all");
    }

    public void save(User user) {
        user.setUpdateDate(new Date());
        dao.update(user);
    }

    public void remove(long userId) {
        dao.delete("byId", userId);
    }

    public void forgotPassword(String username) throws MailException {
        // generate a new password
        String password = GUID.generateGUID();

        // set the new password
        User user = findByUsername(username);
        user.setPasswordHash(encrypt(password));
        dao.update("password", user);

        // email it out
        mailService.send(user.getEmail(), "Able: Reset Password", "Your new password is: " + password);
    }

    public void changePassword(User user, String newPassword) {
        user.setPasswordHash(encrypt(newPassword));
        dao.update("password", user);
    }

    public boolean authenticate(String username, String password) {
        User user = findByUsernameIgnoreCase(username);
        if (user == null) {
            return false;
        }

        return user.getPasswordHash().equals(encrypt(password));
    }

    private synchronized String encrypt(String plainText) {
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

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public String encryptAuthInfo(String username, String password) {
        if (username == null || password == null) {
            throw new NullPointerException("Username or password was null.");
        }
        return cipher.encryptString(username + '\002' + password);
    }

    public String [] decryptAuthInfo(String value) {
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
}
