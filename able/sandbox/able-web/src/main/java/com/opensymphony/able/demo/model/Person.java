package com.opensymphony.able.demo.model;

import com.opensymphony.able.annotations.Input;
import com.opensymphony.able.annotations.InputType;
import com.opensymphony.able.annotations.ViewField;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Represents a person to whom bugs can be assigned.
 *
 * @author Tim Fennell
 */
@Entity
@ViewField(includes = {"firstName", "lastName"})
public class Person {
    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Input(type = InputType.PickList)
    @OneToMany()
    private List<Component> leads;
    @ManyToOne
    private Team team;

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

    public List<Component> getLeads() {
        return leads;
    }

    public void setLeads(List<Component> leads) {
        this.leads = leads;
    }

    public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
