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

import java.util.List;

/**
 * Represents a basic CRUD service on a particular type of entity which can be extended to add business operations or custom queries.
 *
 * @version $Revision$
 */
public interface CrudService<E> {

    /**
     * Loads the entity by primary key
     */
    public E findById(Object id);

    /**
     * Creates a new instance
     */
    public E newInstance() throws IllegalAccessException, InstantiationException;

    /**
     * Makes the entity persistent
     */
    public void insert(E entity);

    /**
     * Performs an update to the entity
     */
    public void update(E entity);

    /**
     * Deletes the given entity
     */
    public void delete(E entity);

    /**
     * Finds all the entities
     */
    public List<E> findAll();

    /**
     * Returns the type of the entity
     */
    Class<E> getEntityClass();
}
