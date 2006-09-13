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
import com.opensymphony.able.util.StringHelper;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
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
	private File javaOutputDirectory = new File("target/main/generated");

	private boolean initialised;

	private Log log;

	public static void main(String[] args) {
		ScaffoldingGenerator generator = new ScaffoldingGenerator();
		try {
			generator.generateAll();
		} catch (Exception e) {
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
		generateFiles(entity);
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
		if (log == null) {
			log = new SystemStreamLog();
		}
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	// Implementation methods
	// -------------------------------------------------------------------------
	protected void generateFiles(EntityInfo entity) throws MojoExecutionException {
		String packageName = entity.getEntityClass().getPackage().getName();
		if (packageName.endsWith(".model")) {
			packageName = packageName.substring(0, packageName.length() - ".model".length());
		}
		packageName += ".action";

		File packageDir = new File(javaOutputDirectory.getAbsolutePath() + "/" + packageName.replace('.', '/'));
		packageDir.mkdirs();

		File views = new File(outputDirectory, "views/entity");
		File tags = new File(outputDirectory, "WEB-INF/tags/entity");
		File dir = new File(views, entity.getEntityUri());
		dir.mkdirs();

		generateCollectionControllers(packageDir, entity, packageName, dir);

		generateFile(dir, "index.jsp", "views/Index.vm", createVelocityContext(entity));
		generateFile(dir, "edit.jsp", "views/Edit.vm", createVelocityContext(entity));
		generateFile(dir, "view.jsp", "views/View.vm", createVelocityContext(entity));
		generateFile(dir, "editTable.jsp", "views/EditTable.vm", createVelocityContext(entity));

		dir = new File(tags, entity.getEntityUri());
		dir.mkdirs();

		generateFile(dir, "viewField.tag", "tags/EntityViewField.vm", createVelocityContext(entity));

		List<PropertyInfo> properties = entity.getProperties();
		for (PropertyInfo propertyInfo : properties) {
			generateFile(dir, propertyInfo.getName() + "EditField.tag", "tags/FieldEdit.vm", createVelocityContext(entity,
					propertyInfo));
			generateFile(dir, propertyInfo.getName() + "ViewField.tag", "tags/FieldView.vm", createVelocityContext(entity,
					propertyInfo));
		}
	}

	protected void generateCollectionControllers(File packageDir, EntityInfo entity, String packageName, File viewDir)
			throws MojoExecutionException {
		List<PropertyInfo> properties = entity.getProperties();
		for (PropertyInfo info : properties) {
			if (info.isCollection() && info.isPersistent()) {
				String actionName = entity.getEntityName() + StringHelper.capitalize(info.getName());
				String actionUri = "/" + actionName + ".action";
				String controllerClassName = actionName + "ActionBean";
				VelocityContext context = createVelocityContext(entity);
				context.put("packageName", packageName);
				context.put("controllerClassName", controllerClassName);
				context.put("actionUri", actionUri);
				context.put("propertyInfo", info);
				context.put("propertyName", info.getName());
				context.put("propertyTypeSimpleName", info.getPropertyEntityInfo().getEntityName());
				context.put("propertyTypeQualifiedName", info.getPropertyComponentType().getName());
				
				generateFile(packageDir, controllerClassName + ".java", "controller/EmbeddedCollectionActionBean.vm", context);

				// now lets create the views for this embedded collection
				File dir = new File(viewDir, info.getName());
				dir.mkdir();

				generateFile(dir, "edit.jsp", "views/EmbeddedCollection.vm", context);
			}
		}
	}

	protected void generateFile(File dir, String outputName, String script, VelocityContext context)
			throws MojoExecutionException {
		Writer out = null;
		File file = new File(dir, outputName);
		try {
			Template template = Velocity.getTemplate(script);
			getLog().info("Generating file: " + file);
			out = new FileWriter(file);
			template.merge(context, out);
		} catch (Exception e) {
			throw new MojoExecutionException("Failed in script: " + script + " when generating scaffolding: " + file
					+ ". Reason: " + e, e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.warn("Caught exception while closing file: " + e, e);
				}
			}
		}
	}

	protected VelocityContext createVelocityContext(EntityInfo entity) {
		VelocityContext answer = new VelocityContext();
		answer.put("entityName", entity.getEntityName());
		answer.put("entityInfo", entity);
		answer.put("entityClass", entity.getEntityClass());
		answer.put("entityUri", entity.getEntityUri());
		answer.put("license", "/** TODO license goes here */");
		return answer;
	}

	protected VelocityContext createVelocityContext(EntityInfo entity, PropertyInfo propertyInfo) {
		VelocityContext answer = createVelocityContext(entity);
		answer.put("propertyInfo", propertyInfo);
		return answer;
	}

}
