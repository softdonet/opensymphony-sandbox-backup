package com.opensymphony.able.webwork;

import com.opensymphony.xwork.interceptor.Interceptor;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;

import java.util.Map;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class FlashInterceptor implements Interceptor {
    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation ai) throws Exception {
        Map session = ai.getInvocationContext().getSession();
        Object action = session.get("__flash");
        if (action != null) {
            session.remove("__flash");

            OgnlValueStack stack = ai.getStack();
            stack.push(action);
        }

        return ai.invoke();
    }
}
