package org.apache.maven.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.project.MavenProject;

/**
 * @author Juraj Burian
 * @version $Revision:$ by $Author:$
 * 
 * @goal eclipse
 * @requiresDependencyResolution compile
 * @description create ./settings/org.eclipse.jdt.apt.core.prefs and
 *              .factorypath.xml
 */
public class EclipseAptMojo extends AbstractMojo
{
    protected static final String FILE_SEPARATOR = //
    System.getProperty( "file.separator" );

    /**
     * The source directory containing the generated sources.
     * 
     * @parameter default-value="main/generated"
     */
    private String generated;

    /**
     * The directory to run the compiler from if fork is true.
     * 
     * @parameter expression="${basedir}"
     * @required
     * @readonly
     */
    protected File basedir;

    /**
     * -classpath Path Specify where to find user class files. Path is a single
     * argument containing a list of paths to zip files or directories,
     * delimited by the platform-specific path delimiter.
     * 
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List classpathElements;

    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // exclude this :
        if( "pom".endsWith( project.getPackaging() )
                || "ear".endsWith( project.getPackaging() ) )
        {
            return;
        }
        if( !project.getPackaging().contains( "apt" ) )
        {
            return;
        }

        // write prefs file
        File prefs = new File( basedir, ".settings" + FILE_SEPARATOR
                + "org.eclipse.jdt.apt.core.prefs" );
        try
        {
            prefs.getParentFile().mkdirs();
            prefs.createNewFile();
        } catch ( IOException e )
        {
            throw new MojoExecutionException( "Can't create file: "
                    + prefs.getPath() );
        }
        PrintWriter out = null;
        try
        {
            out = new PrintWriter( prefs );
        } catch ( FileNotFoundException e )
        {
            // can't happen
        }
        out.println( "#" + new Date() );
        out.println( "eclipse.preferences.version=1" );
        out.println( "org.eclipse.jdt.apt.aptEnabled=true" );
        out.println( "org.eclipse.jdt.apt.genSrcDir=" + generated );
        out.println( "org.eclipse.jdt.apt.processorOptions=" );
        out.close();

        // write .factorypath
        File factorypath = new File( basedir, ".factorypath" );
        try
        {
            prefs.createNewFile();
        } catch ( IOException e )
        {
            throw new MojoExecutionException( "Can't create file: "
                    + factorypath.getPath() );
        }
        try
        {
            out = new PrintWriter( factorypath );
        } catch ( FileNotFoundException e )
        {
            // can't happen
        }
        out.println( "<factorypath> " );
        for( Iterator it = classpathElements.iterator(); it.hasNext(); )
        {
            String x = (String) it.next();
            if( x.contains( "jar" ) ) {
                out.println( "    <factorypathentry kind=\"EXTJAR\" id=\"" + x
                        + " \" enabled=\"true\" runInBatchMode=\"false\"/>" );
            }
        }
        out.println( "</factorypath> " );
        out.close();
    }
}
