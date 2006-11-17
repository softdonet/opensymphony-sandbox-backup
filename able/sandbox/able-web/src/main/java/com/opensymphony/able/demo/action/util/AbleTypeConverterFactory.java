package com.opensymphony.able.demo.action.util;

import com.opensymphony.able.demo.model.User;
import com.opensymphony.able.stripes.util.JpaTypeConverterFactory;
import net.sourceforge.stripes.config.Configuration;

public class AbleTypeConverterFactory extends JpaTypeConverterFactory {
    public void init(Configuration configuration) {
        super.init(configuration);

        add(User.class, UserTypeConverter.class);
    }
}
