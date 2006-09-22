package com.opensymphony.able.upgrade;

import org.springframework.beans.factory.BeanFactory;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import com.opensymphony.able.util.Log;
import com.opensymphony.able.util.ScriptRunner;

public abstract class DatabaseUpgradeTask implements UpgradeTask {
    private Log log = new Log();
    private DataSource dataSource;
    private BeanFactory beanFactory;
    private long build;

    protected boolean runScript(String script) throws UpgradeException {
        String vendor;
        try {
            vendor = "hsql"; // todo: figure out how to determine the vendor
        } catch (Exception e) {
            throw new UpgradeException("Could not determine DB vendor", e);
        }

        String loc = "/sql/upgrade/" + build + "_" + script + "_" + vendor + ".sql";
        Reader scriptReader;
        InputStream str = getClass().getResourceAsStream(loc);
        if (str == null) {
            log.fine("No upgrade script " + loc + " found, ending upgrade path");
            return false;
        }

        scriptReader = new InputStreamReader(str);

        try {
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            try {
                StringWriter errors = new StringWriter();
                StringWriter output = new StringWriter();

                ScriptRunner runner = new ScriptRunner();
                runner.setErrorLogWriter(new PrintWriter(errors));
                runner.setLogWriter(new PrintWriter(output));
                runner.runScript(conn, scriptReader);

                if (errors.toString().length() != 0) {
                    conn.rollback();
                    throw new UpgradeException("There were errors running the script:\n" + errors.toString());
                }

                conn.commit();
                log.fine("Script output:\n" + output.toString());
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            throw new UpgradeException("Error running script " + loc, e);
        } catch (IOException e) {
            throw new UpgradeException("Error reading script " + loc, e);
        }

        return true;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected DataSource getDataSource() {
        return this.dataSource;
    }

    public void setBeanFactory(BeanFactory factory) {
        this.beanFactory = factory;
    }

    protected BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public void setBuildNumber(long build) {
        this.build = build;
    }
}
