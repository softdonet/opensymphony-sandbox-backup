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
	private Integer id;
	private String name;
	private List<Person> members = new ArrayList<Person>();

	public Team() {
	}

	/** Perform equality checks based on identity. */
	public boolean equals(Object obj) {
		return (obj instanceof Team) && this.id == ((Team) obj).id;
	}

	/** Gets the ID of the Component. */
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	/** Sets the ID of the Component. */
	public void setId(Integer id) {
		this.id = id;
	}

    @NotNull
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany()
	public List<Person> getMembers() {
		return members;
	}

	public void setMembers(List<Person> members) {
		this.members = members;
	}
}
