package com.opensymphony.able.webwork;

import com.opensymphony.xwork.ActionInvocation;

import java.util.Map;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class Flash extends Redirect {
    protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
        // before we redirect, let's save the state in to the session
        Object action = invocation.getAction();
        Map session = invocation.getInvocationContext().getSession();
        session.put("__flash", action);

        super.doExecute(finalLocation, invocation);
    }
}
