package com.opensymphony.able.demo.action;

import com.opensymphony.able.annotations.Secure;
import com.opensymphony.able.demo.model.User;
import com.opensymphony.able.demo.service.UserService;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.ValidationMethod;

@Secure
public class UserSettingsActionBean extends AbstractUserActionBean {
    @SpringBean
    private UserService userService;

    private String password;
    private String passwordAgain;

    @DefaultHandler
    @DontValidate
    public Resolution execute() {
        user = getCurrentUser();

        return forward();
    }

    @Before
    public void before() {
        user = getCurrentUser();
    }

    @ValidationMethod
    public void validate() {
        if (password != null || passwordAgain != null) {
            password = noNull(password);
            passwordAgain = noNull(passwordAgain);

            if (!password.equals(passwordAgain)) {
                addFieldError("password", "passwordMismatch");
            }
        }
    }

    private String noNull(String s) {
        if (s == null) {
            return "";
        } else {
            return s;
        }
    }

    public Resolution update() {
        if (getUserId() != user.getId()) {
            // someone is tampering with the form! shame on them!
            // I'm so angry someone would do that I won't even provide a nice error message
            return getContext().getSourcePageResolution();
        }

        if (password != null) {
            String hash = userService.encrypt(password);
            user.setPasswordHash(hash);
        }

        userService.update(user);

        return new RedirectResolution(HomeActionBean.class);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }
}
