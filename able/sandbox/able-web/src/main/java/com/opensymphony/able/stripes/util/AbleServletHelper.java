package com.opensymphony.able.stripes.util;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class AbleServletHelper extends HttpServlet {
    public static AbleServletHelper servlet;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        AbleServletHelper.servlet = this;
    }
}
