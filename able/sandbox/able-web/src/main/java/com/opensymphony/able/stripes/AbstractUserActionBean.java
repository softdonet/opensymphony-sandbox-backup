package com.opensymphony.able.stripes;

import com.opensymphony.able.model.User;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.Validate;

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
