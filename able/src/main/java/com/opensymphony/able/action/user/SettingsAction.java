package com.opensymphony.able.action.user;

import com.opensymphony.able.action.JuiceActionSupport;
import com.opensymphony.able.webwork.Redirect;
import com.opensymphony.able.webwork.Result;

@Result(type = Redirect.class, location = "../home")
public class SettingsAction extends JuiceActionSupport {
    public String execute() throws Exception {
        userService.save(getCurrentUser());

        return SUCCESS;
    }
}
