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

import com.opensymphony.able.entity.EntityInfo;
import com.opensymphony.able.entity.PropertyInfo;
import com.opensymphony.able.jaxb.JaxbResolution;
import com.opensymphony.able.jaxb.JaxbTemplate;
import com.opensymphony.able.util.EnumHelper;
import com.opensymphony.able.validation.hibernate.HibernateValidator;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for any CRUD based JPA {@link ActionBean} to view or edit an
 * entity by ID
 * 
 * @version $Revision$
 */
public abstract class JpaCrudActionSupport<E> extends JpaActionSupport implements CrudActionBean {
    private static final Log log = LogFactory.getLog(JpaCrudActionSupport.class);

    private List idList = new ArrayList();
    private List<E> entities;
    private List<E> bulkEditEntities;
    private E entity;
    private Class<E> entityClass;
    private EntityInfo entityInfo;
    private UriStrategy uriStrategy = new UriStrategy();
    private int bulkEditCount = 5;
    private Validator validator = new HibernateValidator();
    private String query;
    @SpringBean
    private QueryStrategy queryStrategy;

    public JpaCrudActionSupport() {
        init();
    }

    protected JpaCrudActionSupport(JpaTemplate jpaTemplate) {
        super(jpaTemplate);
        init();
    }

    protected JpaCrudActionSupport(JpaTemplate jpaTemplate, QueryStrategy queryStrategy) {
        this(jpaTemplate);
        this.queryStrategy = queryStrategy;
    }

