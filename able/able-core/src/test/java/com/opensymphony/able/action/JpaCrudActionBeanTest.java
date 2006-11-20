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
package com.opensymphony.able.action;

import com.opensymphony.able.demo.action.PersonActionBean;
import com.opensymphony.able.demo.model.Person;
import com.opensymphony.able.demo.model.Role;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.validation.DataBinder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 
 * @version $Revision$
 */
public class JpaCrudActionBeanTest {

    @Test
    public void testReflection() throws Exception {

        PersonActionBean action = new PersonActionBean();

        Class idClass = action.getIdClass();
        Assert.assertEquals(Integer.class, idClass);

        Class<Person> entityClass = action.getEntityClass();
        Assert.assertEquals(Person.class, entityClass);
        
        String entityName = action.getEntityInfo().getEntityName();
        Assert.assertEquals("Person", entityName);

        String entityUri = action.getEntityInfo().getEntityUri();
        Assert.assertEquals("person", entityUri);
        
        Object values = action.getAllValues().get("role");
        System.out.println("Values: " + values);
        
        Assert.assertTrue(values instanceof List, "values is an array");
        List valueArray = (List) values;
        
        for (Object object : valueArray) {
			System.out.println("Found type value: " + object);
		}
        
        Assert.assertEquals(3, valueArray.size());
        System.out.println("found values: " + values);
        
        Assert.assertEquals(Role.Employee, valueArray.get(0));
        Assert.assertEquals(Role.Manager, valueArray.get(1));
        Assert.assertEquals(Role.BigCheese, valueArray.get(2));
    }

    @Test
    public void testIntrospection() throws Exception {
        Person bug = new Person();

        DataBinder binder = new DataBinder(bug);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("id", "123456");
        binder.bind(propertyValues);

        Assert.assertEquals(new Integer(123456), bug.getId());
        
        TypeConverter typeConverter = new BeanWrapperImpl();
        Object value = typeConverter.convertIfNecessary("123456", Integer.class);
        Assert.assertEquals(new Integer(123456), value);
    }

}
