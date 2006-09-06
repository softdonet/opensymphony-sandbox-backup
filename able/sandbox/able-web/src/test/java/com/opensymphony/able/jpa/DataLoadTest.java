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

import com.opensymphony.able.action.UserActionBean;
import com.opensymphony.able.model.User;
import com.opensymphony.able.service.LoadDatabaseService;

import net.sourceforge.stripes.integration.spring.SpringHelper;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 
 * @version $Revision$
 */
public class DataLoadTest extends SpringTestSupport {
    @DataProvider(name = "springUriWithEntityManager")
    public String[][] getSpringFiles() {
        return new String[][] { { "spring.xml" } };
    };

    @Test(dataProvider = "springUriWithEntityManager")
    public void testLoadOfSomeData(String classpathUri) throws Exception {
        final ApplicationContext context = loadContext(classpathUri);

        JpaTemplate jpaTemplate = (JpaTemplate) getMandatoryBean(context, "jpaTemplate");
        TransactionTemplate txnTemplate = (TransactionTemplate) getMandatoryBean(context, "transactionTemplate");
        
        LoadDatabaseService loadService = new LoadDatabaseService(jpaTemplate, txnTemplate);
        loadService.afterPropertiesSet();
        
        txnTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                assertDataPresent(context);
                return null;
            }
        });

    }

    protected void assertDataPresent(ApplicationContext context) {
        UserActionBean action = new UserActionBean();
        SpringHelper.injectBeans(action, context);

        List<User> allEntities = action.getAllEntities();
        System.out.println("Found users: " + allEntities);

        Assert.assertTrue(allEntities.size() > 1, "Should have some users in the database now!");
    }

}