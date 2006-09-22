package com.opensymphony.able.stripes.util;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.integration.spring.SpringBean;
import com.opensymphony.able.model.User;
import com.opensymphony.able.service.UserService;

import java.util.Locale;
import java.util.Collection;

public class UserTypeConverter implements TypeConverter<User> {
    @SpringBean
    private UserService userService;

    public void setLocale(Locale locale) {
    }

    public User convert(String input, Class<? extends User> type, Collection<ValidationError> errors) {
        try {
            long id = Long.parseLong(input);
            return userService.findUserById(id);
        } catch (NumberFormatException e) {
            errors.add(new ScopedLocalizableError("converter.user", "invalidUserId"));
            return null;
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
