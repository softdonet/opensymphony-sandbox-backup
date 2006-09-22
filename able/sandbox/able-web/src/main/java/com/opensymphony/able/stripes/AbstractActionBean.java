package com.opensymphony.able.stripes;

import com.opensymphony.able.model.User;
import com.opensymphony.able.stripes.util.AbleActionBeanContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.validation.LocalizableError;

public abstract class AbstractActionBean implements ActionBean {
    protected AbleActionBeanContext ctx;

    public void setContext(ActionBeanContext ctx) {
        this.ctx = (AbleActionBeanContext) ctx;
    }

    public ActionBeanContext getContext() {
        return ctx;
    }

    protected void addFieldError(String field, String key, Object... params) {
        getContext().getValidationErrors().add(field, new LocalizableError(key, params));
    }

    protected void addGlobalError(String key) {
        ctx.getValidationErrors().addGlobalError(new LocalizableError(key));
    }

    public boolean isUserLoggedIn() {
        return ctx.isUserLoggedIn();
    }

    public User getCurrentUser() {
        return ctx.getUser();
    }

    public long getUserId() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return -1;
        } else {
            return currentUser.getId();
        }
    }

}
