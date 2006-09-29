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
package com.opensymphony.able.stripes;

import com.opensymphony.able.action.JpaEmbeddedCollectionActionBean;
import com.opensymphony.able.model.Component;
import com.opensymphony.able.model.Person;

/**
 * TODO code generate or create dynamically at runtime?
 *
 * This action bean is used to view/edit the {@link Person#getLeads()} collection
 *
 * @version $Revision$
 */
public class Person_leadsActionBean extends JpaEmbeddedCollectionActionBean<Person, Component> {

    public Person_leadsActionBean() {
        super("leads", Person.class, Component.class);
    }
}
