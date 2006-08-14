package com.opensymphony.able.filter;

import com.opensymphony.able.model.User;
import com.opensymphony.able.service.UserService;
import com.opensymphony.able.util.UserToken;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class CookieLoginFilter implements Filter {
    private UserService userService;

    public void init(FilterConfig config) throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        userService = (UserService) ctx.getBean("userService");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        if (session.getAttribute("userToken") == null) {

            Cookie[] cookies = req.getCookies();
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
                                session.setAttribute("userToken", new UserToken(user.getId()));
                            }
                        }
                    }
                }
            }
        }

        filterChain.doFilter(req, response);
    }

    public void destroy() {
    }
}
