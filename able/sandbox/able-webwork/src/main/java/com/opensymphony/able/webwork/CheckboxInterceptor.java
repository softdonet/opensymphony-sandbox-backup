package com.opensymphony.able.webwork;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

public class CheckboxInterceptor implements Interceptor {
    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation ai) throws Exception {
        Map parameters = ai.getInvocationContext().getParameters();
        Map<String, Boolean> newParams = new HashMap<String, Boolean>();
        Set<String> keys = parameters.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            String key = iterator.next();

            if (key.startsWith("__checkbox_")) {
                String name = key.substring("__checkbox_".length());

                iterator.remove();

                // is this checkbox checked/submitted?
                if (!parameters.containsKey(name)) {
                    // if not, let's be sure to default the value to false
                    newParams.put(name, false);
                }
            }
        }

        parameters.putAll(newParams);

        return ai.invoke();
    }
}
