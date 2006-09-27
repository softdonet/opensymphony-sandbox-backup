/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.service;

import com.opensymphony.able.entity.Entities;
import com.opensymphony.able.entity.EntityInfo;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A helper class which uses the Spring entity manager factory bean to load the entities.
 *
 * @version $Revision$
 */
public class EntityFinder {
    public EntityFinder(AbstractEntityManagerFactoryBean entityManagerFactoryBean) {
        Entities entities = Entities.getInstance();

        EntityManagerFactory nativeEM = entityManagerFactoryBean.getNativeEntityManagerFactory();
        if (nativeEM instanceof HibernateEntityManagerFactory) {
            HibernateEntityManagerFactory hibernateEMF = (HibernateEntityManagerFactory) nativeEM;
            Map<Object, Object> metadata = hibernateEMF.getSessionFactory().getAllClassMetadata();
            Set<Map.Entry<Object, Object>> entries = metadata.entrySet();
            for (Map.Entry entry : entries) {
                Object key = entry.getKey();
                if (key != null) {
                    String className = key.toString();
                    entities.getEntityByClass(className);
                }
            }
        }

        Collection<EntityInfo> collection = entities.getEntities();
        for (EntityInfo entityInfo : collection) {
            System.out.println("Entity: " + entityInfo);
        }
    }
}
