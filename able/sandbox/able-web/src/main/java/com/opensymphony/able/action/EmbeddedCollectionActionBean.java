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
import com.opensymphony.able.entity.Option;
import com.opensymphony.able.entity.PropertyInfo;
import com.opensymphony.able.filter.TransactionOutcome;
import com.opensymphony.able.jaxb.JaxbResolution;
import com.opensymphony.able.jaxb.JaxbTemplate;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A base {@link ActionBean} class for viewing and editing an embedded
 * collection of an entity
 *
 * @version $Revision$
 */
public abstract class EmbeddedCollectionActionBean<O, E> implements CrudActionBean {
    private static final Log log = LogFactory.getLog(EmbeddedCollectionActionBean.class);

    private ActionBeanContext context;
    private String propertyName;
    private O owner;
    private Class<O> ownerClass;
    private List<E> entities = new ArrayList<E>();
    private List<E> delete = new ArrayList<E>();
    private Class<E> entityClass;
    private EntityInfo ownerInfo;
    private EntityInfo entityInfo;

    public EmbeddedCollectionActionBean(String propertyName) {
        this.propertyName = propertyName;
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = genericSuperclass.getActualTypeArguments();
        this.ownerClass = (Class<O>) typeArguments[0];
        this.entityClass = (Class<E>) typeArguments[1];
        init();
    }


    protected EmbeddedCollectionActionBean(String propertyName, Class<O> ownerClass, Class<E> entityClass) {
        this.propertyName = propertyName;
        this.ownerClass = ownerClass;
        this.entityClass = entityClass;
        init();
    }

    private void init() {
        this.ownerInfo = new EntityInfo(ownerClass);
        this.entityInfo = new EntityInfo(entityClass);
    }

    // Actions
    // -------------------------------------------------------------------------
    @DontValidate
    @DefaultHandler
    public Resolution view() {
        return new ForwardResolution("/WEB-INF/jsp/generic/editCollection.jsp");
    }

    /**
     * Lets save the current list of entities
     */
    public Resolution save() {
        if (getContext().getValidationErrors().isEmpty()) {
            List<E> ownerCollection = getOwnedEntities();

            List<E> formSubmittedEntities = getEntities();

            System.out.println(">>>> owned entities: " + ownerCollection);
            System.out.println(">>>> submitted entities: " + formSubmittedEntities);
            System.out.println(">>>> delete entities: " + getDelete());

            Map<Object, E> set = new HashMap<Object, E>(formSubmittedEntities.size());
            for (E formSubmittedEntity : formSubmittedEntities) {
                Object id = getEntityInfo().getIdValue(formSubmittedEntity);
                set.put(id, formSubmittedEntity);
            }

            Iterator<E> iter = ownerCollection.iterator();
            while (iter.hasNext()) {
                E entity = iter.next();
                Object id = getEntityInfo().getIdValue(entity);
                if (set.remove(id) == null) {
                    iter.remove();
                }
            }

            ownerCollection.addAll(set.values());
            ownerCollection.removeAll(getDelete());

            System.out.println(">>>> owner collection before commit: " + getOwnedEntities());

            TransactionOutcome.shouldCommit();
        }

        // TODO
        // getContext().addMsg( "saved " + getEntityName(); );

        return new RedirectResolution(getHomeUri());
    }

    @DontValidate
    public Resolution cancel() {
        TransactionOutcome.shouldRollback();

        // TODO
        // getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

        return new RedirectResolution(getHomeUri());
    }


    public Resolution delete() {
        boolean shouldCommit = false;
        for (E entity : delete) {
            List<E> ownerCollection = getOwnedEntities();
            if (ownerCollection.remove(entity)) {
                shouldCommit = true;
            }
        }
        if (shouldCommit) {
            TransactionOutcome.shouldCommit();
        }
        return new RedirectResolution(getHomeUri());
    }

    public Resolution xmlView() {
        return new JaxbResolution(new JaxbTemplate(entityClass), getEntities());
    }

    // Properties
    // -------------------------------------------------------------------------
    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public O getOwner() {
        return owner;
    }

    public void setOwner(O owner) {
        this.owner = owner;
    }

    /**
     * Returns the embedded collection submitted from a form
     */
    public List<E> getEntities() {
        return entities;
    }

    public void setEntities(List<E> entities) {
        this.entities = entities;
    }

    /**
     * Returns the entities submitted from a form which should be deleted
     */
    public List<E> getDelete() {
        return delete;
    }

    public void setDelete(List<E> delete) {
        this.delete = delete;
    }


    /**
     * Returns the current embedded collection
     */
    public List<E> getOwnedEntities() {
        return getOwnerCollection(owner);
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Class getIdClass() {
        return ownerInfo.getIdClass();
    }

    public abstract List<E> getAllEntities();

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    /**
     * Returns a collection of {@link Option} objects to make it easy to render
     * multi select menus
     */
    public List<Option> getOptions() {
        return Option.createOptions(getAllEntities(), getOwnedEntities(), entityInfo);
    }

    public String getActionUri() {
        // TODO move to EntityInfo?
        return ownerInfo.getActionUri() + "_" + propertyName;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    protected String getHomeUri() {
        return ownerInfo.getActionUri();
    }

    /**
     * Looks up the owners related entities
     */
    protected List<E> getOwnerCollection(O owningEntity) {
        PropertyInfo info = ownerInfo.getProperty(propertyName);
        if (info == null) {
            throw new IllegalArgumentException("No such property: " + propertyName + " on type: " + ownerInfo.getEntityName());
        }
        return (List<E>) info.getValue(owningEntity);
    }
}
