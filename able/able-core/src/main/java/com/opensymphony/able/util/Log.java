package com.opensymphony.able.util;

import java.util.logging.*;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class Log {
    static {
        try {
            Handler[] handlers = java.util.logging.Logger.getLogger("").getHandlers();
            boolean foundConsoleHandler = false;

            ConsoleHandler consoleHandler = null;
            for (Handler handler : handlers) {
                // since we have a handler, lets go with whatever level it already has
                if (handler instanceof ConsoleHandler) {
                    consoleHandler = (ConsoleHandler) handler;
                    handler.setFormatter(new TerseFormatter());
                    foundConsoleHandler = true;
                }
            }
            if (!foundConsoleHandler) {
                // no console handler found
                System.err.println("No consoleHandler found, adding one.");
                consoleHandler = new ConsoleHandler();
                //by default, we only log warnings and up
                consoleHandler.setLevel(Level.FINE);
                consoleHandler.setFormatter(new TerseFormatter());
                java.util.logging.Logger.getLogger("").addHandler(consoleHandler);
            } else {
                consoleHandler.setLevel(Level.FINE);
            }
        }
        catch (Throwable t) {
            System.err.println("Unexpected Error setting up logging");
            t.printStackTrace();
        }
    }

    private Logger logger;
    private String className;


    public Log() {
        Exception e = new Exception();
        className = e.getStackTrace()[1].getClassName();
        logger = Logger.getLogger(className);
    }

    public Log(Class clazz) {
        className = clazz.getName();
        logger = Logger.getLogger(className);
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
        lr.setSourceMethodName("");
        lr.setSourceClassName(className);
        logger.log(lr);
    }
}
