package com.opensymphony.able.stripes;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.integration.spring.SpringBean;
import com.opensymphony.able.service.UserService;

public class LoginActionBean extends AbstractActionBean {
    @SpringBean
    private UserService userService;

    @Validate(required = true)
    private String username;

    @Validate(required = true)
    private String password;

    private boolean remember;

    @DefaultHandler
    @DontValidate
    public Resolution prompt() {
        return forward();
    }

    @ValidationMethod(on = "login")
    public void validateLogin() {
        if (!userService.authenticate(username, password)) {
            addGlobalError("authenticationError");
        }
    }

    public Resolution login() {
        ctx.setUser(userService.findByUsername(username));

        if (remember) {
            ctx.remember(username, password);
        }

        Resolution resolution = ctx.getAfterLoginResolution();
        if (resolution != null) {
            return resolution;
        } else {
            return new RedirectResolution(HomeActionBean.class);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
