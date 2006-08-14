package com.opensymphony.able.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class Log {
    private Logger logger;

    public Log(Class clazz) {
        logger = Logger.getLogger(clazz.getName());
    }

    public void severe(String msg, Exception e) {
        log(Level.SEVERE, msg, e);
    }

    public void severe(String msg, String... args) {
        log(Level.SEVERE, msg, args);
    }

    public void severe(String msg, Exception e, String... args) {
        log(Level.SEVERE, msg, e, args);
    }

    public void warn(String msg, Exception e) {
        log(Level.WARNING, msg, e);
    }

    public void warn(String msg, String... args) {
        log(Level.WARNING, msg, args);
    }

    public void warn(String msg, Exception e, String... args) {
        log(Level.WARNING, msg, e, args);
    }

    public void info(String msg, Exception e) {
        log(Level.INFO, msg, e);
    }

    public void info(String msg, String... args) {
        log(Level.INFO, msg, args);
    }

    public void info(String msg, Exception e, String... args) {
        log(Level.INFO, msg, e, args);
    }

    public void fine(String msg, Exception e) {
        log(Level.FINE, msg, e);
    }

    public void fine(String msg, String... args) {
        log(Level.FINE, msg, args);
    }

    public void fine(String msg, Exception e, String... args) {
        log(Level.FINE, msg, e, args);
    }

    private void log(Level level, String msg, Exception e) {
        logger.log(level, msg, e);
    }

    private void log(Level level, String msg, String... args) {
        logger.log(level, msg, args);
    }

    private void log(Level level, String msg, Exception e, String... args) {
        LogRecord lr = new LogRecord(level, msg);
        lr.setThrown(e);
        lr.setParameters(args);
        logger.log(lr);
    }
}
