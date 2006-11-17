package com.opensymphony.able.webwork;

import com.opensymphony.xwork.interceptor.Interceptor;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.webwork.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class PartialInterceptor implements Interceptor {
    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation ai) throws Exception {
        Object action = ai.getAction();
        if (action.getClass().getAnnotation(Partial.class) != null) {
            HttpServletRequest req = (HttpServletRequest) ai.getInvocationContext().get(ServletActionContext.HTTP_REQUEST);
            req.setAttribute("decorator", "none");
        }

        return ai.invoke();
    }
}
