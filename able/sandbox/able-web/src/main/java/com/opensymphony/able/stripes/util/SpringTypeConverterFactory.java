package com.opensymphony.able.stripes.util;

import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.integration.spring.SpringHelper;

import javax.servlet.ServletContext;
import java.util.Locale;

public class SpringTypeConverterFactory extends DefaultTypeConverterFactory {
    public TypeConverter getInstance(Class<? extends TypeConverter> clazz, Locale locale) throws Exception {
        TypeConverter tc = super.getInstance(clazz, locale);
        ServletContext sc = getConfiguration().getServletContext();

        SpringHelper.injectBeans(tc, sc);

        return tc;
    }
}
