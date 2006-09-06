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

import org.opensymphony.able.example.model.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.opensymphony.able.entity.EntityInfo;

/**
 * 
 * @version $Revision: 11 $
 */
public class EntityInfoTest {

    @Test
    public void testReflection() throws Exception {

        EntityInfo action = new EntityInfo(User.class);

        Class entityClass = action.getEntityClass();
        Assert.assertEquals(User.class, entityClass);
        
        String entityName = action.getEntityName();
        Assert.assertEquals("User", entityName);

        String entityUri = action.getEntityUri();
        Assert.assertEquals("user", entityUri);

        Class idClass = action.getIdClass();
        Assert.assertEquals(Long.class, idClass);
    }

    
}
