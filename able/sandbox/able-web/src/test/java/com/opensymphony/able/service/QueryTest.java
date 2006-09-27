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

import com.opensymphony.able.jpa.SpringTestSupport;
import com.opensymphony.able.model.User;
import net.sourceforge.stripes.integration.spring.SpringHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @version $Revision$
 */
public class QueryTest extends SpringTestSupport {

    @DataProvider(name = "spring")
    public String[][] getSpringFiles() {
//        return new String[][]{{"applicationContext.xml"}};
        return new String[][]{{"/com/opensymphony/able/jpa/spring.xml"}};
    }


    @Test(dataProvider = "spring")
    public void testNativeSql(String classpathUri) throws Exception {
        ApplicationContext context = loadContext(classpathUri);

        final SampleUserService service = new SampleUserService();
        SpringHelper.injectBeans(service, context);

        TransactionTemplate transactionTemplate = (TransactionTemplate) getMandatoryBean(context, "transactionTemplate");


        transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus transactionStatus) {
                User user = new User();
                user.setUsername("james");
                user.setName("James Strachan");
                service.getJpaTemplate().persist(user);
                return null;
            }
        });

        transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus transactionStatus) {

                List<User> list = service.findUserViaName("james");
                System.out.println("Found list: " + list);

                list = service.findUserViaNameUsingParameter("james");
                System.out.println("Found list: " + list);

                return null;
            }
        });
    }
}
