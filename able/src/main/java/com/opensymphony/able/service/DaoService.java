package com.opensymphony.able.service;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author <a href="mailto:plightbo@gmail.com">Patrick Lightbody</a>
 */
public class DaoService<T> extends Service {
    protected SqlMapClient sqlMapClient;
    protected Dao<T> dao;
    protected Class<T> modelClass;

    public DaoService(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public void init() throws ServiceException {
        dao = new Dao<T>(modelClass, sqlMapClient);
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }
}
