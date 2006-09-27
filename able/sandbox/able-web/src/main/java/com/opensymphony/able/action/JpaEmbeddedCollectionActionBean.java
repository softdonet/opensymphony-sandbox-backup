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

import com.opensymphony.able.service.CrudService;
import com.opensymphony.able.service.JpaCrudService;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * A helper class to make it easy to create a JPA based implementation of {@link EmbeddedCollectionActionBean}
 * with minimal extra coding.
 *
 * @version $Revision$
 */
public class JpaEmbeddedCollectionActionBean<O, E> extends EmbeddedCollectionActionBean<O, E> {
    private CrudService<E> service;
    @PersistenceContext
    @SpringBean
    private JpaTemplate jpaTemplate;

    public JpaEmbeddedCollectionActionBean(String propertyName, Class<O> ownerClass, Class<E> entityClass) {
        super(propertyName, ownerClass, entityClass);
    }

    public CrudService<E> getService() {
        if (service == null) {
            service = createService();
        }
        return service;
    }

    protected CrudService<E> createService() {
        return new JpaCrudService<E>(getEntityClass(), jpaTemplate);
    }

    public List<E> getAllEntities() {
        return getService().findAll();
    }
}
