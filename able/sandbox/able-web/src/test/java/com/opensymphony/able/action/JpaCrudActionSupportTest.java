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

import java.util.List;

import com.opensymphony.able.example.model.Type;
import com.opensymphony.able.example.model.User;
import com.opensymphony.able.example.action.UserActionBean;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.validation.DataBinder;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 
 * @version $Revision$
 */
public class JpaCrudActionSupportTest {

    @Test
    public void testReflection() throws Exception {

        UserActionBean action = new UserActionBean();

        Class idClass = action.getIdClass();
        Assert.assertEquals(Long.class, idClass);

        Class<User> entityClass = action.getEntityClass();
        Assert.assertEquals(User.class, entityClass);
        
        String entityName = action.getEntityInfo().getEntityName();
        Assert.assertEquals("User", entityName);

        String entityUri = action.getEntityInfo().getEntityUri();
        Assert.assertEquals("user", entityUri);
        
        Object values = action.getAllValues().get("type");
        System.out.println("Values: " + values);
        
        Assert.assertTrue(values instanceof List, "values is an array");
        List valueArray = (List) values;
        
        for (Object object : valueArray) {
			System.out.println("Found type value: " + object);
		}
        
        Assert.assertEquals(3, valueArray.size());
        System.out.println("found values: " + values);
        
        Assert.assertEquals(Type.Customer, valueArray.get(0));
        Assert.assertEquals(Type.Manager, valueArray.get(1));
        Assert.assertEquals(Type.Admin, valueArray.get(2));
    }

    @Test
    public void testIntrospection() throws Exception {
        User user = new User();

        DataBinder binder = new DataBinder(user);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue("id", "1234567890");
        binder.bind(propertyValues);

        Assert.assertEquals(new Long(1234567890), user.getId());
        
        TypeConverter typeConverter = new BeanWrapperImpl();
        Object value = typeConverter.convertIfNecessary("1234567890", Long.class);
        Assert.assertEquals(new Long(1234567890), value);
    }

}
