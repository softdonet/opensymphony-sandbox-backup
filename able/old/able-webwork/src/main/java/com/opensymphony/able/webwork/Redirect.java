/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.able.webwork;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.WebWorkResultSupport;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <!-- START SNIPPET: description -->
 * <p/>
 * Calls the {@link HttpServletResponse#sendRedirect(String) sendRedirect}
 * method to the location specified. The response is told to redirect the
 * browser to the specified location (a new request from the client). The
 * consequence of doing this means that the action (action instance, action
 * errors, field errors, etc) that was just executed is lost and no longer
 * available. This is because actions are built on a single-thread model. The
 * only way to pass data is through the session or with web parameters
 * (url?name=value) which can be OGNL expressions.
 * <p/>
 * <!-- END SNIPPET: description -->
 * <p/>
 * <b>This result type takes the following parameters:</b>
 * <p/>
 * <!-- START SNIPPET: params -->
 * <p/>
 * <ul>
 * <p/>
 * <li><b>location (default)</b> - the location to go to after execution.</li>
 * <p/>
 * <li><b>parse</b> - true by default. If set to false, the location param will
 * not be parsed for Ognl expressions.</li>
 * <p/>
 * </ul>
 * <p/>
 * <p/>
 * This result follows the same rules from {@link WebWorkResultSupport}.
 * </p>
 * <p/>
 * <!-- END SNIPPET: params -->
 * <p/>
 * <b>Example:</b>
 * <p/>
 * <pre><!-- START SNIPPET: example -->
 * &lt;result name="success" type="redirect"&gt;
 *   &lt;param name="location"&gt;foo.jsp&lt;/param&gt;
 *   &lt;param name="parse"&gt;false&lt;/param&gt;
 * &lt;/result&gt;
 * <!-- END SNIPPET: example --></pre>
 *
 * @author Patrick Lightbody
 */
public class Redirect extends WebWorkResultSupport {
    private static final Log log = LogFactory.getLog(Redirect.class);

    /**
     * Redirects to the location specified by calling {@link HttpServletResponse#sendRedirect(String)}.
     *
     * @param finalLocation the location to redirect to.
     * @param invocation    an encapsulation of the action execution state.
     * @throws Exception if an error occurs when redirecting.
     */
    protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
        ActionContext ctx = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);

        if (!finalLocation.startsWith("http") && !finalLocation.startsWith("/")) {
            String path = request.getServletPath();
            int slash = path.lastIndexOf('/');
            String base = path.substring(0, slash);

            if ((base != null) && (base.length() > 0) && (!"/".equals(base))) {
                finalLocation = base + "/" + finalLocation;
            } else {
                finalLocation = "/" + finalLocation;
            }
        }

        finalLocation = response.encodeRedirectURL(finalLocation);

        if (log.isDebugEnabled()) {
            log.debug("Redirecting to finalLocation " + finalLocation);
        }

        response.sendRedirect(finalLocation);
    }
}
