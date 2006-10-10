package com.opensymphony.able.stripes.util;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.util.Log;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DefaultResolution extends ForwardResolution {
    private boolean autoInclude = true;
    private static final Log log = Log.getInstance(ForwardResolution.class);
    private String eventName;
    private String fallback;
    private ServletContext servletContext;

    public DefaultResolution(Class<? extends ActionBean> clazz, ActionBeanContext context, String fallback) {
        super(clazz);
        this.fallback = fallback;
        eventName = context.getEventName();
        servletContext = context.getServletContext();
    }

    public DefaultResolution(Class<? extends ActionBean> clazz, ActionBeanContext context) {
        super(clazz);
        eventName = context.getEventName();
        servletContext = context.getServletContext();
    }

    /**
     * If true then the ForwardResolution will automatically detect when it is executing
     * as part of a server-side Include and <i>include</i> the supplied URL instead of
     * forwarding to it.  Defaults to true.
     *
     * @param auto whether or not to automatically detect and use includes
     */
    public void autoInclude(boolean auto) {
        this.autoInclude = auto;
    }

    /**
     * Attempts to forward the user to the specified path.
     *
     * @throws ServletException thrown when the Servlet container encounters an error
     * @throws IOException      thrown when the Servlet container encounters an error
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = getUrl();

        String path = "/WEB-INF/jsp" + url + "/" + eventName + ".jsp";
        String realPath = servletContext.getRealPath(path);
        if (!new File(realPath).exists()) {
            path = "/WEB-INF/jsp" + url + ".jsp";
            realPath = servletContext.getRealPath(path);
            if (!new File(realPath).exists() && fallback != null) {
                path = fallback;
            }
        }

        // Figure out if we're inside an include, and use an include instead of a forward
        if (autoInclude && request.getAttribute(StripesConstants.REQ_ATTR_INCLUDE_PATH) != null) {
            log.trace("Including URL: ", path);
            request.getRequestDispatcher(path).include(request, response);
        } else {
            log.trace("Forwarding to URL: ", path);
            request.getRequestDispatcher(path).forward(request, response);
        }
    }
}
