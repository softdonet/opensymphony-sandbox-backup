/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.integration.spring.SpringBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

/**
 * Base class for any JPA based {@link ActionBean}
 * 
 * @version $Revision$
 */
public abstract class JpaActionSupport implements ActionBean {

    @PersistenceContext
    @SpringBean
    private EntityManager entityManager;
    private ActionBeanContext context;

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
        preBind();
    }

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            throw new IllegalArgumentException("The JPA EntityManager has not been Injected!");
        }
        return entityManager;
    }

    public List query(String queryText) {
        return getEntityManager().createQuery(queryText).getResultList();
    }

    public List query(String queryText, Object... parameters) {
        Query query = getEntityManager().createQuery(queryText);
        int index = 0;
        for (Object object : parameters) {
            query.setParameter(index++, object);
        }
        return query.getResultList();
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    protected void evictBoundObjects() {
        getEntityManager().getTransaction().setRollbackOnly();
    }

    /**
     * A hook to prebind any persistent entities using the request parameters so
     * that their properties can be bound from request parameters
     */
    protected abstract void preBind();


}
