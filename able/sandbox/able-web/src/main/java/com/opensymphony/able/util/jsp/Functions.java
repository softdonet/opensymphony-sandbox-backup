package com.opensymphony.able.util.jsp;

import java.util.Collection;
import java.util.Map;

public class Functions {
    public static boolean contains(Object o1, Object o2) {
        if (o1 instanceof Collection) {
            return ((Collection) o1).contains(o2);
        }

        return false;
    }

    public static boolean isEmpty(Object o) {
        if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        } else if (o instanceof Map) {
            return ((Map) o).isEmpty();
        }

        return false;
    }

    public static String extractFormName(String className, String eventName) {
        String name = className.substring(className.lastIndexOf(".") + 1);
        if (name.endsWith("ActionBean")) {
            name = name.substring(0, name.length() - "ActionBean".length());
        } else if (name.endsWith("Action")) {
            name = name.substring(0, name.length() - "Action".length());
        }

        name = name.replaceAll("\\.", "_");
        name = name.toLowerCase();

        return eventName + "_" + name;
    }
}
