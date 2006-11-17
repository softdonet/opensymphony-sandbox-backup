package com.opensymphony.able.demo.model;

import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple collection of people
 */
@Entity
public class Team {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;
    @OneToMany()
    private List<Person> members = new ArrayList<Person>();

    public Team() {
    }

    /**
     * Perform equality checks based on identity.
     */
    public boolean equals(Object obj) {
        return (obj instanceof Team) && this.id == ((Team) obj).id;
    }

    /**
     * Gets the ID of the Component.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the Component.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getMembers() {
        return members;
    }

    public void setMembers(List<Person> members) {
        this.members = members;
	}
}
