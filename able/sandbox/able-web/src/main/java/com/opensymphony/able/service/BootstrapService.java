package com.opensymphony.able.service;

import com.opensymphony.able.util.ScriptRunner;

import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public class BootstrapService {
    private static Logger logger = Logger.getLogger(BootstrapService.class.getName());

    private boolean seed;
    private DataSource dataSource;

    public void init() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            runScript("/sql/schema_hsql.sql");

            if (seed) {
                logger.info("Seeding sample data...");

                runScript("/sql/sample_hsql.sql");

                logger.info("... done!");
            }
        } catch (Exception e) {
            String msg = "Could not initialize Able";
            logger.info(msg);
            logger.throwing(BootstrapService.class.getName(), "init", e);
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
            runner.runScript(conn, new InputStreamReader(this.getClass().getResourceAsStream(script)));

            if (errors.toString().length() != 0) {
                String msg = "There were errors initializing the in-memory database schema:\n" + errors.toString();
                logger.info(msg);
                throw new RuntimeException(msg);
            }

            logger.fine("Automatic schema creation output:\n" + output.toString());
        } finally {
            conn.close();
        }
    }

    public void dispose() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SHUTDOWN IMMEDIATELY");
            ps.executeUpdate();
        } catch (Exception e) {
            String msg = "Could not dispose Able";
            logger.info(msg);
            logger.throwing(BootstrapService.class.getName(), "dispose", e);
            throw new RuntimeException(msg, e);
        }
    }

    public void setSeed(boolean seed) {
        this.seed = seed;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
