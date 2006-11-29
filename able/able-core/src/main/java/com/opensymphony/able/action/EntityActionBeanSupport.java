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
package com.opensymphony.able.action;

import com.opensymphony.able.introspect.EntityInfo;

/**
 * A useful base class for entity based actions such as searches or full CRUD operations
 * 
 * @version $Revision$
 */
public class EntityActionBeanSupport<E> extends DefaultActionBean implements CrudActionBean {
    protected Class<E> entityClass;
    protected EntityInfo entityInfo;


    public EntityActionBeanSupport() {
    }

    public EntityActionBeanSupport(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Returns the type of the entity
     */
    public Class<E> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
        entityInfo = null;
    }


    public EntityInfo getEntityInfo() {
        if (entityInfo == null) {
            entityInfo = createEntityInfo();
        }
        return entityInfo;
    }


    /**
     * Returns the type of the primary key of the entity
     *
     * @return
     */
    public Class getIdClass() {
        return getEntityInfo().getIdClass();
    }

    /**
     * Returns the URI of the Crud action (i.e. this POJO :)
     */
    public String getActionUri() {
        return getEntityInfo().getActionUri();
    }


    protected EntityInfo createEntityInfo() {
        return EntityInfo.newInstance(getEntityClass());
    }

}
