package com.opensymphony.able.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Build {
    @Id
    @GeneratedValue
    private Long id;
    private long build;
        
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

    public String toString() {
        return String.valueOf(build);
    }
}
