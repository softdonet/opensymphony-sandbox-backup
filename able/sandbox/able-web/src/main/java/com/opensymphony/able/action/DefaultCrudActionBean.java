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
package com.opensymphony.able.action;

import com.opensymphony.able.entity.EntityInfo;
import com.opensymphony.able.entity.PropertyInfo;
import com.opensymphony.able.filter.TransactionServletFilter;
import com.opensymphony.able.jaxb.JaxbResolution;
import com.opensymphony.able.jaxb.JaxbTemplate;
import com.opensymphony.able.service.CrudService;
import com.opensymphony.able.service.JpaCrudService;
import com.opensymphony.able.util.EnumHelper;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A default {@link ActionBean} implementation that uses a @{link CrudService}
 *
 * @version $Revision$
 */
public class DefaultCrudActionBean<E> implements CrudActionBean {
    private static final Log log = LogFactory.getLog(DefaultCrudActionBean.class);

    private CrudService<E> service;
    @SpringBean
    private Validator validator;
    @SpringBean
    private QueryStrategy queryStrategy;

    private ActionBeanContext context;
    private E entity;
    private Class<E> entityClass;
    private EntityInfo entityInfo;
    private String query;

    public DefaultCrudActionBean() {
    }

    public DefaultCrudActionBean(CrudService<E> service) {
        this.service = service;
        this.entityClass = service.getEntityClass();
        this.entityInfo = new EntityInfo(entityClass);
    }

    public DefaultCrudActionBean(CrudService<E> service, QueryStrategy queryStrategy, Validator validator) {
        this.service = service;
        this.queryStrategy = queryStrategy;
        this.validator = validator;
    }

    // Actions
    // -------------------------------------------------------------------------

    /**
     * Generates a tabular view
     */
    @DontValidate
    @DefaultHandler
    public Resolution list() {
        return new ForwardResolution(getEntityInfo().getListUri());
    }

    /**
     * Views a single entity in a read only form
     */
    public Resolution view() {
        return new ForwardResolution(getEntityInfo().getViewUri());
    }

    /**
     * Views the edit form
     */
    public Resolution edit() {
        return new ForwardResolution(getEntityInfo().getEditUri());
    }

    /**
     * Deletes the current entity
     */
    @DontValidate
    public Resolution delete() {
        E e = getEntity();
        if (e != null) {
            Object idValue = getEntityInfo().getIdValue(e);
            if (idValue != null) {
                service.delete(e);
                shouldCommit();
            }
        }
        return new ForwardResolution(getEntityInfo().getListUri());
    }

    /**
     * Saves an edit form
     */
    public Resolution save() {
        validate();
        if (getContext().getValidationErrors().isEmpty()) {
            E e = getEntity();
            if (e != null) {
                Object idValue = getEntityInfo().getIdValue(e);
                if (idValue == null) {
                    service.insert(e);
                }
                else {
                    service.update(e);
                }
                shouldCommit();
                return new RedirectResolution(getActionUri());
            }
        }
        return getContext().getSourcePageResolution();
    }

    /**
     * Cancels any edits
     */
    @DontValidate
    public Resolution cancel() {
        shouldRollback();

        // TODO
        // getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

        return new RedirectResolution(getActionUri());
    }


    /**
     * Performs a search
     */
    public Resolution search() {
        return getContext().getSourcePageResolution();
    }

    /**
     * A RESTful view as a blob of XML
     */
    public Resolution xmlView() {
        return new JaxbResolution(new JaxbTemplate(entityClass), getEntity());
    }


    // Properties
    // -------------------------------------------------------------------------
    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    /**
     * The current entity
     */
    public E getEntity() {
        return entity;
    }


    public void setEntity(E entity) {
        this.entity = entity;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Class getIdClass() {
        return getEntityInfo().getIdClass();
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
        return service.findAll();
    }

    public EntityInfo getEntityInfo() {
        if (entityInfo == null) {
            entityInfo = EntityInfo.newInstance(getService().getEntityClass());
        }
        return entityInfo;
    }

    /**
     * Returns the URI of the Crud action (i.e. this POJO :)
     */
    public String getActionUri() {
        return getEntityInfo().getActionUri();
    }

    public Map<String, Object> getAllValues() {
        return new AbstractMap<String, Object>() {

            @Override
            public Object get(Object propertyName) {
                return getAllValuesForProperty((String) propertyName);
            }

            @Override
            public Set<Entry<String, Object>> entrySet() {
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

    public CrudService<E> getService() {
        if (service == null) {
            service = createService();
        }
        return service;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * Forces the current transaction to rollback which will cancel any updates
     * made as part of a form submission.
     */
    protected void shouldRollback() {
        TransactionServletFilter.shouldRollback(getContext().getRequest());
    }

    /**
     * Marks the transaction has being one that should commit (unless another
     * object decides it should rollback)
     */
    protected void shouldCommit() {
        TransactionServletFilter.shouldCommit(getContext().getRequest());
    }

    /**
     * Performs any custom validation
     */
    protected void validate() {
        validator.validate(getContext(), "entity.", getEntity());
    }

    /**
     * Returns a collection of values for the given property name of this entity.
     * This method is used so we can render pick lists of values to be chosen for related values
     */
    protected Object getAllValuesForProperty(String propertyName) {
        PropertyInfo property = getEntityInfo().getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException("Entity " + getEntityInfo().getEntityName() + " does not have a property called: " + propertyName);
        }
        EntityInfo propertyTypeInfo = property.getPropertyEntityInfo();
        if (propertyTypeInfo.isPersistent()) {
            if (service instanceof JpaCrudService) {
                JpaCrudService jpaService = (JpaCrudService) service;
                return jpaService.getJpaTemplate().find(propertyTypeInfo.getFindAllQuery());
            }
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

    protected CrudService<E> createService() {
        throw new IllegalArgumentException("No service property has been configured!");
    }
}
