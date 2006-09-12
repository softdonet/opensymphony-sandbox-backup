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
package org.opensymphony.able.entity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import com.opensymphony.able.entity.Entities;
import com.opensymphony.able.entity.EntityInfo;
import com.opensymphony.able.entity.PropertyInfo;
import com.opensymphony.able.view.EditTable;
import com.opensymphony.able.view.ViewField;
import com.opensymphony.able.view.ViewTable;

import org.opensymphony.able.example.model.Address;
import org.opensymphony.able.example.model.Project;
import org.opensymphony.able.example.model.User;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * 
 * @version $Revision: 11 $
 */
public class EntityInfoTest {

    @Test
    public void testAutoDiscoveredEntity() throws Exception {
        Map<String, EntityInfo> map = Entities.getInstance().getEntityMap();
        assertEquals(1, map.size(), "Should have one item: " + map);

        EntityInfo info = Entities.getInstance().getEntity("User");
        assertNotNull(info);

        Class entityClass = info.getEntityClass();
        assertEquals(User.class, entityClass);

        String entityName = info.getEntityName();
        assertEquals("User", entityName);

        String entityUri = info.getEntityUri();
        assertEquals("user", entityUri);

        Class idClass = info.getIdClass();
        assertEquals(Long.class, idClass);

        assertEquals(info.getHomeUri(), "/views/entity/user/index.jsp");
        assertEquals(info.getActionUri(), "/User.action");
        assertEquals(info.getViewUri(), "/views/entity/user/view.jsp?id=");
        assertEquals(info.getEditUri(), "/views/entity/user/edit.jsp?id=");
        
        PropertyInfo property = info.getProperty("creationDate");
        assertNotNull(property);
        System.out.println("displayName: " + property.getDisplayName());
        System.out.println("shortDescription: " + property.getShortDescription());

        assertEquals(property.getDisplayName(), "Creation Date");

        property = info.getProperty("username");
        assertNotNull(property);
        assertEquals(property.getDisplayName(), "User account name");

        List<PropertyInfo> viewFieldProperties = info.getViewFieldProperties();
        assertEquals(viewFieldProperties.size(), 1, "view field properties: " + viewFieldProperties);
        assertProperty("name", viewFieldProperties, 0);
        
        property = info.getProperty("addresses");
        assertNotNull(property);
        assertTrue(property.isCollection(), "property should be a collection: " + property);
        assertEquals(property.getPropertyComponentType(), Address.class);

        property = info.getProperty("projects");
        assertNotNull(property);
        assertTrue(property.isCollection(), "property should be a collection: " + property);
        assertEquals(property.getPropertyComponentType(), Project.class);
    }

    @Test
    public void testOrderOfPropertiesIsInFieldDeclarationOrder() throws Exception {
        EntityInfo info = Entities.getInstance().getEntity(Project.class.getName());
        assertNotNull(info);

        List<PropertyInfo> properties = info.getProperties();
        assertProperty("id", properties, 0);
        assertProperty("name", properties, 1);
        assertProperty("description", properties, 2);
        assertProperty("category", properties, 3);
        assertEquals(properties.size(), 4);
    }
    
    @Test
    public void testDefaultSortOrder() throws Exception {
        EntityInfo info = new EntityInfo(User.class);

        List<PropertyInfo> properties = info.getProperties();
        for (PropertyInfo property : properties) {
            System.out.println("Property: " + property);
        }

        // lets test the order of the properties
        assertProperty("username", properties, 0);
        assertProperty("name", properties, 1);
        assertProperty("email", properties, 2);
        assertProperty("type", properties, 3);

        List<PropertyInfo> editTableProperties = info.getEditTableProperties();
        assertProperty("username", editTableProperties, 0);
        assertProperty("type", editTableProperties, 1);
        assertExcludes(editTableProperties, "name");
        assertExcludes(editTableProperties, "email");
    }

    @Test
    public void testEditViewsExcludeItemsExcludedFromView() throws Exception {
        EntityInfo info = new EntityInfo(User.class);

        assertExcludes(info.getViewFormProperties(), "creationDate");

        List<PropertyInfo> properties = info.getEditFormProperties();
        assertExcludes(properties, "creationDate");
    }

    @Test
    public void testViewWithIncludesAndEditWithInheritedFilterAndExcludes() throws Exception {
        EntityInfo info = new EntityInfo(UserWithViewWithIncludes.class);

        List<PropertyInfo> viewTableProperties = info.getViewTableProperties();
        assertProperty("name", viewTableProperties, 0);
        assertProperty("email", viewTableProperties, 1);
        assertEquals(2, viewTableProperties.size());

        List<PropertyInfo> editTableProperties = info.getEditTableProperties();
        assertEquals(editTableProperties.size(), 1, "wrong size of properties: " + editTableProperties);
        assertProperty("email", editTableProperties, 0);
    }

    @ViewTable(includes = { "name", "email" })
    @EditTable(excludes = { "name" })
    public static class UserWithViewWithIncludes extends User {
    }

    @Test
    public void testEditWithOverloadedIncludes() throws Exception {
        EntityInfo info = new EntityInfo(UserWithOverloadedIncludes.class);

        List<PropertyInfo> viewTableProperties = info.getViewTableProperties();
        assertProperty("name", viewTableProperties, 0);
        assertProperty("email", viewTableProperties, 1);
        assertEquals(2, viewTableProperties.size());

        List<PropertyInfo> editTableProperties = info.getEditTableProperties();
        assertEquals(editTableProperties.size(), 2, "wrong size of properties: " + editTableProperties);
        assertProperty("id", editTableProperties, 0);
        assertProperty("username", editTableProperties, 1);
    }

    @EditTable(includes = { "id", "username" })
    public static class UserWithOverloadedIncludes extends UserWithViewWithIncludes {
    }

    @Test
    public void testViewFieldsDefinedViaAnnotation() throws Exception {
        EntityInfo info = new EntityInfo(UserWithViewField.class);

        List<PropertyInfo> properties = info.getViewFieldProperties();
        assertEquals(2, properties.size());
        assertProperty("id", properties, 0);
        assertProperty("username", properties, 1);
    }

    @ViewField(includes = { "id", "username" })
    public static class UserWithViewField extends User {
    }

    @Test
    public void testViewFieldsWithDescriptionProperty() throws Exception {
        EntityInfo info = new EntityInfo(BeanWithDescriptionProperty.class);

        List<PropertyInfo> properties = info.getViewFieldProperties();
        assertEquals(1, properties.size());
        assertProperty("description", properties, 0);
    }

    public static class BeanWithDescriptionProperty {
        private String foo;
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void assertExcludes(List<PropertyInfo> properties, String name) {
        for (PropertyInfo property : properties) {
            if (property.getName().equals(name)) {
                fail("Should have excluded property: " + name + " from property: " + property);
            }
        }
    }

    protected void assertProperty(String expected, List<PropertyInfo> properties, int index) {
        PropertyInfo property = properties.get(index);
        assertNotNull(property, "No property found at index: " + index);
        assertEquals(property.getName(), expected, "Property name for: " + property);
    }

}
