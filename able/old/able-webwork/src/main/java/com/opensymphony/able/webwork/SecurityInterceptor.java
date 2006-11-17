package com.opensymphony.able.webwork;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

import java.util.Map;


/**
 * @author <a href="mailto:travhoang@yahoo.com">Travis Hoang</a>
 */
public class SecurityInterceptor implements Interceptor {
    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation ai) throws Exception {
        Map session = ai.getInvocationContext().getSession();

        if (!(session.containsKey("userToken"))) {
            return "login";
        }

        return ai.invoke();
    }
}
