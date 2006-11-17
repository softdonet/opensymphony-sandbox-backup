/*
 * $Id: QuickStart.java 407897 2006-05-19 13:00:43 -0700 (Fri, 19 May 2006) plightbo $
 *
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.webwork.quickstart;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.util.*;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * The QuickStart main program.
 *
 */
public class QuickStart {
    public static void main(String[] args) throws FileNotFoundException, MalformedURLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (args.length != 3 && args.length != 0) {
            System.err.println("QuickStart must be either invoked with three arguments or no arguments:");
            System.err.println("[contextPath] [webapp] [sources]");
            System.err.println("");
            System.err.println("Ex: java -jar struts.jar \\");
            System.err.println("    quickstart /sandbox sandbox/src/webapp sandbox/src/java");
            System.err.println("");
            System.err.println("OR");
            System.err.println("");
            System.err.println("Ex: java -jar struts.jar quickstart");
            System.err.println(" Where a 'quickstart.xml' file exists in your working directory");
            return;
        }

        Configuration c;
        if (args.length == 0) {
            XStream xstream = new XStream(new DomDriver());
            xstream.alias("configuration", Configuration.class);
            xstream.alias("extendsConfig", String.class);
            xstream.alias("port", int.class);
            xstream.alias("context", String.class);
            xstream.alias("dir", String.class);
            xstream.alias("path", String.class);
            xstream.alias("webDir", Mapping.class);
            File config = new File("quickstart.xml");
            if (!config.exists()) {
                // uh oh, time to stop
                System.err.println("Could not find quickstart.xml!");
                System.err.println("Tip: quickstart.xml must exist in your working directory");
                System.err.println("");
                System.err.println("Alternatively, if you your deployment is simple, try launching");
                System.err.println("QuickStart using the simple command line options rather than");
                System.err.println("Relying on quickstart.xml existing");
                return;
            }

            c = (Configuration) xstream.fromXML(new FileReader(config));
            c.resolveDirs(config.getParent());
            c.resolveExtensions(config.getParent(), xstream);
        } else {
            c = new Configuration();
            c.setContext(args[0]);
            c.setPort(new Integer(8080));
            ArrayList webDirs = new ArrayList();
            webDirs.add(new Mapping("/", args[1]));
            c.setWebDirs(webDirs);
            ArrayList sources = new ArrayList();
            sources.add(args[2]);
            c.setSources(sources);
            ArrayList classDirs = new ArrayList();
            classDirs.add(args[1] + "/WEB-INF/classes");
            classDirs.add(args[2]);
            c.setClassDirs(classDirs);
            ArrayList libs = new ArrayList();
            libs.add("lib");
            c.setLibs(libs);

            c.resolveDirs(new File(".").getParent());
        }

        // validate the configuration
        if (c.validate()) {
            return;
        }


        // ok, clean up the classpath crap
        TreeSet finalLibs = new TreeSet();
        for (Object o : c.getLibs()) {
            String s = (String) o;
            finalLibs.add(s);
        }
        TreeSet finalClasses = new TreeSet();
        for (Object o1 : c.getClassDirs()) {
            String s = (String) o1;
            finalClasses.add(s);
        }
        TreeSet finalSources = new TreeSet();
        if (c.getSources() != null) {
            for (Object o : c.getSources()) {
                String s = (String) o;
                finalSources.add(s);
            }
        }

        // explain what is being executed
        System.out.println("Launching Jetty with the following configuration:");
        System.out.println("Jars/Directory of jars:");
        for (Object finalLib : finalLibs) {
            String s = (String) finalLib;
            System.out.println("    " + s);
        }
        System.out.println("Directories of classes:");
        for (Object finalClass : finalClasses) {
            String s = (String) finalClass;
            System.out.println("    " + s);
        }
        if (!finalSources.isEmpty()) {
            System.out.println("Sources:");
            for (Object finalSource : finalSources) {
                String s = (String) finalSource;
                System.out.println("    " + s);
            }
        }
        System.out.println("WebApp directories:");
        for (Object o2 : c.getMappings().entrySet()) {
            Map.Entry entry = (Map.Entry) o2;
            System.out.println(entry.getKey() + "  ->  " + entry.getValue());
        }

        // prepare the classloader
        ClassLoader parent = new MultiDirClassLoader((String[]) finalLibs.toArray(new String[finalLibs.size()]),
                (String[]) finalClasses.toArray(new String[finalClasses.size()]),
                Thread.currentThread().getContextClassLoader());

        URLClassLoader url = new MyURLClassLoader(parent);
        Thread.currentThread().setContextClassLoader(url);

        Class clazz = url.loadClass("com.opensymphony.able.webwork.quickstart.JettyServer");
        Method method = clazz.getDeclaredMethod("startServer",
                int.class, String.class, List.class, Map.class, String.class);
        method.invoke(null, c.port, c.getContext(), c.getPathPriority(), c.getMappings(), c.getResolver());

        System.out.println("");
        System.out.println("********************************************************");
        System.out.println("Quick-started at http://localhost:" + c.getPort() + c.getContext());
        System.out.println("You may now edit your Java classes and web files without");
        System.out.println(" deploying or restarting.");
        System.out.println("********************************************************");
    }

    static class MyURLClassLoader extends URLClassLoader {
        private ClassLoader parent;

        public MyURLClassLoader(ClassLoader parent) {
            super(new URL[0], parent);
            this.parent = parent;
        }

        public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.startsWith("org.xml.") || name.startsWith("org.w3c.")
                    || name.startsWith("java.") || name.startsWith("javax.")
                    || name.startsWith("sun.") || name.startsWith("com.sun.")) {
                return super.loadClass(name, resolve);
            }

            ClassLoader parent = getParent();
            // First, check if the class has already been loaded
            Class c = findLoadedClass(name);
            if (c == null) {
                try {
                    c = findClass(name);
                } catch (Throwable t) {
                    // If still not found, only then ask the parent
                    c = parent.loadClass(name);
                }
            }
            if (resolve) {
                resolveClass(c);
            }

            return c;
        }

        public URL getResource(String name) {
            URL url = findResource(name);
            if (url == null && parent != null) {
                url = parent.getResource(name);
            }

            return url;
        }
    }
}
