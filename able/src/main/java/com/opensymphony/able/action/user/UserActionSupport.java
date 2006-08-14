package com.opensymphony.able.action.user;

import com.opensymphony.able.action.JuiceActionSupport;
import com.opensymphony.able.model.User;

public class UserActionSupport extends JuiceActionSupport {
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
