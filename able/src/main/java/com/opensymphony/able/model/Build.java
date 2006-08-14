package com.opensymphony.able.model;

public class Build {
    private long build;

    public Build() {
    }

    public Build(long build) {
        this.build = build;
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
