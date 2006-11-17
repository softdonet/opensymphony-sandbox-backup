package com.opensymphony.able.demo.model;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Searchable
public class Build {
    @Id
    @GeneratedValue
    @SearchableId
    private Long id;
    private long build;
    @SearchableProperty
    private String description;
        
    public Build() {
    }

    public Build(long build) {
        this.build = build;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBuild() {
        return build;
    }

    public void setBuild(long build) {
        this.build = build;
    }

    public void increment() {
        build++;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return String.valueOf(build);
    }
}
