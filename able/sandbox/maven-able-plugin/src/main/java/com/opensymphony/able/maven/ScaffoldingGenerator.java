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
package com.opensymphony.able.maven;

import com.opensymphony.able.entity.Entities;
import com.opensymphony.able.entity.EntityInfo;
import com.opensymphony.able.entity.PropertyInfo;

import org.apache.maven.plugin.logging.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 
 * @version $Revision$
 */
public class ScaffoldingGenerator {
    private Entities entities = Entities.getInstance();

    private File outputDirectory = new File("target/scaffolding");

    private boolean initialised;

    private Log log;

    public static void main(String[] args) {
        ScaffoldingGenerator generator = new ScaffoldingGenerator();
        try {
            generator.generateAll();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public void generateAll() throws Exception {
        Set<Entry<String, EntityInfo>> entrySet = entities.getEntityMap().entrySet();
        if (entrySet.isEmpty()) {
            getLog().warn("No entities found!");
            return;
        }
        for (Entry<String, EntityInfo> entry : entrySet) {
            generate(entry.getKey(), entry.getValue());
        }
    }
    
    public void generate(String alias) throws Exception {
        EntityInfo entity = Entities.getInstance().getEntity(alias);
        if (entity == null) {
            throw new IllegalArgumentException("Could not find entity with alias '" + alias + "'");
        }
        generate(alias, entity);
    }

    public void generate(String alias, EntityInfo entity) throws Exception {
        if (!initialised) {
            Properties p = new Properties();
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Velocity.init(p);
            initialised = true;
        }
        VelocityContext context = createVelocityContext(alias, entity);
        generateFiles(entity, context);
    }

    // Properties
    // -------------------------------------------------------------------------
    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void generateFiles(EntityInfo entity, VelocityContext context) throws Exception {
        File views = new File(outputDirectory, "views/entity");
        File tags = new File(outputDirectory, "tags/entity");
        File dir = new File(views, entity.getEntityUri());
        dir.mkdirs();

        generateFile(dir, "index.jsp", "views/Index.vm", context);
        generateFile(dir, "edit.jsp", "views/Edit.vm", context);
        generateFile(dir, "view.jsp", "views/View.vm", context);
        generateFile(dir, "editTable.jsp", "views/EditTable.vm", context);

        dir = new File(tags, entity.getEntityUri());
        dir.mkdirs();

        List<PropertyInfo> properties = entity.getProperties();
        for (PropertyInfo propertyInfo : properties) {
            context.put("propertyInfo", propertyInfo);

            generateFile(dir, propertyInfo.getName() + "EditField.tag", "tags/FieldEdit.vm", context);
            generateFile(dir, propertyInfo.getName() + "ViewField.tag", "tags/FieldView.vm", context);
        }
    }

    protected void generateFile(File dir, String outputName, String script, VelocityContext context) throws Exception {
        Writer out = null;
        try {
            Template template = Velocity.getTemplate(script);
            File file = new File(dir, outputName);
            getLog().info("Generating file: " + file);
            out = new FileWriter(file);
            template.merge(context, out);
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected VelocityContext createVelocityContext(String alias, EntityInfo entity) {
        VelocityContext answer = new VelocityContext();
        answer.put("entityName", alias);
        answer.put("entityInfo", entity);
        answer.put("entityClass", entity.getEntityClass());
        answer.put("entityUri", entity.getEntityUri());
        answer.put("license", "/** TODO license goes here */");
        return answer;
    }

}
