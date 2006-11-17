package com.opensymphony.able.demo.action;

import com.opensymphony.able.demo.model.User;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

public class AbstractUserActionBean extends AbstractActionBean {
    @ValidateNestedProperties(
            {@Validate(field = "name", required = true),
            @Validate(field = "email", required = true, mask = ".*@.*")}
    )
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
