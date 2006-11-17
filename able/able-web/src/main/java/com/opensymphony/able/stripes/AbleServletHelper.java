package com.opensymphony.able.stripes;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class AbleServletHelper extends HttpServlet {
    public static AbleServletHelper servlet;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        AbleServletHelper.servlet = this;
    }
}
