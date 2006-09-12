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

import com.opensymphony.able.util.StringHelper;
import com.opensymphony.able.view.Label;

import javax.persistence.Id;
import javax.persistence.Transient;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;

public class PropertyInfo {

    private static final Object[] EMPTY_ARGS = {};

    private final EntityInfo entity;
    private final PropertyDescriptor descriptor;
    private boolean idProperty;
    private String displayName;

    public PropertyInfo(EntityInfo entity, PropertyDescriptor descriptor) {
        this.entity = entity;
        this.descriptor = descriptor;
        Method readMethod = descriptor.getReadMethod();
        idProperty = (readMethod != null) && (readMethod.getAnnotation(Id.class) != null);
    }

    @Override
    public String toString() {
        return "PropertyInfo[name=" + getName() + "]";
    }

    public Enumeration<String> attributeNames() {
        return descriptor.attributeNames();
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = createDisplayName();
        }
        return displayName;
    }

    protected String createDisplayName() {
        String name = descriptor.getDisplayName();
        if (name.equals(getName())) {
            // TODO lets see if we have an annotation
            Label label = descriptor.getReadMethod().getAnnotation(Label.class);
            if (label != null) {
                return label.value();
            }
            return StringHelper.splitCamelCase(name);
        }
        return name;
    }

    public String getName() {
        return descriptor.getName();
    }

    public Class<?> getPropertyType() {
        return descriptor.getPropertyType();
    }

    public String getShortDescription() {
        return descriptor.getShortDescription();
    }

    public boolean isReadable() {
        return descriptor.getReadMethod() != null;
    }

    /**
     * Returns true if the property value is writable
     */
    public boolean isWritable() {
        return descriptor.getWriteMethod() != null && !isIdProperty();
    }

    public boolean isFormProperty() {
        if (isWritable() && isReadable()) {
            if (descriptor.getReadMethod().getAnnotation(Transient.class) != null) {
                return false;
            }
            // TODO use an annotation to exclude from forms?
            return true;
        }
        return false;
    }

    public PropertyDescriptor getDescriptor() {
        return descriptor;
    }

    public EntityInfo getEntity() {
        return entity;
    }

    public boolean isIdProperty() {
        return idProperty;
    }

    public Object getValue(Object entity) {
        try {
            return descriptor.getReadMethod().invoke(entity, EMPTY_ARGS);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to extract property: " + getName() + " from: " + entity + ". Reason: " + e, e);
        }
    }

    public Object setValue(Object entity, Object value) {
        try {
            return descriptor.getWriteMethod().invoke(entity, new Object[] { value });
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update property: " + getName() + " on entity: " + entity + ". Reason: " + e, e);
        }
    }

    public boolean isCollection() {
        Class<?> propertyType = descriptor.getPropertyType();
        return propertyType.isArray() || propertyType.isAssignableFrom(Collection.class);
    }

    /**
     * Returns the component type of the property - e.g. ignoring the
     * cardinality (array or List).
     */
    public Class getPropertyComponentType() {
        Class<?> propertyType = descriptor.getPropertyType();

        if (propertyType.isArray()) {
            return propertyType.getComponentType();
        }
        if (propertyType.isAssignableFrom(Collection.class)) {
            ParameterizedType genericSuperclass = (ParameterizedType) propertyType.getGenericSuperclass();
            Type[] typeArguments = genericSuperclass.getActualTypeArguments();
            return (Class) typeArguments[0];
        }
        return propertyType;
    }

    public EntityInfo getPropertyEntityInfo() {
        // TODO - use registry....
        return new EntityInfo(getPropertyComponentType());
    }
}
