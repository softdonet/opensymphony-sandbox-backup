package com.opensymphony.able.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.mock.MockHttpServletResponse;
import net.sourceforge.stripes.util.Log;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateResolution extends ForwardResolution {
    private static final Log log = Log.getInstance(ForwardResolution.class);
    private String eventName;
    private ServletContext servletContext;

    public GenerateResolution(Class<? extends ActionBean> clazz, ActionBeanContext context) {
        super(clazz);
        this.eventName = context.getEventName();
        this.servletContext = context.getServletContext();
    }

    /**
     * Attempts to forward the user to the specified path.
     *
     * @throws ServletException thrown when the Servlet container encounters an error
     * @throws IOException      thrown when the Servlet container encounters an error
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = eventName.substring("generate".length()).toLowerCase();
        String url = "/WEB-INF/jsp/generic/" + eventName + ".jsp";
        String target = "/WEB-INF/jsp" + getUrl() + "/" + name + ".jsp";
        String realPath = servletContext.getRealPath(target);

        MockHttpServletResponse mockResp = new MockHttpServletResponse() {
            public void resetBuffer() {
                //super.resetBuffer();
            }

            public void reset() {
                //super.reset();
            }
        };

        // Figure out if we're inside an include, and use an include instead of a forward
        if (request.getAttribute(StripesConstants.REQ_ATTR_INCLUDE_PATH) != null) {
            log.trace("Including URL: ", url);
            request.getRequestDispatcher(url).include(request, mockResp);
        } else {
            log.trace("Forwarding to URL: ", url);
            request.getRequestDispatcher(url).forward(request, mockResp);
        }

        File file = new File(realPath);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        String out = mockResp.getOutputString();
        out = out.replaceAll("\\&lt\\;", "<").replaceAll("\\&gt\\;", ">").trim();
        writer.write(out);
        writer.close();

        response.setContentType("text/plain");
        response.getWriter().write("We have generated a JSP at " + realPath);
    }
}
