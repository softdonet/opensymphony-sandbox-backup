package com.opensymphony.able.action;

import com.opensymphony.able.stripes.DefaultResolution;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

public class DefaultActionBean implements ActionBean {
    protected ActionBeanContext context;

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    protected DefaultResolution forward() {
        return new DefaultResolution(getClass(), getContext());
    }
}
