/*
 * Copyright 2005-2006 The Apache Software Foundation.
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
package com.opensymphony.able.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for Able Maven plugin mojos
 * 
 * @version $Id$
 * @description Able Maven Plugin
 * @requiresDependencyResolution compile
 */
public abstract class AbleMojoSupport extends AbstractMojo {

	/**
	 * The path where the generated artifacts will be placed.
	 * 
	 * @parameter expression="${basedir}/src/main/webapp"
	 */
	private File outputDirectory;

    /**
     * Project classpath.
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;
    
    private ScaffoldingGenerator generator;
    
	/**
	 * This method will run the mojo
	 */
    public void execute() throws MojoExecutionException {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader newLoader = createClassLoader(oldClassLoader);
            Thread.currentThread().setContextClassLoader(newLoader);
            generate(getScaffoldingGenerator());
        }
        catch (MojoExecutionException e) {
            throw e;
        }
        catch (Exception e) {
            throw new MojoExecutionException("Failed to generate scaffolding: " + e, e);
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }


    /**
     * Performs scaffolding generation
     */
	protected abstract void generate(ScaffoldingGenerator scaffoldingGenerator) throws Exception;


    /**
	 * Getter for property output directory.
	 * 
	 * @return The value of output directory.
	 */
	public File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Setter for the output directory.
	 * 
	 * @param inOutputDirectory
	 *            The value of output directory.
	 */
	public void setOutputDirectory(final File inOutputDirectory) {
		this.outputDirectory = inOutputDirectory;
	}

	/**
	 * This method creates a String representation of this object
	 * 
	 * @return the String representation of this object
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("AbleBase[");
		buffer.append("outputDirectory = ").append(outputDirectory);
		buffer.append("]");
		return buffer.toString();
	}
    
    public ScaffoldingGenerator getScaffoldingGenerator() {
        if (generator == null) {
            generator = new ScaffoldingGenerator();
            generator.setOutputDirectory(outputDirectory);
            generator.setLog(getLog());

        }
        return generator;
    }
    

    protected ClassLoader createClassLoader(ClassLoader parent) throws MalformedURLException {
        int size = classpathElements.size();
        URL[] urls = new URL[size];
        for (int i=0; i< size; i++) {
            String name = (String) classpathElements.get(i);
            File file = new File(name);
            urls[i] = file.toURL();
        }
        return new URLClassLoader(urls, parent);
    }
    

}
