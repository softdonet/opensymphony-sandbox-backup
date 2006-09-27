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
public abstract class AbstractScriptService extends Service {
    private String script;

    protected AbstractScriptService(String script) {
        this.script = script;
    }

    private DataSource dataSource;

    public void init() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            runScript("/sql/" + script);
        } catch (Exception e) {
            String msg = "Could not run script: " + script;
            log.severe(msg, e);
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
                log.info(msg);
                throw new RuntimeException(msg);
            }

            log.fine("Automatic schema creation output:\n" + output.toString());
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
            log.severe(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
