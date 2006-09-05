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
public abstract class JpaCrudActionSupport<K, E> extends JpaActionSupport {
    private static final Log log = LogFactory.getLog(JpaCrudActionSupport.class);
    
    private K id;
    private E entity;
    private Class<K> idClass;
    private Class<E> entityClass;
    private String entityName;
    private String entityUri;
    private UriStrategy uriStrategy = new UriStrategy();
    private TypeConverter typeConverter = new BeanWrapperImpl();

    public JpaCrudActionSupport() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = genericSuperclass.getActualTypeArguments();
        this.idClass = (Class<K>) typeArguments[0];
        this.entityClass = (Class<E>) typeArguments[1];
    }

    // Actions
    // -------------------------------------------------------------------------
    @DontValidate
    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution(uriStrategy.getViewUri(this));
    }

    public Resolution save() {
        if (getId() != null) {
            getEntityManager().persist(getEntity());
            // TODO set the ID value
        }

        // TODO
        // getContext().addMsg( "saved " + getEntityName(); );

        return new RedirectResolution(uriStrategy.getEditUri(this));
    }

    @DontValidate
    public Resolution cancel() {
        evictBoundObjects();

        // TODO
        // getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

        return new RedirectResolution(uriStrategy.getHomeUri(this));
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

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Class<K> getIdClass() {
        return idClass;
    }

    /**
     * Returns the simple name of the entity
     */
    public String getEntityName() {
        if (entityName == null) {
            entityName = createEntityName();
        }
        return entityName;
    }

    /**
     * Returns the URI name for the entity
     */
    public String getEntityUri() {
        if (entityUri == null) {
            entityUri = createEntityUri();
        }
        return entityUri;
    }

    @SuppressWarnings("unchecked")
    public List<E> getAllEntities() {
        return query("from " + getEntityName());
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    protected void preBind() {
        // TODO lets bind the request parameter for the ID to the id attribute
        String idValue = uriStrategy.getEntityPrimaryKeyString(this);
        if (idValue != null) {
            idValue = idValue.trim();
            if (idValue.length() > 0) {
                log.info("Converting primary key: "+ idValue + " to type: " + idClass.getName());
                K value = (K) typeConverter.convertIfNecessary(idValue, idClass);
                setId(value);
            }
        }
    }

    /**
     * Looks up the entity by primary key
     */
    protected E findByPrimaryKey() {
        return getEntityManager().find(entityClass, id);
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

    protected String createEntityName() {
        String answer = getEntityClass().getName();
        int idx = answer.lastIndexOf('.');
        if (idx >= 0) {
            answer = answer.substring(idx + 1);
        }
        return answer;
    }

    protected String createEntityUri() {
        return Introspector.decapitalize(getEntityName());
    }

}
