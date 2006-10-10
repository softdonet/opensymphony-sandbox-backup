package com.opensymphony.able.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import com.opensymphony.able.stripes.util.DefaultResolution;

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
