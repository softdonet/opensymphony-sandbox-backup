/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.service;

import com.opensymphony.able.util.Log;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Represents a base class for CRUD based services.
 *
 * @version $Revision$
 */
public abstract class JpaCrudService<E> implements CrudService<E> {
    protected Log log = new Log(getClass());

    @PersistenceContext
    @SpringBean
    private JpaTemplate jpaTemplate;
    private Class<E> entityClass;


    protected JpaCrudService() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = genericSuperclass.getActualTypeArguments();
        this.entityClass = (Class<E>) typeArguments[0];
    }

    protected JpaCrudService(JpaTemplate jpaTemplate) {
        this();
        this.jpaTemplate = jpaTemplate;
    }

    public JpaTemplate getJpaTemplate() {
        if (jpaTemplate == null) {
            throw new IllegalArgumentException("The JpaTemplate has not been Injected!");
        }
        return jpaTemplate;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public E findById(Object id) {
        return getJpaTemplate().find(entityClass, id);
    }

    public E newInstance() throws IllegalAccessException, InstantiationException {
        return entityClass.newInstance();
    }

    public void insert(E entity) {
        getJpaTemplate().persist(entity);
    }

    public void update(E entity) {
    }

    public List<E> findAll() {
        return getJpaTemplate().find("from " + entityClass.getName());
    }

    protected List find(String queryText) {
        return getJpaTemplate().find(queryText);
    }

    protected List find(String queryText, Object... parameters) {
        return getJpaTemplate().find(queryText, parameters);
    }

    protected Object findFirst(String queryText) {
        return first(getJpaTemplate().find(queryText));
    }

    protected Object findFirst(String queryText, Object... parameters) {
        return first(getJpaTemplate().find(queryText, parameters));
    }

    /**
     * Returns the first item of a list or null if the list is empty
     */
    protected Object first(List list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

/*
    protected List findUsingSql(final String sql) {
        return findUsingSql(sql, getEntityClass());
    }
    
    protected List findUsingSql(final String sql) {
        return getJpaTemplate().executeFind(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                return entityManager.createNativeQuery(sql).getResultList();
            }
        });
    }

    protected List findUsingSql(final String sql, final Class returnType) {
        return getJpaTemplate().executeFind(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                return entityManager.createNativeQuery(sql, returnType).getResultList();
            }
        });
    }

    protected List findUsingSql(final String sql, final String resultSetMapping) {
        return getJpaTemplate().executeFind(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                return entityManager.createNativeQuery(sql, resultSetMapping).getResultList();
            }
        });
    }

*/

    /**
     * Queries the current entities using native SQL and optional parameters
     */
    protected List findUsingSql(final String sql, final Object... parameters) {
        return findTypeUsingSql(sql, getEntityClass(), parameters);
    }

    /**
     * Quries entities of the given type using native SQL and optional parameters
     */
    protected List findTypeUsingSql(final String sql, final Class returnType, final Object... parameters) {
        return getJpaTemplate().executeFind(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                System.out.println("About to perform sql: " + sql + " to return type: " + returnType);
                Query query = entityManager.createNativeQuery(sql, returnType);
                int index = 1;
                for (Object parameter : parameters) {
                    System.out.println("Setting param: " + index + " to: " + parameter);
                    query.setParameter(index++, parameter);
                }
                return query.getResultList();
            }
        });
    }

    /**
     * Performs a native SQL query with optional parameters using a named SQL result set mapping
     */
    protected List findUsingSqlResultSetMapping(final String sql, final String resultSetMapping, final Object... parameters) {
        return getJpaTemplate().executeFind(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                Query query = entityManager.createNativeQuery(sql, resultSetMapping);
                int index = 1;
                for (Object parameter : parameters) {
                    System.out.println("Setting param: " + index + " to: " + parameter);
                    query.setParameter(index++, parameter);
                }
                return query.getResultList();
            }
        });
    }

}
