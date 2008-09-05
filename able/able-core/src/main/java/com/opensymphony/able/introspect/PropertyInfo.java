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
package com.opensymphony.able.introspect;

import com.opensymphony.able.annotations.Input;
import com.opensymphony.able.util.CollectionHelper;
import com.opensymphony.able.util.StringHelper;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.core.annotation.AnnotationUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;

public class PropertyInfo {

    private static final Object[] EMPTY_ARGS = {};

    private final EntityInfo entity;
    private final PropertyDescriptor descriptor;
    private Class entityClass;
    private boolean idProperty;
    private String displayName;

    public static PropertyInfo getPropertyInfo(Object entity, String name) {
        EntityInfo entityInfo = Entities.info(entity);
        if (entityInfo != null) {
            return entityInfo.getProperty(name);
        }
        return null;
    }

    public PropertyInfo(EntityInfo entity, PropertyDescriptor descriptor, Class entityClass) {
        this.entity = entity;
        this.descriptor = descriptor;
        this.entityClass = entityClass;
        idProperty = findAnnotation(Id.class) != null;
    }

    protected <T extends Annotation> T findAnnotation(Class<T> clazz) {
        String name = descriptor.getName();
        try {
            Field field = entityClass.getDeclaredField(name);
            T a = field.getAnnotation(clazz);
            if (a != null) {
                return a;
            }
        } catch (NoSuchFieldException e) {
            // this should generally never happen...
        }

        Method readMethod = descriptor.getReadMethod();
        if (readMethod != null) {
            return AnnotationUtils.getAnnotation(readMethod, clazz);
        }

        return null;
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

    public String getName() {
        return descriptor.getName();
    }

    public Class<?> getPropertyType() {
        return descriptor.getPropertyType();
    }

    public String getShortDescription() {
        return descriptor.getShortDescription();
    }

    public PropertyDescriptor getDescriptor() {
        return descriptor;
    }

    public EntityInfo getEntity() {
        return entity;
    }

    /**
     * Returns the value of the property
     */
    public Object getValue(Object entity) {
        try {
            return descriptor.getReadMethod().invoke(entity, EMPTY_ARGS);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to extract property: "
                    + getName() + " from: " + entity + ". Reason: " + e, e);
        }
    }

    public Object setValue(Object entity, Object value) {
        try {
            return descriptor.getWriteMethod().invoke(entity,
                    new Object[]{value});
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update property: "
                    + getName() + " on entity: " + entity + ". Reason: " + e, e);
        }
    }


    /**
     * Returns the size of the collection if the property is a collection (array or Collection)
     */
    public int getSize(Object entity) {
        if (!isCollection()) {
            return 1;
        }
        Object value = getValue(entity);
        return CollectionHelper.size(value);
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
            if (findAnnotation(Transient.class) != null) {
                return false;
            }
            // TODO use an annotation to exclude from forms?
            return true;
        }
        return false;
    }

    /**
     * Returns true if this property is a date entry field (so that a date
     * picker or calendar control should be used)
     */
    public boolean isDate() {
        return Date.class.isAssignableFrom(getPropertyType());
    }

    /**
     * Returns true if the type of the property is a numeric type
     */
    public boolean isNumber() {
        Class<?> type = getPropertyType();
        return Number.class.isAssignableFrom(type)
                || (type.isPrimitive() && type != boolean.class && type != char.class);
    }

    public boolean isIdProperty() {
        return idProperty;
    }

    public boolean isEnum() {
        return Enum.class.isAssignableFrom(getPropertyType());
    }

    public boolean isCollection() {
        Class<?> propertyType = descriptor.getPropertyType();
        return propertyType.isArray()
                || Collection.class.isAssignableFrom(propertyType);
    }

    public boolean isPersistent() {
        return getPropertyEntityInfo().isPersistent();
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
        if (Collection.class.isAssignableFrom(propertyType)) {
            if (descriptor.getReadMethod() != null) {
                ParameterizedType genericSuperclass = (ParameterizedType) descriptor
                        .getReadMethod().getGenericReturnType();
                Type[] typeArguments = genericSuperclass
                        .getActualTypeArguments();
                return (Class) typeArguments[0];
            } else {
                // no idea what the type is
                // TODO do we have an annotation?
                return Object.class;
            }
        }
        return propertyType;
    }

    public EntityInfo getPropertyEntityInfo() {
        return EntityInfo.newInstance(getPropertyComponentType());
    }

    public Input getInput() {
        return findAnnotation(Input.class);
    }

    protected String createDisplayName() {
        String name = descriptor.getDisplayName();
        if (name.equals(getName())) {
            // lets see if we have an annotation
            Input input = findAnnotation(Input.class);
            if (input != null) {
                String label = input.label();
                if (label != null && label.length() > 0) {
                    return label;
                }
            }
            return StringHelper.splitCamelCase(name);
        }
        return name;
    }
}
