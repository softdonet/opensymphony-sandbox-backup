package com.opensymphony.able.action;

import com.opensymphony.able.model.User;
import com.opensymphony.able.service.UserService;
import com.opensymphony.able.util.UserToken;
import com.opensymphony.webwork.interceptor.SessionAware;
import com.opensymphony.xwork.ActionSupport;

import java.util.Map;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class AbleActionSupport extends ActionSupport implements SessionAware {
    protected UserService userService;
    protected User currentUser;

    protected Map session;

    public void setSession(Map session) {
        this.session = session;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            UserToken userToken = (UserToken) session.get("userToken");
            if (userToken == null) {
                return null;
            }

            currentUser = userService.findById(userToken.getUserId());
        }

        return currentUser;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
