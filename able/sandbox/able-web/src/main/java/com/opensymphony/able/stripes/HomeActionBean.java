package com.opensymphony.able.stripes;

import com.opensymphony.able.entity.Entities;
import com.opensymphony.able.entity.EntityInfo;
import com.opensymphony.able.model.User;
import com.opensymphony.able.service.UserService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.Collection;
import java.util.List;

public class HomeActionBean extends AbstractActionBean {
    @SpringBean
    private UserService userService;
    private List<User> users;

    @DefaultHandler
    public Resolution home() {
        users = userService.findAll();

        return forward();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Collection<EntityInfo> getEntities() {
        return Entities.getInstance().getEntities();
    }
}
