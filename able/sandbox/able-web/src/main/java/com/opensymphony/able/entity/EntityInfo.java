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

import com.opensymphony.able.view.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeConverter;

import javax.persistence.Entity;
import javax.xml.namespace.QName;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

public class EntityInfo {
    private Class<Object> entityClass;
    private String entityName;
    private String entityUri;
    private String actionUri;
    private Map<String, PropertyInfo> propertyMap = new HashMap<String, PropertyInfo>();
    private List<PropertyInfo> properties;
    private List<PropertyInfo> listProperties;
    private List<PropertyInfo> viewProperties;
    private List<PropertyInfo> editProperties;
    private List<PropertyInfo> nameProperties;
    private List<PropertyInfo> bulkEditProperties;
    private PropertyInfo idProperty;
    private TypeConverter typeConverter = new BeanWrapperImpl();
    private String[] defaultViewFieldPropertyNames = {"name", "shortDescription", "description", "code"};


    /**
     * Creates a new instance making it available in the static registry
     */
    public static EntityInfo newInstance(Class type) {
        return Entities.getInstance().getEntityByClass(type);
    }

    public EntityInfo(Class entityClass) {
        //noinspection unchecked
        this.entityClass = entityClass;
        //noinspection unchecked
        introspect(entityClass);
    }

    @Override
    public String toString() {
        return "EntityInfo[name=" + getEntityName() + "; class=" + entityClass + "]";
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

    public List<PropertyInfo> getListProperties() {
        return Collections.unmodifiableList(listProperties);
    }

    public List<PropertyInfo> getBulkEditProperties() {
        return Collections.unmodifiableList(bulkEditProperties);
    }

    public List<PropertyInfo> getViewProperties() {
        return Collections.unmodifiableList(viewProperties);
    }

    public List<PropertyInfo> getEditProperties() {
        return Collections.unmodifiableList(editProperties);
    }

    /**
     * Returns the list of properties to display in a field view or pick list, combo box, radio selection etc
     */
    public List<PropertyInfo> getNameProperties() {
        return Collections.unmodifiableList(nameProperties);
    }

    /**
     * Returns the first name property used to present the entity in a pick list, combo box, radio selection etc
     */
    public PropertyInfo getNameProperty() {
        if (nameProperties.isEmpty()) {
            return null;
        }
        return nameProperties.get(0);
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
        return "/" + getEntityUri();
    }

    /**
     * Lets introspect all the properties
     */
    protected void introspect(Class<Object> type) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(entityClass);
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String name = descriptor.getName();
            if (name.equals("class")) {
                continue;
            }

            PropertyInfo propertyInfo = new PropertyInfo(this, descriptor, entityClass);
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
        configureViewField();
    }

    protected void configureViewDefaults() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        DisplayDefaults annotation = entityClass.getAnnotation(DisplayDefaults.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }
        List<PropertyInfo> defaultPropertyListOrder = createDefaultOrderList();
        properties = createOrderedList(defaultPropertyListOrder, sortOrder, includes, excludes);
    }

    protected void configureViewTable() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        DisplayList annotation = entityClass.getAnnotation(DisplayList.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        listProperties = createOrderedList(properties, sortOrder, includes, excludes);
    }

    protected void configureEditTable() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = getDefaultEditExcludes();
        DisplayBulkEdit annotation = entityClass.getAnnotation(DisplayBulkEdit.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        bulkEditProperties = createOrderedList(listProperties, sortOrder, includes, excludes);
    }

    protected void configureViewForm() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = null;
        DisplayView annotation = entityClass.getAnnotation(DisplayView.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        viewProperties = createOrderedList(properties, sortOrder, includes, excludes);
    }

    protected void configureEditForm() {
        String[] sortOrder = null;
        String[] includes = null;
        String[] excludes = getDefaultEditExcludes();
        DisplayEdit annotation = entityClass.getAnnotation(DisplayEdit.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
        }

        editProperties = createOrderedList(viewProperties, sortOrder, includes, excludes);

        // lets remove any collection types
        Iterator<PropertyInfo> iter = editProperties.iterator();
        while (iter.hasNext()) {
            PropertyInfo propertyInfo = iter.next();
            if (propertyInfo.isCollection()) {
                iter.remove();
            }
        }
    }

    protected String[] getDefaultEditExcludes() {
        String[] excludes = null;
        if (idProperty != null) {
            excludes = new String[]{idProperty.getName()};
        }
        return excludes;
    }

    protected void configureViewField() {
        String[] sortOrder;
        String[] includes;
        String[] excludes;
        ViewField annotation = entityClass.getAnnotation(ViewField.class);
        if (annotation != null) {
            sortOrder = annotation.sortOrder();
            includes = annotation.includes();
            excludes = annotation.excludes();
            nameProperties = createOrderedList(properties, sortOrder, includes, excludes);
        } else {
            nameProperties = findDefaultViewFields(properties);
        }
    }

    protected List<PropertyInfo> findDefaultViewFields(List<PropertyInfo> visibleProperties) {
        List<PropertyInfo> answer = new ArrayList<PropertyInfo>();
        Set<String> set = new HashSet<String>(Arrays.asList(defaultViewFieldPropertyNames));
        for (PropertyInfo info : visibleProperties) {
            if (set.contains(info.getName())) {
                answer.add(info);
                break;
            }
        }
        if (answer.isEmpty()) {
            // lets find the first property which is a string
            for (PropertyInfo info : visibleProperties) {
                if (info.getPropertyType().equals(String.class)) {
                    answer.add(info);
                    break;
                }
            }
        }
        if (answer.isEmpty() && visibleProperties.size() > 0) {
            // lets just use the first
            answer.add(visibleProperties.get(0));
        }
        return answer;
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

        } else {
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

    /**
     * Creates the default sorted order of the properties. Lets default to the
     * order in which the fields are defined as thats better than just sorting
     * them in alphabetical order which is the default introspection order. Note
     * this will not work for properties which do not have a matching field; but
     * then folks can use the {@link DisplayDefaults} annotation to fix those
     * cases.
     *
     * @return
     */
    protected List<PropertyInfo> createDefaultOrderList() {
        List<PropertyInfo> answer = new ArrayList<PropertyInfo>();
        SortedMap<String, PropertyInfo> map = new TreeMap<String, PropertyInfo>(propertyMap);
        appendDefaultPropertyList(entityClass, answer, map);

        // now lets add the remaining values
        answer.addAll(map.values());
        return answer;
    }

    protected void appendDefaultPropertyList(Class<Object> type, List<PropertyInfo> list, Map<String, PropertyInfo> map) {
        Class superclass = type.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            //noinspection unchecked
            appendDefaultPropertyList(superclass, list, map);
        }
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            PropertyInfo property = map.remove(field.getName());
            if (property != null) {
                list.add(property);
            }
        }
    }

    protected boolean empty(String[] names) {
        return names == null || names.length == 0;
    }

    /**
     * Creates a new QName for this entity type
     *
     * @return
     */
    public QName getQName() {
        return new QName(getEntityUri());
    }
}
