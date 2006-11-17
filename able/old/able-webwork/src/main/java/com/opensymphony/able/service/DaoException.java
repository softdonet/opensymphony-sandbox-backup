package com.opensymphony.able.service;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public class DaoException extends RuntimeException {
    private static Logger logger = Logger.getLogger(DaoException.class.getName());

    private String method;
    private Dao.QueryPair qp;

    public DaoException(String method, Dao.QueryPair qp, SQLException e) {
        super(getMessage(method, qp), e);
        this.method = method;
        this.qp = qp;

        logger.info(getMessage(method, qp));
        logger.throwing(Dao.class.getName(), method, e);
    }

    private static String getMessage(String method, Dao.QueryPair qp) {
        return "Error with DAO operation '" + method + "': " + qp.id + " has failed.";
    }

    public String getMethod() {
        return method;
    }

    public Dao.QueryPair getQp() {
        return qp;
    }
}
