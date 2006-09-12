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
package com.opensymphony.able.entity;

import com.opensymphony.able.view.EditForm;
import com.opensymphony.able.view.EditTable;
import com.opensymphony.able.view.ViewDefaults;
import com.opensymphony.able.view.ViewForm;
import com.opensymphony.able.view.ViewTable;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeConverter;

import javax.persistence.Entity;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInfo {
    private Class entityClass;
    private String entityName;
    private String entityUri;
    private String actionUri;
    private Map<String, PropertyInfo> propertyMap = new HashMap<String, PropertyInfo>();
    private List<PropertyInfo> unsortedProperties = new ArrayList<PropertyInfo>();
    private List<PropertyInfo> properties;
    private List<PropertyInfo> viewTableProperties;
    private List<PropertyInfo> editTableProperties;
    private List<PropertyInfo> viewFormProperties;
    private List<PropertyInfo> editFormProperties;
    private PropertyInfo idProperty;
    private TypeConverter typeConverter = new BeanWrapperImpl();

    public EntityInfo(Class entityClass) {
        this.entityClass = entityClass;
        introspect(entityClass);
    }

    @Override
    public String toString() {
        return "EntityInfo[name=" + entityName + "; class=" + entityClass + "]";
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public Class getIdClass() {
        if (idProperty != null) {
            return idProperty.getPropertyType();
        }
        return null;
    }

    public PropertyInfo getIdProperty() {
        return idProperty;
    }

    public Object getIdValue(Object entity) {
        if (idProperty != null) {
            return idProperty.getValue(entity);
        }
        return null;
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
     * Returns the Action URI name for the entity
     */
    public String getActionUri() {
        if (actionUri == null) {
            actionUri = createActionUri();
        }
        return actionUri;
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

    public String getHomeUri() {
        return "/" + getEntityUri() + "/index.jsp";
    }

    public String getViewUri() {
        return "/" + getEntityUri() + "/view.jsp";
    }

    public String getEditUri() {
        return "/" + getEntityUri() + "/edit.jsp?id=";
    }

    public String getBulkEditUri() {
        return "/" + getEntityUri() + "/editTable.jsp?id=";
    }

    public PropertyInfo getProperty(String name) {
        return propertyMap.get(name);
    }

    public String getFindAllQuery() {
        return "from " + getEntityName();
    }

    @SuppressWarnings("unchecked")
    public boolean isPersistent() {
        return entityClass.getAnnotation(Entity.class) != null;
    }

    public List<PropertyInfo> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public List<PropertyInfo> getViewTableProperties() {
        return Collections.unmodifiableList(viewTableProperties);
    }

    public List<PropertyInfo> getEditTableProperties() {
        return Collections.unmodifiableList(editTableProperties);
    }

    public List<PropertyInfo> getViewFormProperties() {
        return Collections.unmodifiableList(viewFormProperties);
    }

    public List<PropertyInfo> getEditFormProperties() {
        return Collections.unmodifiableList(editFormProperties);
    }

    public Object convertToPrimaryKeyValkue(String value) {
        if (value != null && value.length() > 0) {
            Class idClass = getIdClass();
            return typeConverter.convertIfNecessary(value, idClass);
        }
        return null;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
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

    protected String createActionUri() {
        return "/" + getEntityName() + ".action";
    }

    /**
     * Lets introspect all the properties
     */
    protected void introspect(Class type) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(entityClass);
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String name = descriptor.getName();
            if (name.equals("class")) {
                continue;
            }

            PropertyInfo propertyInfo = new PropertyInfo(this, descriptor);
            unsortedProperties.add(propertyInfo);
            propertyMap.put(name, propertyInfo);
            if (propertyInfo.isIdProperty()) {
                if (idProperty != null) {
                    throw new IllegalStateException("Duplicate @Id properties defined for: " + idProperty + " and " + propertyInfo);
                }
                this.idProperty = propertyInfo;
            }
        }

        // now lets create the sorted properties list
        configureViewDefaults();
        configureViewTable();
        configureEditTable();
        configureViewForm();
        configureEditForm();
    }

    protected void configureViewDefaults() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        ViewDefaults annotation = (ViewDefaults) entityClass.getAnnotation(ViewDefaults.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        properties = createOrderedList(unsortedProperties, sortOrder, includes, excludes);
    }

    protected void configureViewTable() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        ViewTable annotation = (ViewTable) entityClass.getAnnotation(ViewTable.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        viewTableProperties = createOrderedList(properties, sortOrder, includes, excludes);
    }

    protected void configureEditTable() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        EditTable annotation = (EditTable) entityClass.getAnnotation(EditTable.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        editTableProperties = createOrderedList(viewTableProperties, sortOrder, includes, excludes);
    }

    protected void configureViewForm() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        ViewForm annotation = (ViewForm) entityClass.getAnnotation(ViewForm.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        viewFormProperties = createOrderedList(properties, sortOrder, includes, excludes);
    }

    protected void configureEditForm() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        EditForm annotation = (EditForm) entityClass.getAnnotation(EditForm.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        editFormProperties = createOrderedList(viewFormProperties, sortOrder, includes, excludes);
    }

    protected List<PropertyInfo> createOrderedList(List<PropertyInfo> properties, String[] sortOrder, String[] includes, String[] excludes) {
        if (empty(sortOrder) && empty(includes) && empty(excludes)) {
            return properties;
        }
        Map<String, PropertyInfo> map = new HashMap<String, PropertyInfo>(propertyMap);
        List<PropertyInfo> answer = new ArrayList<PropertyInfo>(map.size());
        if (!empty(sortOrder)) {
            for (String name : sortOrder) {
                PropertyInfo info = map.remove(name);
                if (info != null) {
                    answer.add(info);
                }
            }
        }
        if (!empty(includes)) {
            for (String name : includes) {
                PropertyInfo info = map.remove(name);
                if (info != null) {
                    answer.add(info);
                }
            }

        }
        else {
            if (!empty(excludes)) {
                for (String name : excludes) {
                    map.remove(name);
                }
            }

            // now lets add all the remaining properties in the sort order
            for (PropertyInfo info : properties) {
                if (map.containsKey(info.getName())) {
                    answer.add(info);
                }
            }
        }
        return answer;
    }

    protected boolean empty(String[] names) {
        return names == null || names.length == 0;
    }

}
