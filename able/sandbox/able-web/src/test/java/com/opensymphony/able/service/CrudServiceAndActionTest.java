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

import com.opensymphony.able.action.TestFixture;
import com.opensymphony.able.jpa.SpringTestSupport;
import com.opensymphony.able.model.Person;
import net.sourceforge.stripes.exception.StripesServletException;
import net.sourceforge.stripes.integration.spring.SpringHelper;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @version $Revision$
 */
public class CrudServiceAndActionTest extends SpringTestSupport {

    @DataProvider(name = "spring")
    public String[][] getSpringFiles() {
//        return new String[][]{{"applicationContext.xml"}};
        return new String[][]{{"/com/opensymphony/able/jpa/spring.xml"}};
    }


    @Test(dataProvider = "spring")
    public void testCrudServices(String classpathUri) throws Exception {
        ApplicationContext context = loadContext(classpathUri);

        final PersonService service = new PersonService();
        SpringHelper.injectBeans(service, context);

        TransactionTemplate transactionTemplate = (TransactionTemplate) getMandatoryBean(context, "transactionTemplate");


        transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus transactionStatus) {
                Person user = new Person();
                user.setUsername("james");
                user.setFirstName("James");
                service.getJpaTemplate().persist(user);
                return null;
            }
        });

        transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus transactionStatus) {

                try {
                    testNativeSql(service);
                }
                catch (Exception e) {
                    System.out.println("Caught: " + e);
                    e.printStackTrace(System.out);
                    System.out.println("done");
                    throw new RuntimeException(e);
                }

                return null;
            }
        });

        // TODO can't seem to get this to work...
        //testBinding(service);
    }

    public void testBinding(PersonService service) throws Exception {

        MockServletContext ctx = TestFixture.getServletContext();

        MockRoundtrip trip = new MockRoundtrip(ctx, PersonActionBean.class);
        trip.setParameter("entity", "1");
        try {
            trip.execute();
        }
        catch (StripesServletException e) {
            Throwable cause = e.getRootCause();
            System.out.println("Caught: " + cause);
            cause.printStackTrace(System.out);
            throw e;
        }

        PersonActionBean action = trip.getActionBean(PersonActionBean.class);

        action.getEntity();

        /*
        Locale locale = Locale.getDefault();
        ActionBeanContext context = new ActionBeanContext() {
            @Override
            public Resolution getSourcePageResolution() {
                return new ForwardResolution("/sourcePage");
            }
        };

        MockHttpServletRequest request = new MockHttpServletRequest("/", "/Person.action");
        request.getParameterMap().put("entity", new String[]{"1"});
        context.setRequest(request);

        DefaultCrudActionBean action = new DefaultCrudActionBean(service);

        action.setContext(context);

        // lets bind to the request parameters
        DefaultActionBeanPropertyBinder binder = new DefaultActionBeanPropertyBinder();
        DefaultConfiguration configuration = new DefaultConfiguration();
        MockFilterConfig filterConfig = new MockFilterConfig();
        filterConfig.setServletContext(TestFixture.getServletContext());
        
        configuration.setBootstrapPropertyResolver(new BootstrapPropertyResolver(filterConfig));
        configuration.init();
        binder.init(configuration);

        binder.bind(action, context, false);

        // now lets invoke the action
        Resolution resolution = action.list();
        */

        Object entity = action.getEntity();

        assertNotNull(entity, "Should have loaded an entity!");

        System.out.println("Found entity: " + entity);
    }

    public void testNativeSql(PersonService service) {
        List<Person> list = service.findPersonViaName("james");
        System.out.println("Found list: " + list);

        list = service.findPersonViaNameUsingParameter("james");
        System.out.println("Found list: " + list);

    }
}
