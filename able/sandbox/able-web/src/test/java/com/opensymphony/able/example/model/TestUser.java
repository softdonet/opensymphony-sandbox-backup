package com.opensymphony.able.example.model;

import com.opensymphony.able.annotations.DisplayBulkEdit;
import com.opensymphony.able.annotations.DisplayDefaults;
import com.opensymphony.able.annotations.DisplayView;
import com.opensymphony.able.annotations.Input;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * @author Nicholas Hill <a
 *         href="mailto:nick.hill@gmail.com">nick.hill@gmail.com</a>
 * @author <a href="mailto:jhouse@revolition.net">James House</a>
 */
@Entity
@DisplayDefaults(sortOrder = { "username", "name", "email", "type" })
@DisplayBulkEdit(excludes = { "name", "email" })
@DisplayView(excludes = { "creationDate" })
public class TestUser {
	private Long id;
	private String username;
	private String email;
	private String name;
	private String passwordHash;
	private Date creationDate;
	private Date updateDate;
	private Type type;
	private List<TestProject> projects;
	private Address[] addresses;

	public TestUser() {
	}

	public TestUser(String username, String email, String name, String passwordHash) {
		this.username = username;
		this.email = email;
		this.name = name;
		this.passwordHash = passwordHash;
		this.creationDate = new Date();
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final TestUser user = (TestUser) o;

		return id == user.id;
	}

	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Input(label = "User account name")
    @NotNull
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Address[] getAddresses() {
		return addresses;
	}

	public void setAddresses(Address[] addresses) {
		this.addresses = addresses;
	}

    @OneToMany
    public List<TestProject> getProjects() {
		return projects;
	}

	public void setProjects(List<TestProject> projects) {
		this.projects = projects;
	}

}
