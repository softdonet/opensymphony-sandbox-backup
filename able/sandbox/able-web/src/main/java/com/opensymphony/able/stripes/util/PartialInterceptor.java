package com.opensymphony.able.stripes.util;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Intercepts(LifecycleStage.EventHandling)
public class PartialInterceptor implements Interceptor {
    public Resolution intercept(ExecutionContext context) throws Exception {

        Method method = context.getHandler();

        //guard condition
        if (method == null) {
            return context.proceed();
        }

        if (method.getAnnotation(Partial.class) != null) {
            HttpServletRequest req = context.getActionBeanContext().getRequest();
            req.setAttribute("decorator", "none");
        }

        return context.proceed();
    }
}
