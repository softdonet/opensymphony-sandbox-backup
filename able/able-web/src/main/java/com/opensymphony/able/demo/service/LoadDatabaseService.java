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
package com.opensymphony.able.demo.service;

import com.opensymphony.able.demo.model.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Date;

/**
 * TODO replace with an XML based alternative
 *
 * @version $Revision$
 */
public class LoadDatabaseService implements InitializingBean {

    private JpaTemplate jpaTemplate;
    private final TransactionTemplate transactionTemplate;
    private EntityManager entityManager;

    public LoadDatabaseService(JpaTemplate jpaTemplate,
                               TransactionTemplate transactionTemplate) {
        this.jpaTemplate = jpaTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public void afterPropertiesSet() throws Exception {
        transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                return jpaTemplate.execute(new JpaCallback() {
                    public Object doInJpa(EntityManager em)
                            throws PersistenceException {
                        entityManager = em;

                        populateUsers();
                        populateBugs();

                        return null;

                    }
                });
            }
        });
    }

    protected void persist(Object object) {
        entityManager.persist(object);
    }

    protected void populateUsers() {
        User user;
        /*
        removed: will be insertet by SQL script..
        user = new User("plightbo", "plightbo@gmail.com", "Patrick Lightbody", "z8yZIIFLQ14VbFs5r8uHL5ecH5U=");
        persist(user);*/

        user = new User("tfennel", "tim@fennel.com", "Tim Fennell", "z8yZIIFLQ14VbFs5r8uHL5ecH5U=");
        persist(user);

        user = new User("jstrachan", "james@strachan.com", "James Strachan", "z8yZIIFLQ14VbFs5r8uHL5ecH5U=");
        persist(user);

        user = new User("kjetilhp", "kjetil@java.no", "Kjetil H.Paulsen", "z8yZIIFLQ14VbFs5r8uHL5ecH5U=");
        persist(user);
    }

    protected void populateBugs() {
        Person person0 = new Person("scooby", "scooby", "Scooby", "Doo",
                "scooby@mystery.machine.tv");
        persist(person0);

        Person person1 = new Person("shaggy", "shaggy", "Shaggy", "Rogers",
                "shaggy@mystery.machine.tv");
        persist(person1);

        Person person2 = new Person("scrappy", "scrappy", "Scrappy", "Doo",
                "scrappy@mystery.machine.tv");
        persist(person2);

        Person person3 = new Person("daphne", "daphne", "Daphne", "Blake",
                "daphne@mystery.machine.tv");
        persist(person3);

        Person person4 = new Person("velma", "velma", "Velma", "Dinkly",
                "velma@mystery.machine.tv");
        persist(person4);

        Person person5 = new Person("fred", "fred", "Fred", "Jones",
                "fred@mystery.machine.tv");
        persist(person5);

        Component component0 = new Component("Core");
        persist(component0);

        Component component1 = new Component("Common");
        persist(component1);

        Component component2 = new Component("Hibernate");
        persist(component2);

        Component component3 = new Component("Apache");
        persist(component3);

        Component component4 = new Component("OpenSymphony");
        persist(component4);

        Bug bug = new Bug();
        bug.setShortDescription("First ever bug in the system.");
        bug
                .setLongDescription("This is a test bug, and is the first one ever made.");
        bug.setOpenDate(new Date());
        bug.setStatus(Status.Resolved);
        bug.setPriority(Priority.High);
        bug.setComponent(component0);
        bug.setOwner(person3);
        persist(bug);

        bug = new Bug();
        bug.setShortDescription("Another bug!  Oh no!.");
        bug.setLongDescription("How terrible - I found another bug.");
        bug.setOpenDate(new Date());
        bug.setStatus(Status.Assigned);
        bug.setPriority(Priority.Blocker);
        bug.setComponent(component2);
        bug.setOwner(person4);
        persist(bug);

        bug = new Bug();
        bug
                .setShortDescription("Three bugs?  This is just getting out of hand.");
        bug.setLongDescription("What kind of system has three bugs?  Egads.");
        bug.setOpenDate(new Date());
        bug.setStatus(Status.New);
        bug.setPriority(Priority.High);
        bug.setComponent(component0);
        bug.setOwner(person1);
        persist(bug);

        bug = new Bug();
        bug.setShortDescription("Oh good lord - I found a fourth bug.");
        bug
                .setLongDescription("That's it, you're all fired.  I need some better developers.");
        bug.setOpenDate(new Date());
        bug.setStatus(Status.New);
        bug.setPriority(Priority.Critical);
        bug.setComponent(component3);
        bug.setOwner(person0);
        persist(bug);

        bug = new Bug();
        bug.setShortDescription("Development team gone missing.");
        bug
                .setLongDescription("No, wait! I didn't mean it!  Please come back and fix the bugs!!");
        bug.setOpenDate(new Date());
        bug.setStatus(Status.New);
        bug.setPriority(Priority.Blocker);
        bug.setComponent(component2);
        bug.setOwner(person5);
        persist(bug);

        Team team = new Team();
        team.setName("Stripes");
        team.getMembers().add(person0);
        team.getMembers().add(person1);
        persist(team);

        team = new Team();
        team.setName("Able");
        team.getMembers().add(person3);
        team.getMembers().add(person5);
        persist(team);

    }
}
