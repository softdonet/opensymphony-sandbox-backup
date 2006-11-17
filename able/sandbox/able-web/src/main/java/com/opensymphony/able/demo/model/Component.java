package com.opensymphony.able.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Class that represents a compopnent of a software system against which bugs
 * can be filed.
 * 
 * @author Tim Fennell
 */
@Entity
public class Component {
	private Integer id;
	private String name;
	private Person lead;

	/** Default constructor. */
	public Component() {
	}

	/** Constructs a new component with the supplied name. */
	public Component(String name) {
		this.name = name;
	}

	/** Perform equality checks based on identity. */
	public boolean equals(Object obj) {
		return (obj instanceof Component) && this.id == ((Component) obj).id;
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

	/** Gets the name of the Component - may be null if one is not set. */
	public String getName() {
		return name;
	}

	/** Sets the name of the Component. */
	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	public Person getLead() {
		return lead;
	}

	public void setLead(Person lead) {
		this.lead = lead;
	}
}
