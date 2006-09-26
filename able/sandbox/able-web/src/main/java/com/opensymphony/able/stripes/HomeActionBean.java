package com.opensymphony.able.stripes;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import com.opensymphony.able.service.UserService;
import com.opensymphony.able.model.User;

import java.util.List;

public class HomeActionBean extends AbstractActionBean {
    @SpringBean
    private UserService userService;
    private List<User> users;

    @DefaultHandler
    public Resolution home() {
        users = userService.findAll();

        return new ForwardResolution("/WEB-INF/jsp/home.jsp");
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}