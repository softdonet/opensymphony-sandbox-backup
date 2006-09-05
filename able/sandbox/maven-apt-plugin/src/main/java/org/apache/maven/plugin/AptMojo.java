package org.apache.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;

/**
 * @author <a href="mailto:jubu@volny.cz">Juraj Burian</a>
 * @version $Id:$
 * @goal execute
 * @phase generate-sources
 * @requiresDependencyResolution compile
 * @description generates and/or compiles application sources
 */
public class AptMojo extends AbstractAPTMojo
{

    /**
     * The source directory containing the generated sources.
     * 
     * @parameter default-value="main/generated"
     */
    private String generated;

    /**
     * The source directories containing the sources to be compiled.
     * 
     * @parameter expression="${project.compileSourceRoots}"
     * @required
     * @readonly
     */
    private List compileSourceRoots;

    /**
     * Project classpath.
     * 
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;

    /**
     * The directory for compiled classes.
     * 
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File outputDirectory;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    
    protected String getGenerated()
    {
        return generated;
    }

    protected List getCompileSourceRoots()
    {
        return compileSourceRoots;
    }

    protected List getClasspathElements()
    {
        return classpathElements;
    }

    protected File getOutputDirectory()
    {
        return outputDirectory;
    }

    public void execute() throws MojoExecutionException
    {
        super.execute();

		String genDir = builddir.getAbsolutePath() + FILE_SEPARATOR + getGenerated();
        project.addCompileSourceRoot( genDir );
        Resource resource = new Resource();
        resource.setDirectory( genDir );
        resource.addExclude( "**/*.java" );
        project.addResource( resource );
    }

    /**
     * A list of inclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set includes = new HashSet();

    /**
     * A list of exclusion filters for the compiler.
     * 
     * @parameter
     */
    private Set excludes = new HashSet();

    protected SourceInclusionScanner getSourceInclusionScanner()
    {
        StaleSourceScanner scanner = null;

        if( includes.isEmpty() )
        {
            includes.add( "**/*.java" );
        }
        scanner = new StaleSourceScanner( staleMillis, includes, excludes );
        scanner.addSourceMapping( new SuffixMapping( ".java", ".class" ) );
        return scanner;
    }
}
