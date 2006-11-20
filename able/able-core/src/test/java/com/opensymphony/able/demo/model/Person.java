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
package com.opensymphony.able.demo.model;

import com.opensymphony.able.annotations.ViewField;
import com.opensymphony.able.annotations.Input;
import com.opensymphony.able.annotations.InputType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import java.util.List;

/**
 * Represents a person to whom bugs can be assigned.
 *
 * @author Tim Fennell
 */
@Entity
@ViewField(includes = {"firstName", "lastName"})
@Searchable
public class Person {
    @Id
    @GeneratedValue
    @SearchableId
    private Integer id;
    @SearchableProperty
    private String username;
    @SearchableProperty
    private String firstName;
    @Input(label = "Surname")
    @SearchableProperty
    private String lastName;
    @SearchableProperty
    private String email;
    private String password;
    private Role role;

    /**
     * Default constructor.
     */
    public Person() {
    }

    /**
     * Constructs a well formed person.
     */
    public Person(String username, String password, String first, String last, String email) {
        this.username = username;
        this.password = password;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
    }

    /**
     * Equality is determined to be when the ID numbers match.
     */
    public boolean equals(Object obj) {
        return (obj instanceof Person) && this.id == ((Person) obj).id;
    }

    @Override
    public String toString() {
        return "Persion[id:" + id + "]";
    }

    /**
     * Gets the ID of the person.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the person.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the username of the person.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the first name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the person's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the person's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the person's unencrypted password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the person's unencrypted password.
     */
    public void setPassword(String password) {
        this.password = password;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
