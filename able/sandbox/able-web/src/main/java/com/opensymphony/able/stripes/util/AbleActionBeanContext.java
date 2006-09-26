package com.opensymphony.able.stripes.util;

import com.opensymphony.able.model.User;
import com.opensymphony.able.service.UserService;
import com.opensymphony.able.util.Log;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ActionResolver;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.integration.spring.SpringHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

public class AbleActionBeanContext extends ActionBeanContext {
    private static final Log log = new Log();

    public static final String USER_KEY = "__userId";
    public static final String AFTER_LOGIN = "__afterLogin";

    @SpringBean
    private UserService userService;
    private User user;
    private String afterLoginUri;

    /**
     * helper method to indicate if the current user is logged in.
     * @return true if logged in
     */
    public boolean isUserLoggedIn() {
        return getUser() != null;
    }

    public User getUser() {
        Long id = (Long) getSession().getAttribute(USER_KEY);
        if (id != null) {
            user = userService.findById(id);
        }

        return user;
    }

    public void setUser(User user) {
        getSession().setAttribute(USER_KEY, user.getId());
        this.user = user;
    }

    public void setServletContext(ServletContext servletContext) {
        SpringHelper.injectBeans(this, servletContext);
        super.setServletContext(servletContext);
    }

    public void logout() {
        this.user = null;
        getSession().setAttribute(USER_KEY, null);
        removeCookie();
    }

    private HttpSession getSession() {
        return getRequest().getSession(true);
    }

    public void saveAfterLoginLocation() {
        String uri = getRequest().getAttribute(ActionResolver.RESOLVED_ACTION).toString();
        getSession().setAttribute(AFTER_LOGIN, uri);
    }

    public Resolution getAfterLoginResolution() {
        String afterLoginUri = (String) getSession().getAttribute(AFTER_LOGIN);
        if (afterLoginUri != null) {
            return new RedirectResolution(afterLoginUri);
        } else {
            return null;
        }
    }

    public void autoLogin() {
        if (!isUserLoggedIn()) {
            Cookie[] cookies = getRequest().getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("able.auth")) {
                        String value = cookie.getValue();
                        String[] usernameAndPassword = userService.decryptAuthInfo(value);
                        if (usernameAndPassword != null) {
                            String username = usernameAndPassword[0];
                            String password = usernameAndPassword[1];

                            if (userService.authenticate(username, password)) {
                                User user = userService.findByUsername(username);
                                if (user != null) {
                                    setUser(user);
                                } else {
                                    // not sure why we can't find the user, but log them out
                                    log.warn("Found null user after successful authentication. " +
                                            "This is an UNKNOWN state so we are clearing the cookies continuing");
                                    removeCookie();
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void removeCookie() {
        Cookie cookie = new Cookie("cparty.auth", "");
        cookie.setPath("/");
        cookie.setMaxAge(0); // remove now
        getResponse().addCookie(cookie);
    }

    public void remember(String username, String password) {
        String value = userService.encryptAuthInfo(username, password);
        Cookie cookie = new Cookie("able.auth", value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
        getResponse().addCookie(cookie);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
