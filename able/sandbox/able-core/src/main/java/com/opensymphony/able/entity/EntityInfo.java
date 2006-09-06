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

import java.beans.*;
import java.util.*;

import org.springframework.beans.BeanUtils;

public class EntityInfo {
    private Class entityClass;
    private String entityName;
    private String entityUri;
    private String actionUri;
    private List<PropertyInfo> properties = new ArrayList<PropertyInfo>();
    private PropertyInfo idProperty;

    public EntityInfo(Class entityClass) {
        this.entityClass = entityClass;
        introspect(entityClass);
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

    public List<PropertyInfo> getProperties() {
        return Collections.unmodifiableList(properties);
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
            properties.add(propertyInfo);
            if (propertyInfo.isIdProperty()) {
                if (idProperty != null) {
                    throw new IllegalStateException("Duplicate @Id properties defined for: " + idProperty + " and " + propertyInfo);
                }
                this.idProperty = propertyInfo;
            }
        }
    }

}
