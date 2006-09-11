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

import java.util.Map;

import com.opensymphony.able.entity.Entities;
import com.opensymphony.able.entity.EntityInfo;

import static org.testng.Assert.*;

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
        
        testUserEntityInfo(info);
    }
    
    @Test
    public void testReflection() throws Exception {
        EntityInfo info = new EntityInfo(User.class);
        testUserEntityInfo(info);
    }

    protected void testUserEntityInfo(EntityInfo info) {
        Class entityClass = info.getEntityClass();
        assertEquals(User.class, entityClass);
        
        String entityName = info.getEntityName();
        assertEquals("User", entityName);

        String entityUri = info.getEntityUri();
        assertEquals("user", entityUri);

        Class idClass = info.getIdClass();
        assertEquals(Long.class, idClass);
    }
    
}
