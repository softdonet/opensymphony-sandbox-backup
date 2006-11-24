package com.opensymphony.able.demo.action;

import com.opensymphony.able.demo.service.UserService;
import com.opensymphony.able.filter.TransactionOutcome;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import java.io.StringReader;
import java.util.logging.Logger;

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
        return forward();
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

        TransactionOutcome.shouldCommit();

        //auto login
        ctx.setUser(user);

        return new RedirectResolution(HomeActionBean.class);
    }

      private static final Logger myLogger =
                     Logger.getLogger(RegisterActionBean.class.getPackage().getName());

    @HandlesEvent("addUser")
    public Resolution addUser() {
        user.setPasswordHash(userService.encrypt(password));
        userService.insert(user);

        TransactionOutcome.shouldCommit();

        String userInfo = user.getName() + "(" + user.getEmail() + ")";

        return new StreamingResolution("newUser", new StringReader(userInfo));
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
