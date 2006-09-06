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
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeConverter;

import com.opensymphony.able.entity.EntityInfo;

import java.beans.Introspector;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Base class for any CRUD based JPA {@link ActionBean} to view or edit an
 * entity by ID
 * 
 * @version $Revision$
 */
public abstract class JpaCrudActionSupport<E> extends JpaActionSupport {
    private static final Log log = LogFactory.getLog(JpaCrudActionSupport.class);

    private Object id;
    private E entity;
    private Class<E> entityClass;
    private EntityInfo entityInfo;
    private UriStrategy uriStrategy = new UriStrategy();
    private TypeConverter typeConverter = new BeanWrapperImpl();

    public JpaCrudActionSupport() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = genericSuperclass.getActualTypeArguments();
        this.entityClass = (Class<E>) typeArguments[0];
        this.entityInfo = new EntityInfo(entityClass);
    }

    // Actions
    // -------------------------------------------------------------------------
    @DontValidate
    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(entityInfo.getViewUri());
    }
    
    public Resolution edit() {
        return new ForwardResolution(entityInfo.getEditUri());
    }
    
    public Resolution bulkEdit() {
        return new ForwardResolution(entityInfo.getBulkEditUri());
    }

    public Resolution save() {
        System.out.println("####Êsaving() entity!!!" + getEntity());

        Object idValue = entityInfo.getIdValue(getEntity());
        if (idValue == null) {
            getJpaTemplate().persist(getEntity());
            idValue = entityInfo.getIdValue(getEntity());
        }

        // TODO
        // getContext().addMsg( "saved " + getEntityName(); );

        String uri = entityInfo.getHomeUri();
        return new RedirectResolution(uri);
    }

    @DontValidate
    public Resolution cancel() {
        System.out.println("####Êcancel() entity!!! " + getEntity());
        evictBoundObjects();

        // TODO
        // getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

        return new RedirectResolution(entityInfo.getHomeUri());
    }

    // Properties
    // -------------------------------------------------------------------------
    public E getEntity() {
        if (entity == null) {
            if (id != null) {
                entity = findByPrimaryKey();
            }
            else {
                entity = newInstance();
            }
        }
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        if ("".equals(id)) {
            id = null;
        }
        if (id != null) {
            Class idClass = entityInfo.getIdClass();
            this.id = typeConverter.convertIfNecessary(id, idClass);
        }
        else {
            this.id = id;
        }
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Class getIdClass() {
        return entityInfo.getIdClass();
    }

    @SuppressWarnings("unchecked")
    public List<E> getAllEntities() {
        return query("from " + entityInfo.getEntityName());
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    protected void preBind() {
        // TODO lets bind the request parameter for the ID to the id attribute
        String idValue = uriStrategy.getEntityPrimaryKeyString(this);
        setId(idValue);
    }

    /**
     * Looks up the entity by primary key
     */
    protected E findByPrimaryKey() {
        log.info("Loading primaryKey Value: " + id + " of type: " + id.getClass());
        return getJpaTemplate().find(entityClass, id);
    }

    /**
     * Creates a new instance of the entity class
     */
    protected E newInstance() {
        try {
            return entityClass.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to instantiate class: " + entityClass.getName() + ". Reason: " + e, e);
        }
    }
}
