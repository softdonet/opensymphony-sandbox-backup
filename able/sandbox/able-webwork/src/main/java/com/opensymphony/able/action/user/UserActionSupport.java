package com.opensymphony.able.action.user;

import com.opensymphony.able.action.AbleActionSupport;
import com.opensymphony.able.model.User;

public class UserActionSupport extends AbleActionSupport {
    protected long userId;
    protected User user;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public User getUser() {
        if (user == null) {
            user = userService.findById(userId);
        }

        return user;
    }
}
