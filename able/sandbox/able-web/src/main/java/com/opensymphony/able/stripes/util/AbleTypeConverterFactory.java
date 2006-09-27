package com.opensymphony.able.stripes.util;

import com.opensymphony.able.model.User;
import net.sourceforge.stripes.config.Configuration;

public class AbleTypeConverterFactory extends JpaTypeConverterFactory {
    public void init(Configuration configuration) {
        super.init(configuration);

        add(User.class, UserTypeConverter.class);
    }
}
