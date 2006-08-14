package com.opensymphony.able.webwork;

import com.opensymphony.webwork.dispatcher.ServletDispatcherResult;
import com.opensymphony.xwork.ObjectFactory;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationException;
import com.opensymphony.xwork.config.RuntimeConfiguration;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.InterceptorMapping;
import com.opensymphony.xwork.config.entities.PackageConfig;
import com.opensymphony.xwork.config.entities.ResultConfig;
import com.opensymphony.xwork.config.providers.InterceptorBuilder;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import com.opensymphony.able.util.EmptyMap;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.io.File;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public class AbleConfiguration implements Configuration {
    private ServletContext servletContext;
    private PackageConfig webworkDefault;

    public AbleConfiguration(ServletContext servletContext) {
        this.servletContext = servletContext;

        XmlConfigurationProvider xml = new XmlConfigurationProvider("webwork-default.xml");
        xml.init(this);
    }

    public void rebuildRuntimeConfiguration() {
    }

    public PackageConfig getPackageConfig(String name) {
        if (name.equals("webwork-default")) {
            return webworkDefault;
        }

        return null;
    }

    public Set getPackageConfigNames() {
        return Collections.EMPTY_SET;
    }

    public Map getPackageConfigs() {
        return Collections.EMPTY_MAP;
    }

    public RuntimeConfiguration getRuntimeConfiguration() {
        return new RuntimeConfiguration() {
            public ActionConfig getActionConfig(String namespace, final String name) {
                ActionConfig ac = new ActionConfig();

                // name is somethig like 'suite/test/foo'
                // which should now become: suite.test.Foo

                try {
                    String className = name.replaceAll("\\/", "\\.");
                    int lastDot = className.lastIndexOf('.');
                    if (lastDot == -1) {
                        className = className.substring(0, 1).toUpperCase() + className.substring(1);
                    } else {
                        className = className.substring(0, lastDot + 1) + className.substring(lastDot + 1, lastDot + 2).toUpperCase() + className.substring(lastDot + 2);
                    }

                    className = "com.opensymphony.able.action." + className + "Action";

                    // check that the class exists
                    Class clazz;
                    try {
                        clazz = ObjectFactory.getObjectFactory().getClassInstance(className);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }

                    ac.setClassName(className);
                    ac.setPackageName("default");
                    ac.setParams(Collections.EMPTY_MAP);
                    ac.setResults(new ResultMap(className, name));
                    ac.addInterceptor(new InterceptorMapping("checkbox", new CheckboxInterceptor()));
                    ac.addInterceptors(InterceptorBuilder.constructInterceptorReference(webworkDefault,
                            webworkDefault.getDefaultInterceptorRef(),
                            new LinkedHashMap()));
                    ac.addInterceptor(new InterceptorMapping("flash", new FlashInterceptor()));
                    ac.addInterceptor(new InterceptorMapping("partial", new PartialInterceptor()));

                    if (null != clazz.getAnnotation(Secure.class)) {
                        ac.addInterceptor(new InterceptorMapping("secure", new SecurityInterceptor()));
                    }

                    return ac;
                } catch (Exception e) {
                    return null;
                }
            }

            public Map getActionConfigs() {
                return Collections.EMPTY_MAP;
            }
        };
    }

    public void addPackageConfig(String name, PackageConfig packageConfig) {
        if (name.equals("webwork-default")) {
            this.webworkDefault = packageConfig;
        }
    }

    public void destroy() {
    }

    public void reload() throws ConfigurationException {
    }

    public void removePackageConfig(String name) {
    }

    private class ResultMap extends EmptyMap {
        private String className;
        private String name;

        public ResultMap(String className, String name) {
            this.className = className;
            this.name = name;
        }

        public Object get(Object key) {
            String location = null;
            Class resultClass = null;

            if ("login".equals(key)) {
                resultClass = Redirect.class;
                location = "/login!input";
            }

            // check if any annotations are around
            Class clazz = getActionClass();

            while (!clazz.getName().equals(Object.class.getName())) {
                //noinspection unchecked
                Results results = (Results) clazz.getAnnotation(Results.class);
                if (results != null) {
                    // first check here...
                    for (int i = 0; i < results.results().length; i++) {
                        Result result = results.results()[i];
                        if (result.name().equals(key)) {
                            resultClass = result.type();
                            location = result.location();
                            break;
                        }
                    }
                }

                // what about a single Result annotation?
                Result result = (Result) clazz.getAnnotation(Result.class);
                if (result != null) {
                    if (result.name().equals(key)) {
                        resultClass = result.type();
                        location = result.location();
                    }
                }

                if (location != null) {
                    break;
                } else {
                    clazz = clazz.getSuperclass();
                }
            }

            if (location == null && resultClass == null) {
                String fileName = name + "-" + key + ".jsp";
                String path = servletContext.getRealPath("/" + fileName);
                if (path == null || !new File(path).exists()) {
                    fileName = name + ".jsp";
                }

                location = "/" + fileName;
                resultClass = ServletDispatcherResult.class;
            }

            String defaultParam;
            try {
                defaultParam = (String) resultClass.getField("DEFAULT_PARAM").get(null);
            } catch (Exception e) {
                // not sure why this happened, but let's just use a sensible choice
                defaultParam = "location";
            }

            return new ResultConfig((String) key, resultClass,
                    Collections.singletonMap(defaultParam, location));
        }

        private Class getActionClass() {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
