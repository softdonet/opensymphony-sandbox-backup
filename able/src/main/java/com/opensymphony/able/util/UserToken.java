package com.opensymphony.able.util;

/**
 * @author Nicholas Hill <a href="mailto:nick.hill@gmail.com">nick.hill@gmail.com</a>
 */
public class UserToken {
    private long userId;

    public UserToken(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
