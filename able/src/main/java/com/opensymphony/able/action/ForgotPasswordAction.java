package com.opensymphony.able.action;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class ForgotPasswordAction extends JuiceActionSupport {
    private String username;

    public String execute() throws Exception {
        userService.forgotPassword(username);

        return SUCCESS;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
