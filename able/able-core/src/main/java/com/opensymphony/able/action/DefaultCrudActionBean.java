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

import com.opensymphony.able.annotations.Partial;
import com.opensymphony.able.filter.TransactionOutcome;
import com.opensymphony.able.introspect.EntityInfo;
import com.opensymphony.able.introspect.PropertyInfo;
import com.opensymphony.able.service.CrudService;
import com.opensymphony.able.service.JpaCrudService;
import com.opensymphony.able.stripes.DefaultResolution;
import com.opensymphony.able.stripes.GenerateResolution;
import com.opensymphony.able.util.EnumHelper;
import static com.opensymphony.able.util.StringHelper.notBlank;
import com.opensymphony.able.xml.JaxbResolution;
import com.opensymphony.able.xml.JaxbTemplate;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
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
public class DefaultCrudActionBean<E> extends EntityActionBeanSupport<E> {
    private static final Log log = LogFactory.getLog(DefaultCrudActionBean.class);

    private CrudService<E> service;
    @SpringBean
    private Validator validator;
    @SpringBean
    private QueryStrategy queryStrategy;

    private E entity;
    private String query;

    public DefaultCrudActionBean() {
    }

    public DefaultCrudActionBean(Class<E> entityClass) {
        super(entityClass);
    }

    public DefaultCrudActionBean(CrudService<E> service) {
        this(service.getEntityClass());
    }

    public DefaultCrudActionBean(CrudService<E> service, QueryStrategy queryStrategy, Validator validator) {
        this(service);
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
        return new DefaultResolution(getClass(), getContext(), "/WEB-INF/jsp/generic/list.jsp");
    }

    @Partial
    @DontValidate
    public Resolution generateList() {
        return new GenerateResolution(getClass(), getContext());
    }

    /**
     * Views a single entity in a read only form
     */
    public Resolution view() {
        return new DefaultResolution(getClass(), getContext(), "/WEB-INF/jsp/generic/view.jsp?entity=");
    }

    @Partial
    @DontValidate
    public Resolution generateView() {
        return new GenerateResolution(getClass(), getContext());
    }

    /**
     * Views the edit form
     */
    public Resolution edit() {
        return new DefaultResolution(getClass(), getContext(), "/WEB-INF/jsp/generic/edit.jsp?entity=");
    }

    @Partial
    @DontValidate
    public Resolution generateEdit() {
        return new GenerateResolution(getClass(), getContext());
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
                getService().delete(e);
                TransactionOutcome.shouldCommit();
            }
        }
        return new RedirectResolution(getActionUri());
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
                    getService().insert(e);
                }
                else {
                    getService().update(e);
                }
                TransactionOutcome.shouldCommit();
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
        TransactionOutcome.shouldRollback();

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


    /**
     * The current entity
     */
    public E getEntity() {
        if (entity == null) {
            // lets lazily create an instance so we pre-populate the
            // create new edit form
            try {
                entity = getService().newInstance();
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to create a new instance of: " + getEntityClass() + ". Reason: " + e, e);
            }
        }
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    /**
     * Returns the primary key of the entity
     */
    public Object getId() {
        return getEntityInfo().getIdValue(getEntity());
    }

    @SuppressWarnings("unchecked")
    public List<E> getAllEntities() {
        String text = getQuery();
        if (notBlank(text)) {
            if (queryStrategy == null) {
                throw new IllegalArgumentException("No QueryStrategy was injected!");
            }
            List answer = queryStrategy.execute(entityClass, text);
            log.info("Query with: " + text + " found: " + answer);
            return answer;
        }
        return getService().findAll();
    }

    public Map<String, Object> getAllValues() {
        return new AbstractMap<String, Object>() {

            @Override
            public Object get(Object propertyName) {
                return getAllValuesForProperty((String) propertyName);
            }

            @Override
            public Set<Entry<String, Object>> entrySet() {
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
     * Performs any custom validation
     */
    protected void validate() {
        if (validator != null) {
            validator.validate(getContext(), "entity.", getEntity());
        }
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
            CrudService<E> s = getService();
            if (s instanceof JpaCrudService) {
                JpaCrudService jpaService = (JpaCrudService) s;
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
        log.warn("No such property: " + propertyName + " for entity: " + entityInfo.getEntityName());
        return Collections.EMPTY_LIST;
    }

    protected CrudService<E> createService() {
        throw new IllegalArgumentException("No service property has been configured!");
    }

    protected EntityInfo createEntityInfo() {
        return EntityInfo.newInstance(getService().getEntityClass());
    }

}
