package com.opensymphony.able.stripes;

import net.sourceforge.stripes.controller.NameBasedActionResolver;

/**
 * We prefer "home" rather than "Home.action", which this class takes care of.
 */
public class AbleNameBasedActionResolver extends NameBasedActionResolver {
    protected String getUrlBinding(String string) {
        String original = super.getUrlBinding(string);

        int end = original.indexOf(".action");
        String temp = original.substring(0, end);
        int begin = temp.lastIndexOf("/");
        String name = original.substring(begin + 1, end);
        name = name.substring(0, 1).toLowerCase() + name.substring(1);

        return original.substring(0, begin + 1).toLowerCase() + name ;
    }
}
