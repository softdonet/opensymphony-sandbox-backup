package com.opensymphony.able.action;

import com.opensymphony.webwork.interceptor.ServletResponseAware;
import com.opensymphony.able.webwork.Redirect;
import com.opensymphony.able.webwork.Result;
import com.opensymphony.able.webwork.Results;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Nicholas Hill <a href="mailto:nick.hill@gmail.com">nick.hill@gmail.com</a>
 */
@Results(results = {
@Result(type = Redirect.class, location = "home")
        })
public class LogoutAction extends AbleActionSupport implements ServletResponseAware {
    private HttpServletResponse response;

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String execute() throws Exception {
        session.remove("userToken");

        // expire autologin cookie
        Cookie cookie = new Cookie("able.auth", "");
        cookie.setPath("/");
        cookie.setMaxAge(0); // remove now
        response.addCookie(cookie);

        return SUCCESS;
    }
}
