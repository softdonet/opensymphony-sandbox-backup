package com.opensymphony.able.service;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapException;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Patrick Lightbody (plightbo at gmail dot com)
 */
public class Dao<O> {
    protected SqlMapClient sqlMapClient;

    private Class clazz;
    private String vendor;

    public Dao(Class<O> clazz, SqlMapClient sqlMapClient) {
        this.clazz = clazz;
        this.sqlMapClient = sqlMapClient;
        TransactionAwareDataSourceProxy ds = ((TransactionAwareDataSourceProxy) sqlMapClient.getDataSource());
        String url = ((BasicDataSource) ds.getTargetDataSource()).getUrl();
        if (url.startsWith("jdbc:hsqldb:")) {
            vendor = "hsql";
        } else {
            vendor = "postgres";
        }
    }

    public Object insert(String query, Object... args) {
        QueryPair qp = buildQueryPair("insert", query, args);
        try {
            try {
                return sqlMapClient.insert(qp.id, qp.o);
            } catch (SqlMapException e) {
                return sqlMapClient.insert(qp.id + "-" + vendor, qp.o);
            }
        } catch (SQLException e) {
            handleException("insert", qp, e);
            return null;
        }
    }

    public Object insert(O obj) {
        return insert("", obj);
    }

    public int update(String query, Object... args) {
        QueryPair qp = buildQueryPair("update", query, args);
        try {
            try {
                return sqlMapClient.update(qp.id, qp.o);
            } catch (SqlMapException e) {
                return sqlMapClient.update(qp.id + "-" + vendor, qp.o);
            }
        } catch (SQLException e) {
            handleException("update", qp, e);
            return -1;
        }
    }

    public int update(O obj) {
        return update("", obj);
    }

    public int delete(String query, Object... args) {
        QueryPair qp = buildQueryPair("delete", query, args);
        try {
            try {
                return sqlMapClient.delete(qp.id, qp.o);
            } catch (SqlMapException e) {
                return sqlMapClient.delete(qp.id + "-" + vendor, qp.o);
            }
        } catch (SQLException e) {
            handleException("delete", qp, e);
            return 0;
        }
    }

    public O selectSingle(String query, Object... args) {
        QueryPair qp = buildQueryPair("select", query, args);
        try {
            //noinspection unchecked
            O o;
            try {
                o = (O) sqlMapClient.queryForObject(qp.id, qp.o);
            } catch (SqlMapException e) {
                o = (O) sqlMapClient.queryForObject(qp.id + "-" + vendor, qp.o);
            }

            return o;
        } catch (SQLException e) {
            handleException("selectSingle", qp, e);
            return null;
        }
    }

    public Object selectValue(String query, Object... args) {
        QueryPair qp = buildQueryPair("select", query, args);
        try {
            //noinspection unchecked
            Object o;
            try {
                o = sqlMapClient.queryForObject(qp.id, qp.o);
            } catch (SqlMapException e) {
                o = sqlMapClient.queryForObject(qp.id + "-" + vendor, qp.o);
            }

            return o;
        } catch (SQLException e) {
            handleException("selectValue", qp, e);
            return null;
        }
    }

    public List<O> select(String query, Object... args) {
        QueryPair qp = buildQueryPair("select", query, args);
        try {
            //noinspection unchecked
            List<O> list;
            try {
                list = (List<O>) sqlMapClient.queryForList(qp.id, qp.o);
            } catch (SqlMapException e) {
                list = (List<O>) sqlMapClient.queryForList(qp.id + "-" + vendor, qp.o);
            }

            return list;
        } catch (SQLException e) {
            handleException("select", qp, e);
            //noinspection unchecked
            return Collections.EMPTY_LIST;
        }
    }

    private QueryPair buildQueryPair(String type, String query, Object... args) {
        String id = getIdByClass(clazz);
        if (!query.equals("")) {
            query = query.substring(0, 1).toUpperCase() + query.substring(1);
        }

        Object o;
        if (args.length == 0) {
            o = null;
        } else if (args.length == 1) {
            o = args[0];
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            // break up the args in to a Map, using the query as a basis
            String fields = query;
            if (fields.contains("By")) {
                int index = fields.indexOf("By");
                fields = fields.substring(index + 2);
            }
            String[] parts = fields.split("(By|And|Or)");

            if (parts.length == args.length) {
                for (int i = 0; i < parts.length; i++) {
                    String part = parts[i];
                    part = part.substring(0, 1).toLowerCase() + part.substring(1);
                    map.put(part, args[i]);
                }
            } else {
                
            }


            o = map;
        }
        id = id + "." + type + query;

        return new QueryPair(id, o);
    }

    private String getIdByClass(Class clazz) {
        int dot = clazz.getName().lastIndexOf(".");
        return clazz.getName().substring(dot + 1);
    }

    private void handleException(String method, Dao<O>.QueryPair qp, SQLException e) {
        throw new DaoException(method, qp, e);
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    class QueryPair {
        String id;
        Object o;

        public QueryPair(String id, Object o) {
            this.id = id;
            this.o = o;
        }
    }
}
