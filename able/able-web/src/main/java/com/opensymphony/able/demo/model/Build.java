package com.opensymphony.able.demo.model;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Searchable
public class Build {
    @Id
    @GeneratedValue
    @SearchableId
    private long id;
    @SearchableProperty
    @NotNull
    private long build;
    @SearchableProperty
    private String tag;
    @SearchableProperty
    private String description;
        
    public Build() {
    }

    public Build(long build) {
        this.build = build;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String toString() {
        return String.valueOf(build);
    }
}
