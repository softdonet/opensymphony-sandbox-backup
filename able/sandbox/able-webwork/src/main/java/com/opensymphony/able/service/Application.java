package com.opensymphony.able.service;

import com.opensymphony.able.util.ScriptRunner;
import com.opensymphony.able.webwork.AbleConfiguration;
import com.opensymphony.util.ClassLoaderUtil;
import com.opensymphony.xwork.config.ConfigurationManager;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Note: UPGRADES HAVE NO HAPPENED AT THIS POINT
 *
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public class Application implements ServletContextAware {
    private static Logger logger = Logger.getLogger(Application.class.getName());
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    private BasicDataSource dataSource;

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init() {
        ConfigurationManager.setConfiguration(new AbleConfiguration(servletContext));

        String url = dataSource.getUrl();

        try {
            if (url.startsWith("jdbc:hsqldb:mem:able")) {
                Class.forName("org.hsqldb.jdbcDriver");
                runScript("sql/schema/hsql/schema.sql");

                if (url.endsWith("jdbc:hsqldb:mem:able-sample")) {
                    runScript("sql/sample.sql");
                }
            }
        } catch (Exception e) {
            String msg = "Could not initialize Able";
            logger.info(msg);
            logger.throwing(Application.class.getName(), "init", e);
            throw new RuntimeException(msg, e);
        }
    }

    private void runScript(String script) throws SQLException, IOException {
        Connection conn = dataSource.getConnection();
        try {
            StringWriter errors = new StringWriter();
            StringWriter output = new StringWriter();

            ScriptRunner runner = new ScriptRunner();
            runner.setErrorLogWriter(new PrintWriter(errors));
            runner.setLogWriter(new PrintWriter(output));
            runner.runScript(conn, new InputStreamReader(ClassLoaderUtil.getResourceAsStream(script, getClass())));

            if (errors.toString().length() != 0) {
                logger.info("There were errors initializing the in-memory database schema:\n" + errors.toString());
            }

            logger.fine("Automatic schema creation output:\n" + output.toString());
        } finally {
            conn.close();
        }
    }

    public void dispose() {
        String url = dataSource.getUrl();

        try {
            if (url.startsWith("jdbc:hsqldb:mem:able")) {
                Class.forName("org.hsqldb.jdbcDriver");
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("SHUTDOWN IMMEDIATELY");
                ps.executeUpdate();
            }
        } catch (Exception e) {
            String msg = "Could not dispose Able";
            logger.info(msg);
            logger.throwing(Application.class.getName(), "dispose", e);
            throw new RuntimeException(msg, e);

        }
    }
}
