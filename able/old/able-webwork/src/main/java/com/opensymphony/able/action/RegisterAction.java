package com.opensymphony.able.action;

import com.opensymphony.able.model.User;
import com.opensymphony.able.webwork.Flash;
import com.opensymphony.able.webwork.Result;
import com.opensymphony.able.webwork.Results;

import java.util.Date;

@Results(results = {
@Result(type = Flash.class, location = "registered")
        })
public class RegisterAction extends AbleActionSupport {
    private User user;
    private String password;
    private String confirmPassword;

    public void validate() {
        if (null != userService.findByEmail(user.getEmail())) {
            addFieldError("user.email", "The email specified is already in use");
        }

        if (null != userService.findByUsernameIgnoreCase(user.getUsername())) {
            addFieldError("user.username", "The username specified is already in use");
        }

        if (!(confirmPassword.equals(password))) {
            addFieldError("confirmPassword", "The confirmation password does not match with the password");
        }
    }

    public String execute() throws Exception {
        user.setCreationDate(new Date());
        user.setUsername(user.getUsername());
        userService.create(user, password);

        return SUCCESS;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
