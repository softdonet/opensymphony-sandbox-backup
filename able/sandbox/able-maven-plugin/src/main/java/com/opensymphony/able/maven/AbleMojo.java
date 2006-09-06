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

/**
 * Mojo for the Able Maven plugin
 * 
 * @version $Id$
 * @description Able Maven Plugin
 */
public class AbleMojo extends AbstractMojo {

	/**
	 * The path where the generated artifacts will be placed.
	 * 
	 * @parameter expression="${basedir}/target/generated-sources"
	 */
	private String outputDirectory;

	/**
	 * This method will run the mojo
	 * 
	 * @throws MojoExecutionException
	 *             Thrown if we fail to obtain an appfuse resource.
	 */
	public void execute() throws MojoExecutionException {
		if (getLog().isInfoEnabled()) {
			getLog().info("Running the Able Mojo");
		}

		throw new MojoExecutionException("Unimplemented Mojo Base");
	}

	/**
	 * Getter for property output directory.
	 * 
	 * @return The value of output directory.
	 */
	public String getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Setter for the output directory.
	 * 
	 * @param inOutputDirectory
	 *            The value of output directory.
	 */
	public void setOutputDirectory(final String inOutputDirectory) {
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
}
