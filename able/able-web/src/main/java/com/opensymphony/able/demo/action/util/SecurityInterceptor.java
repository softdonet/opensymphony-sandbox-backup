package com.opensymphony.able.demo.action.util;

import com.opensymphony.able.annotations.Secure;
import com.opensymphony.able.demo.action.LoginActionBean;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

@Intercepts(LifecycleStage.HandlerResolution)
public class SecurityInterceptor implements Interceptor {
    public Resolution intercept(ExecutionContext ctx) throws Exception {
        Resolution resolution = ctx.proceed();

        // support both class and method level annotation
        Secure secure = ctx.getHandler().getAnnotation(Secure.class);
        if (secure == null) {
            secure = ctx.getActionBean().getClass().getAnnotation(Secure.class);
        }

        AbleActionBeanContext abc = ((AbleActionBeanContext) ctx.getActionBeanContext());
        abc.autoLogin();

        if (secure != null && !abc.isUserLoggedIn()) {
            abc.saveAfterLoginLocation();

            return new RedirectResolution(LoginActionBean.class);
        } else {
            return resolution;
        }
    }
}