    private void init() {
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

    @DontValidate
    public Resolution delete() {
        List<E> list = getEntities();
        for (E e : list) {
            Object idValue = entityInfo.getIdValue(e);
            if (idValue != null) {
                getJpaTemplate().remove(e);
                shouldCommit();
            }
        }
        return new RedirectResolution(entityInfo.getListUri());
    }

    public Resolution save() {
        validate();
        if (getContext().getValidationErrors().isEmpty()) {
            if (bulkEditEntities != null) {
                for (E e : bulkEditEntities) {
                    save(e);
                }
            }
            else {
                save(getEntity());
            }
            shouldCommit();
            return new RedirectResolution(entityInfo.getListUri());
        }
        return getContext().getSourcePageResolution();
    }

    public Resolution search() {
        return getContext().getSourcePageResolution();
    }

    @DontValidate
    public Resolution cancel() {
        shouldRollback();

        // TODO
        // getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

        return new RedirectResolution(entityInfo.getListUri());
    }

    public Resolution xmlView() {
        return new JaxbResolution(new JaxbTemplate(entityClass), getEntities());
    }

    // Properties
    // -------------------------------------------------------------------------
    public E getEntity() {
        if (entity == null) {
            List<E> list = getEntities();
            if (list.isEmpty()) {
                entity = newInstance();
                list.add(entity);
            }
            else {
                // TODO if size > 1 should we warn?
                entity = list.get(0);
            }
        }
        return entity;
    }

    public List<E> getEntities() {
        if (entities == null) {
            int size = idList.size();
            entities = new ArrayList<E>(size);

            // TODO we could do a more efficient query?
            int i = 0;
            for (Object id : idList) {
                E entity = findByPrimaryKey(id);
                if (entity != null) {
                    customizeEntityRelationships(entity, i++);
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    public List<E> getBulkEditEntities() {
        if (bulkEditEntities == null) {
            List<E> list = getEntities();
            int size = list.size();
            if (size == bulkEditCount) {
                bulkEditEntities = list;
            }
            else if (size > bulkEditCount) {
                bulkEditEntities = list.subList(0, bulkEditCount - 1);
            }
            else {
                bulkEditEntities = new ArrayList<E>(bulkEditCount);
                bulkEditEntities.addAll(list);
                while (bulkEditEntities.size() < bulkEditCount) {
                    bulkEditEntities.add(newInstance());
                }
            }
        }
        return bulkEditEntities;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Class getIdClass() {
        return entityInfo.getIdClass();
    }

    @SuppressWarnings("unchecked")
    public List<E> getAllEntities() {
        if (query != null) {
            if (queryStrategy == null) {
                throw new IllegalArgumentException("No QueryStrategy was injected!");
            }
            List answer = queryStrategy.execute(entityClass, query);
            log.info("Query with: " + query + " found: " + answer);
            return answer;
        }
        return query(getAllElementsQuery());
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public int getBulkEditCount() {
        return bulkEditCount;
    }

    public void setBulkEditCount(int bulkEditCount) {
        this.bulkEditCount = bulkEditCount;
    }

    public Map<String, Object> getAllValues() {
        return new AbstractMap<String, Object>() {

            @Override
            public Object get(Object propertyName) {
                return getAllValuesForProperty((String) propertyName);
            }

            @Override
            public Set<java.util.Map.Entry<String, Object>> entrySet() {
                // TODO Auto-generated method stub
                return Collections.EMPTY_SET;
            }
        };
    }


    /**
     * @return the text query being used
     */
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryStrategy getQueryStrategy() {
        return queryStrategy;
    }


    // Implementation methods
    // -------------------------------------------------------------------------
    protected void save(E e) {
        Object idValue = entityInfo.getIdValue(e);
        if (idValue == null) {
            getJpaTemplate().persist(e);
            idValue = entityInfo.getIdValue(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected void preBind() {
        String[] idValues = uriStrategy.getEntityPrimaryKeyValues(this);
        idList.clear();
        if (idValues != null) {
            for (String value : idValues) {
                Object id = entityInfo.convertToPrimaryKeyValkue(value);
                if (id != null) {
                    idList.add(id);
                }
            }
        }
    }

    protected void validate() {
        if (bulkEditEntities != null) {
            int i = 0;
            for (E e : bulkEditEntities) {
                validator.validate(getContext(), "entity[" + i++ + "]", e);
            }
        }
        else {
            validator.validate(getContext(), "entity.", getEntity());
        }
    }

    /**
     * Looks up the entity by primary key
     */
    protected E findByPrimaryKey(Object id) {
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

    protected Object getAllValuesForProperty(String propertyName) {
        PropertyInfo property = entityInfo.getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException("Entity " + entityInfo.getEntityName() + " does not have a property called: " + propertyName);
        }
        EntityInfo propertyTypeInfo = property.getPropertyEntityInfo();
        if (propertyTypeInfo.isPersistent()) {
            return getJpaTemplate().find(propertyTypeInfo.getFindAllQuery());
        }
        else {
            Class propertyType = propertyTypeInfo.getEntityClass();
            if (Enum.class.isAssignableFrom(propertyType)) {
                try {
                    return Arrays.asList(EnumHelper.getEnumValues(propertyType));
                }
                catch (Exception e) {
                    log.error("Attempting to find all enum types of : " + propertyType + ". Caught: " + e, e);
                }
            }
        }
        return null;
    }

    /**
     * Allows the query to be overloaded for returning all of the items
     */
    protected String getAllElementsQuery() {
        return entityInfo.getFindAllQuery();
    }

    /**
     * Configures any relationships from the request parameters
     */
    protected void customizeEntityRelationships(E entity, int index) {
        HttpServletRequest request = getContext().getRequest();
        List<PropertyInfo> properties = entityInfo.getProperties();
        for (PropertyInfo info : properties) {
            if (info.isCollection()) {
                continue;
            }
            EntityInfo propertyType = info.getPropertyEntityInfo();
            if (propertyType.isPersistent()) {
                String name = "entity." + info.getName() + ".id";
                String value = request.getParameter(name);
                if (value != null) {
                    bindProperty(entity, info, propertyType, index, value);
                }
                else {
                    name = "bulkEditEntities[" + index + "]." + info.getName() + ".id";
                    value = request.getParameter(name);
                    if (value != null) {
                        bindProperty(entity, info, propertyType, index, value);
                    }
                }
            }
        }
    }

    /**
     * Binds the related entity to the entity using a string primary key
     * parameter
     */
    @SuppressWarnings("unchecked")
    protected void bindProperty(E entity, PropertyInfo property, EntityInfo propertyType, int index, String value) {
        Object pk = propertyType.convertToPrimaryKeyValkue(value);
        Object relatedEntity = getJpaTemplate().find(propertyType.getEntityClass(), pk);

        property.setValue(entity, relatedEntity);
    }

}
