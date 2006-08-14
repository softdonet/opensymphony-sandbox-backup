package com.opensymphony.able.action;

import com.opensymphony.webwork.interceptor.ServletResponseAware;
import com.opensymphony.able.model.User;
import com.opensymphony.able.util.UserToken;
import com.opensymphony.able.webwork.Redirect;
import com.opensymphony.able.webwork.Result;
import com.opensymphony.able.webwork.Results;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:travhoang@yahoo.com">Travis Hoang</a>
 */
@Results(results = {
@Result(type = Redirect.class, location = "home")
        })
public class LoginAction extends JuiceActionSupport implements ServletResponseAware {
    private String username;
    private String password;
    private boolean remember;

    private HttpServletResponse response;

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void validate() {
        if (!userService.authenticate(username, password)) {
            addFieldError("password", "Your username and password is invalid");
        }
    }

    public String execute() throws Exception {
        User user = userService.findByUsernameIgnoreCase(username);
        session.put("userToken", new UserToken(user.getId()));

        if (remember) {
            String value = userService.encryptAuthInfo(username, password);
            Cookie cookie = new Cookie("able.auth", value);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
            response.addCookie(cookie);
        }

        return SUCCESS;
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

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
