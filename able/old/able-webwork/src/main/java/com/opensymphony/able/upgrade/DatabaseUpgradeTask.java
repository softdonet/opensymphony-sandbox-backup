package com.opensymphony.able.upgrade;

import com.opensymphony.able.util.ScriptRunner;
import com.opensymphony.able.util.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;

import com.opensymphony.util.ClassLoaderUtil;
import org.apache.commons.dbcp.BasicDataSource;

public abstract class DatabaseUpgradeTask implements UpgradeTask {
    private Log log = new Log(getClass());
    private BasicDataSource dataSource;
    private long build;

    protected void runScript(String script) throws UpgradeException {
        String loc = "com/opensymphony/able/upgrade/" + build + "_" + script;
        try {
            Connection conn = dataSource.getConnection();
            try {
                StringWriter errors = new StringWriter();
                StringWriter output = new StringWriter();

                ScriptRunner runner = new ScriptRunner();
                runner.setErrorLogWriter(new PrintWriter(errors));
                runner.setLogWriter(new PrintWriter(output));
                runner.runScript(conn, new InputStreamReader(ClassLoaderUtil.getResourceAsStream(loc, getClass())));

                if (errors.toString().length() != 0) {
                    log.info("There were errors running the script:\n" + errors.toString());
                }

                log.fine("Script output:\n" + output.toString());
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            throw new UpgradeException("Error running script " + loc, e);
        } catch (IOException e) {
            throw new UpgradeException("Error reading script " + loc, e);
        }
    }

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setBuildNumber(long build) {
        this.build = build;
    }
}
