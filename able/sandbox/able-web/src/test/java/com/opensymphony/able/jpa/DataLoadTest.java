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
import com.opensymphony.able.jaxb.Envelope;
import com.opensymphony.able.jaxb.JaxbTemplate;
import com.opensymphony.able.model.User;
import com.opensymphony.able.service.LoadDatabaseService;
import net.sourceforge.stripes.integration.spring.SpringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 
 * @version $Revision$
 */
public class DataLoadTest extends SpringTestSupport {
	private static final Log log = LogFactory.getLog(DataLoadTest.class);

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

		Exception e = (Exception) txnTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				return assertDataPresent(context);
			}
		});
		if (e != null) {
			Assert.fail("Failed to popupate: " + e, e);
		}
	}

	protected Object assertDataPresent(ApplicationContext context) {
		UserActionBean action = new UserActionBean();
		SpringHelper.injectBeans(action, context);

		List<User> allEntities = action.getAllEntities();
		System.out.println("Found users: " + allEntities);

		Envelope envelope = new Envelope();
		envelope.setUserList(allEntities);

		Assert.assertTrue(allEntities.size() > 1, "Should have some users in the database now!");

		JaxbTemplate template = new JaxbTemplate(new Class[] { Envelope.class, User.class });
		File dir = new File("target/data");
		dir.mkdirs();
		FileOutputStream out = null;
		try {
			File file = new File(dir, "users.xml");
			System.out.println("Writing file: " + file);
			log.info("file: " + file);
			out = new FileOutputStream(file);
			template.write(out, envelope);
			return null;
		} catch (Exception e) {
			log.error(e);
			return e;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				log.error("Failed to close file: " + e, e);
			}
		}
	}

}
