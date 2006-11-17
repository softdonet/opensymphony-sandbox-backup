package com.opensymphony.able.demo.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

public class LogoutActionBean extends AbstractActionBean {
    @DefaultHandler
    public Resolution logout() {
        ctx.logout();

        return new RedirectResolution(HomeActionBean.class);
    }
}
