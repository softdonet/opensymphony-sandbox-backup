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
package com.opensymphony.able.jpa;

import com.opensymphony.able.demo.action.PersonActionBean;
import com.opensymphony.able.demo.model.Person;
import com.opensymphony.able.service.JpaCrudService;
import net.sourceforge.stripes.integration.spring.SpringHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

/**
 * 
 * @version $Revision$
 */
public class JpaInjectionTest extends SpringTestSupport {

    @DataProvider(name = "springUriWithEntityManager")
    public String[][] getSpringFiles() {
        return new String[][] { { "spring.xml" } };
    };

    @Test(dataProvider = "springUriWithEntityManager")
    public void testInjectionOfActionBeans(String classpathUri) throws Exception {
        ApplicationContext context = loadContext(classpathUri);

        String[] names = context.getBeanNamesForType(EntityManagerFactory.class);
        Assert.assertEquals(1, names.length, "number of names");

        final PersonActionBean target = new PersonActionBean();
        SpringHelper.injectBeans(target, context);

        JpaCrudService<Person> service = (JpaCrudService<Person>) target.getService();

        final JpaTemplate jpaTemplate = service.getJpaTemplate();
        Assert.assertNotNull(jpaTemplate, "Failed to inject an JpaTemplate: " + jpaTemplate);

        System.out.println("Found template: " + jpaTemplate);

        EntityManager entityManager = (EntityManager) jpaTemplate.execute(new JpaCallback() {
            public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                return entityManager;
            }
        });

        Assert.assertNotNull(entityManager, "Failed to inject an EntityManager: " + entityManager);
    }

}
