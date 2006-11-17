package com.opensymphony.able.demo.model;

import org.hibernate.validator.Email;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Nicholas Hill <a
 *         href="mailto:nick.hill@gmail.com">nick.hill@gmail.com</a>
 * @author <a href="mailto:jhouse@revolition.net">James House</a>
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String username;
    @Email
    private String email;
    private String name;
    private String passwordHash;

    public User() {
    }

    public User(String username, String email, String name, String passwordHash) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final User user = (User) o;

        return id == user.id;
    }

    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


    @Override
    public String toString() {
        return "User[id=" + id + ", username=" + username + ", name=" + name + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
