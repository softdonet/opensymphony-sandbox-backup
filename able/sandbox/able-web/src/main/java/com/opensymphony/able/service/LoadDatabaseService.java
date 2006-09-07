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
package com.opensymphony.able.service;

import com.opensymphony.able.model.User;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 * TODO replace with an XML based alternative
 * 
 * @version $Revision$
 */
public class LoadDatabaseService implements InitializingBean {

	private JpaTemplate jpaTemplate;
	private final TransactionTemplate transactionTemplate;

	public LoadDatabaseService(JpaTemplate jpaTemplate,
			TransactionTemplate transactionTemplate) {
		this.jpaTemplate = jpaTemplate;
		this.transactionTemplate = transactionTemplate;
	}

	public void afterPropertiesSet() throws Exception {
		transactionTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				return jpaTemplate.execute(new JpaCallback() {
					public Object doInJpa(EntityManager entityManager)
							throws PersistenceException {
						return addSomeUsers(entityManager);
					}
				});
			}
		});
	}

	protected Object addSomeUsers(EntityManager entityManager) {
		User user = new User();
		user.setName("Patrick Lightbody");
		user.setUsername("plightbody");
		entityManager.persist(user);

		user = new User();
		user.setName("Tim Fennell");
		user.setUsername("tfennell");
		entityManager.persist(user);

		user = new User();
		user.setName("James Strachan");
		user.setUsername("jstrachan");
		entityManager.persist(user);

		return null;
	}
}
