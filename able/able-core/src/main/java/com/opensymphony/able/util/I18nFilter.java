package com.opensymphony.able.util;

import javax.servlet.*;
import java.io.IOException;
import java.util.ResourceBundle;
import java.lang.reflect.Field;

public class I18nFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // reset i18n resource bundle cache
        try {
            Class klass = ResourceBundle.getBundle("StripesResources").getClass().getSuperclass();
            Field field = klass.getDeclaredField("cacheList");
            field.setAccessible(true);
            sun.misc.SoftCache cache = (sun.misc.SoftCache)field.get(null);
            cache.clear();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
