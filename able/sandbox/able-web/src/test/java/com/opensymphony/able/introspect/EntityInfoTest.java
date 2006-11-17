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

import com.opensymphony.able.annotations.DisplayBulkEdit;
import com.opensymphony.able.annotations.DisplayList;
import com.opensymphony.able.annotations.Input;
import com.opensymphony.able.annotations.ViewField;
import com.opensymphony.able.demo.model.Attachment;
import com.opensymphony.able.demo.model.Bug;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 
 * @version $Revision$
 */
public class EntityInfoTest {

    @Test
    public void testAutoDiscoveredEntity() throws Exception {
        EntityInfo info = Entities.getInstance().getEntity(Bug.class.getName());
        assertNotNull(info);

        Class entityClass = info.getEntityClass();
        assertEquals(Bug.class, entityClass);

        String entityName = info.getEntityName();
        assertEquals("Bug", entityName);

        String entityUri = info.getEntityUri();
        assertEquals("bug", entityUri);

        Class idClass = info.getIdClass();
        assertEquals(Integer.class, idClass);

        assertEquals(info.getActionUri(), "/bug");

        PropertyInfo property = info.getProperty("openDate");
        assertNotNull(property);
        System.out.println("displayName: " + property.getDisplayName());
        System.out.println("shortDescription: " + property.getShortDescription());

        assertEquals(property.getDisplayName(), "Open Date");

        property = info.getProperty("shortDescription");
        assertNotNull(property);
        assertEquals(property.getDisplayName(), "Description");
        Input input = property.getInput();
        assertNotNull(input);

        List<PropertyInfo> viewFieldProperties = info.getNameProperties();
        assertEquals(viewFieldProperties.size(), 1, "view field properties: " + viewFieldProperties);
        assertProperty("shortDescription", viewFieldProperties, 0);
        
        property = info.getProperty("attachments");
        assertNotNull(property);
        assertTrue(property.isCollection(), "property should be a collection: " + property);
        assertEquals(property.getPropertyComponentType(), Attachment.class);
    }

    @Test
    public void testOrderOfPropertiesIsInFieldDeclarationOrder() throws Exception {
        EntityInfo info = Entities.getInstance().getEntity(Bug.class.getName());
        assertNotNull(info);

        List<PropertyInfo> properties = info.getProperties();
        assertProperty("id", properties, 0);
        assertProperty("openDate", properties, 1);
        assertProperty("shortDescription", properties, 2);
        assertProperty("longDescription", properties, 3);
        assertEquals(properties.size(), 11);
    }
    
    @Test
    public void testDefaultSortOrder() throws Exception {
        EntityInfo info = new EntityInfo(Bug.class);

        List<PropertyInfo> properties = info.getProperties();
        for (PropertyInfo property : properties) {
            System.out.println("Property: " + property);
        }

        // lets test the order of the properties
        assertProperty("id", properties, 0);
        assertProperty("openDate", properties, 1);
        assertProperty("shortDescription", properties, 2);
        assertProperty("longDescription", properties, 3);

        List<PropertyInfo> editTableProperties = info.getBulkEditProperties();
        assertProperty("openDate", editTableProperties, 0);
        assertProperty("shortDescription", editTableProperties, 1);
        /*
        assertExcludes(editTableProperties, "name");
        assertExcludes(editTableProperties, "email");
        */
    }

    @Test(enabled = false)
    public void testExcludedViewPropertiesAreExcludedFromEdit() throws Exception {
        EntityInfo info = new EntityInfo(Bug.class);

        assertExcludes(info.getViewProperties(), "longDescription");

        List<PropertyInfo> properties = info.getEditProperties();
        assertExcludes(properties, "longDescription");
    }

    @Test(enabled = false)
    public void testViewWithIncludesAndEditWithInheritedFilterAndExcludes() throws Exception {
        EntityInfo info = new EntityInfo(BugWithViewWithIncludes.class);

        List<PropertyInfo> viewTableProperties = info.getListProperties();
        assertProperty("name", viewTableProperties, 0);
        assertProperty("email", viewTableProperties, 1);
        assertEquals(2, viewTableProperties.size());

        List<PropertyInfo> editTableProperties = info.getBulkEditProperties();
        assertEquals(editTableProperties.size(), 1, "wrong size of properties: " + editTableProperties);
        assertProperty("email", editTableProperties, 0);
    }

    @DisplayList(includes = { "name", "email" })
    @DisplayBulkEdit(excludes = { "name" })
    public static class BugWithViewWithIncludes extends Bug {
    }

    @Test(enabled = false)
    public void testEditWithOverloadedIncludes() throws Exception {
        EntityInfo info = new EntityInfo(BugWithOverloadedIncludes.class);

        List<PropertyInfo> viewTableProperties = info.getListProperties();
        assertProperty("name", viewTableProperties, 0);
        assertProperty("email", viewTableProperties, 1);
        assertEquals(2, viewTableProperties.size());

        List<PropertyInfo> editTableProperties = info.getBulkEditProperties();
        assertEquals(editTableProperties.size(), 2, "wrong size of properties: " + editTableProperties);
        assertProperty("id", editTableProperties, 0);
        assertProperty("username", editTableProperties, 1);
    }

    @DisplayBulkEdit(includes = { "id", "username" })
    public static class BugWithOverloadedIncludes extends BugWithViewWithIncludes {
    }

    @Test(enabled = false)
    public void testViewFieldsDefinedViaAnnotation() throws Exception {
        EntityInfo info = new EntityInfo(BugWithViewField.class);

        List<PropertyInfo> properties = info.getNameProperties();
        assertEquals(2, properties.size());
        assertProperty("id", properties, 0);
        assertProperty("username", properties, 1);
    }

    @ViewField(includes = { "id", "username" })
    public static class BugWithViewField extends Bug {
    }

    @Test(enabled = false)
    public void testViewFieldsWithDescriptionProperty() throws Exception {
        EntityInfo info = new EntityInfo(BeanWithDescriptionProperty.class);

        List<PropertyInfo> properties = info.getNameProperties();
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
