/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.apt;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.util.SimpleDeclarationVisitor;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.persistence.Entity;

import java.io.PrintWriter;
import java.util.Properties;

/**
 * 
 * @version $Revision$
 */
public class VelocityClassVisitor extends SimpleDeclarationVisitor {
    private final AnnotationProcessorEnvironment env;

    private String templateName = "Action.vm";
    private String packagePostfix = ".action";
    private String classPostfix = "Action";
    private String primaryKeyType = "Long";

    private String packageName;
    private String className;
    private String qualifiedName;

    public VelocityClassVisitor(final AnnotationProcessorEnvironment env) {
        this.env = env;
    }

    public void visitClassDeclaration(ClassDeclaration declaration) {
        if (matchesDeclaration(declaration)) {
            createClassProperties(declaration);
            PrintWriter file = null;
            try {
                Properties p = new Properties();
                p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
                Velocity.init(p);

                VelocityContext context = createVelocityContext(declaration);
                // VelocityEngine engine = new VelocityEngine();
                file = env.getFiler().createSourceFile(qualifiedName);
                // engine.evaluate(context, arg1, className, arg3)
                Template template = Velocity.getTemplate(templateName);
                template.merge(context, file);
                /*
                 * file.println("package " + packageName + ";");
                 * file.println("public class " + className + " { }");
                 */
                file.close();
            }
            catch (Exception e) {
                System.err.println(e);
                e.printStackTrace();
            }
            finally {
                if (file != null) {
                    file.close();
                }
            }
        }
    }

    protected void createClassProperties(ClassDeclaration declaration) {
        packageName = declaration.getPackage().getQualifiedName();
        if (packageName.endsWith(".model")) {
            packageName = packageName.substring(0, packageName.length() - ".model".length());
        }
        packageName += packagePostfix;
        className = declaration.getSimpleName() + classPostfix;
        qualifiedName = packageName + "." + className;
    }

    protected VelocityContext createVelocityContext(ClassDeclaration declaration) {
        VelocityContext answer = new VelocityContext();
        answer.put("declaration", declaration);
        answer.put("packageName", packageName);
        answer.put("license", "/** TODO license goes here */");
        answer.put("className", className);
        answer.put("qualifiedName", qualifiedName);

        // TODO we need to figure out what the primary key type is by reflection
        answer.put("primaryKeyType", primaryKeyType);
        return answer;
    }

    protected boolean matchesDeclaration(ClassDeclaration declaration) {
        Entity annotation = declaration.getAnnotation(Entity.class);
        return annotation != null;
    }
}
