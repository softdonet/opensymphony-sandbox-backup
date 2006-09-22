package com.opensymphony.able.stripes.util;

import net.sourceforge.stripes.config.Configuration;
import com.opensymphony.able.model.User;

public class AbleTypeConverterFactory extends SpringTypeConverterFactory {
    public void init(Configuration configuration) {
        super.init(configuration);

        add(User.class, UserTypeConverter.class);
    }
}
