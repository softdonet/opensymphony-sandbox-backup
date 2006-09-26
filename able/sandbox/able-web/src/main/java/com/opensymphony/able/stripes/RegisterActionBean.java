package com.opensymphony.able.stripes;

import com.opensymphony.able.filter.TransactionServletFilter;
import com.opensymphony.able.service.UserService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

public class RegisterActionBean extends AbstractUserActionBean {
    @SpringBean
    private UserService userService;

    @Validate(minlength = 6, required = true)
    private String password;
    @Validate(minlength = 6, required = true)
    private String passwordAgain;

    @DefaultHandler
    @DontValidate
    public Resolution prompt() {
        return new ForwardResolution("/WEB-INF/jsp/register.jsp");
    }

    @ValidationMethod(on = "register")
    public void registerValidate() {
        if (!password.equals(passwordAgain)) {
            addFieldError("password", "passwordMismatch");
        }
    }

    public Resolution register() {
        user.setPasswordHash(userService.encrypt(password));
        userService.insert(user);
        TransactionServletFilter.shouldCommit(getContext().getRequest());

        //auto login
        ctx.setUser(user);

        return new RedirectResolution(HomeActionBean.class);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

}
