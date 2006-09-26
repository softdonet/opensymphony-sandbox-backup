package com.opensymphony.able.stripes.util;

import com.opensymphony.able.model.User;
import com.opensymphony.able.service.UserService;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import java.util.Collection;
import java.util.Locale;

public class UserTypeConverter implements TypeConverter<User> {
    @SpringBean
    private UserService userService;

    public void setLocale(Locale locale) {
    }

    public User convert(String input, Class<? extends User> type, Collection<ValidationError> errors) {
        try {
            long id = Long.parseLong(input);
            return userService.findById(id);
        } catch (NumberFormatException e) {
            errors.add(new ScopedLocalizableError("converter.user", "invalidUserId"));
            return null;
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
