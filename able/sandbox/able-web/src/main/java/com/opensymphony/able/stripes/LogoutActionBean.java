package com.opensymphony.able.stripes;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;

public class LogoutActionBean extends AbstractActionBean {
    @DefaultHandler
    public Resolution logout() {
        ctx.logout();

        return new RedirectResolution(HomeActionBean.class);
    }
}
